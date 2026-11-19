package com.cinestream.service;

import com.cinestream.dto.RentalDto;
import com.cinestream.dto.RentalRequest;
import com.cinestream.dto.WatchLinkResponse;
import com.cinestream.exception.ResourceNotFoundException;
import com.cinestream.model.*;
import com.cinestream.repository.MediaRepository;
import com.cinestream.repository.RentalRepository;
import com.cinestream.repository.WalletRepository;
import com.cinestream.repository.WalletTransactionRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final MediaRepository mediaRepository;
    private final WalletRepository walletRepository;
    private final WalletTransactionRepository transactionRepository;
    private final R2StorageService r2StorageService;

    public RentalService(RentalRepository rentalRepository, MediaRepository mediaRepository,
                         WalletRepository walletRepository, WalletTransactionRepository transactionRepository,
                         R2StorageService r2StorageService) {
        this.rentalRepository = rentalRepository;
        this.mediaRepository = mediaRepository;
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.r2StorageService = r2StorageService;
    }

    @Transactional
    public RentalDto createRental(User user, RentalRequest request) {
        Media media = mediaRepository.findById(request.getMediaId())
                .orElseThrow(() -> new ResourceNotFoundException("Média non trouvé avec l'id: " + request.getMediaId()));

        LocalDateTime now = LocalDateTime.now();

        // US026: Check if media is already actively rented by the user
        rentalRepository.findFirstByUserIdAndMediaIdAndStatusAndEndDateAfter(
                user.getId(), media.getId(), RentalStatus.EN_COURS, now
        ).ifPresent(r -> {
            throw new IllegalStateException("Vous avez déjà une location en cours pour le titre « " + media.getTitle() + " ».");
        });

        // US027: Mandatory server-side price recalculation
        int days = (request.getDays() != null && request.getDays() >= 1) ? request.getDays() : 1;
        BigDecimal totalPrice = media.getRentalPricePerDay().multiply(BigDecimal.valueOf(days));

        // US024 & US025: Check virtual wallet balance
        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseGet(() -> walletRepository.save(new Wallet(user, BigDecimal.ZERO)));

        if (wallet.getBalance().compareTo(totalPrice) < 0) {
            throw new IllegalStateException("Solde insuffisant (" + wallet.getBalance() + " € disponibles, " +
                    totalPrice + " € requis). Veuillez recharger votre portefeuille.");
        }

        // Deduct balance and record transaction
        wallet.setBalance(wallet.getBalance().subtract(totalPrice));
        walletRepository.save(wallet);

        WalletTransaction tx = new WalletTransaction(
                wallet,
                totalPrice.negate(),
                TransactionType.LOCATION,
                "Location de « " + media.getTitle() + " » pour " + days + " jour" + (days > 1 ? "s" : "")
        );
        transactionRepository.save(tx);

        // Create rental
        Rental rental = new Rental();
        rental.setUser(user);
        rental.setMedia(media);
        rental.setStartDate(now);
        rental.setEndDate(now.plusDays(days));
        rental.setDays(days);
        rental.setTotalPrice(totalPrice);
        rental.setStatus(RentalStatus.EN_COURS);

        Rental saved = rentalRepository.save(rental);
        return mapToDto(saved, now);
    }

    @Transactional
    public List<RentalDto> getUserRentals(User user) {
        LocalDateTime now = LocalDateTime.now();
        List<Rental> rentals = rentalRepository.findByUserIdOrderByStartDateDesc(user.getId());

        // Update status for expired rentals
        for (Rental r : rentals) {
            if (r.getStatus() == RentalStatus.EN_COURS && r.getEndDate().isBefore(now)) {
                r.setStatus(RentalStatus.TERMINEE);
                rentalRepository.save(r);
            }
        }

        return rentals.stream()
                .map(r -> mapToDto(r, now))
                .collect(Collectors.toList());
    }

    /**
     * US028 / Sprint 4 Video Streaming:
     * Generates a presigned Cloudflare R2 / S3 watch link valid for a few hours
     * (never longer than remaining rental time). Link is never stored in DB.
     */
    @Transactional(readOnly = true)
    public WatchLinkResponse getWatchLink(User user, Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("Location non trouvée: " + rentalId));

        if (!rental.getUser().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Cette location ne vous appartient pas.");
        }

        LocalDateTime now = LocalDateTime.now();
        if (rental.getEndDate().isBefore(now) || rental.getStatus() == RentalStatus.TERMINEE) {
            throw new IllegalStateException("Cette location est expirée. Veuillez relouer ce titre pour le regarder.");
        }

        Duration remainingTime = Duration.between(now, rental.getEndDate());
        // Max 4 hours presigned link, capped strictly by remaining rental time
        Duration linkDuration = remainingTime.compareTo(Duration.ofHours(4)) > 0
                ? Duration.ofHours(4)
                : remainingTime;

        if (linkDuration.isNegative() || linkDuration.isZero()) {
            throw new IllegalStateException("Cette location vient d'expirer.");
        }

        String videoKey = rental.getMedia().getVideoKey();
        if (videoKey == null || videoKey.trim().isEmpty()) {
            videoKey = "demo_" + rental.getMedia().getId() + ".mp4";
        }

        String presignedUrl = r2StorageService.generatePresignedWatchUrl(videoKey, linkDuration);
        LocalDateTime expiresAt = now.plus(linkDuration);

        return new WatchLinkResponse(
                presignedUrl,
                expiresAt,
                rental.getMedia().getTitle(),
                rental.getMedia().getType().name(),
                rental.getId()
        );
    }

    private RentalDto mapToDto(Rental rental, LocalDateTime now) {
        RentalDto dto = new RentalDto();
        dto.setId(rental.getId());
        dto.setUserId(rental.getUser().getId());
        dto.setMediaId(rental.getMedia().getId());
        dto.setMediaTitle(rental.getMedia().getTitle());
        dto.setMediaPosterUrl(rental.getMedia().getPosterUrl());
        dto.setMediaType(rental.getMedia().getType().name());
        dto.setStartDate(rental.getStartDate());
        dto.setEndDate(rental.getEndDate());
        dto.setDays(rental.getDays());
        dto.setTotalPrice(rental.getTotalPrice());
        dto.setStatus(rental.getStatus());

        boolean active = rental.getStatus() == RentalStatus.EN_COURS && rental.getEndDate().isAfter(now);
        dto.setActive(active);

        if (active) {
            Duration duration = Duration.between(now, rental.getEndDate());
            dto.setDaysLeft(duration.toDays());
            dto.setHoursLeft(duration.toHours() % 24);
        } else {
            dto.setDaysLeft(0L);
            dto.setHoursLeft(0L);
        }

        return dto;
    }
}
