package com.cinestream.config;

import com.cinestream.model.*;
import com.cinestream.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final WalletTransactionRepository transactionRepository;
    private final MediaRepository mediaRepository;
    private final RentalRepository rentalRepository;
    private final ReviewRepository reviewRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, WalletRepository walletRepository,
                           WalletTransactionRepository transactionRepository, MediaRepository mediaRepository,
                           RentalRepository rentalRepository, ReviewRepository reviewRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.mediaRepository = mediaRepository;
        this.rentalRepository = rentalRepository;
        this.reviewRepository = reviewRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // 1. Seed Admin
        if (!userRepository.existsByEmail("admin@cinestream.com")) {
            User admin = new User();
            admin.setEmail("admin@cinestream.com");
            admin.setPasswordHash(passwordEncoder.encode("Admin123!"));
            admin.setRole(Role.ADMIN);
            User savedAdmin = userRepository.save(admin);

            Wallet adminWallet = new Wallet(savedAdmin, new BigDecimal("100.00"));
            walletRepository.save(adminWallet);
            logger.info("Created default administrator: admin@cinestream.com / Admin123!");
        }

        // 2. Seed Demo User
        User demoUser;
        if (!userRepository.existsByEmail("user@cinestream.com")) {
            User user = new User();
            user.setEmail("user@cinestream.com");
            user.setPasswordHash(passwordEncoder.encode("User123!"));
            user.setRole(Role.USER);
            demoUser = userRepository.save(user);

            Wallet userWallet = new Wallet(demoUser, new BigDecimal("45.50"));
            walletRepository.save(userWallet);

            WalletTransaction tx = new WalletTransaction(
                    userWallet,
                    new BigDecimal("50.00"),
                    TransactionType.RECHARGE,
                    "Recharge initiale de bienvenue"
            );
            transactionRepository.save(tx);
            logger.info("Created default user: user@cinestream.com / User123!");
        } else {
            demoUser = userRepository.findByEmail("user@cinestream.com").orElse(null);
        }

        // 3. Seed Demo Active Rental for user
        if (demoUser != null && rentalRepository.count() == 0) {
            mediaRepository.findById(1L).ifPresent(inception -> {
                Rental rental = new Rental();
                rental.setUser(demoUser);
                rental.setMedia(inception);
                rental.setStartDate(LocalDateTime.now().minusHours(2));
                rental.setEndDate(LocalDateTime.now().plusDays(5));
                rental.setDays(5);
                rental.setTotalPrice(new BigDecimal("19.95"));
                rental.setStatus(RentalStatus.EN_COURS);
                rentalRepository.save(rental);
                logger.info("Created initial active demo rental for user@cinestream.com: Inception");
            });

            mediaRepository.findById(7L).ifPresent(breakingBad -> {
                Rental expiredRental = new Rental();
                expiredRental.setUser(demoUser);
                expiredRental.setMedia(breakingBad);
                expiredRental.setStartDate(LocalDateTime.now().minusDays(10));
                expiredRental.setEndDate(LocalDateTime.now().minusDays(3));
                expiredRental.setDays(7);
                expiredRental.setTotalPrice(new BigDecimal("42.00"));
                expiredRental.setStatus(RentalStatus.TERMINEE);
                rentalRepository.save(expiredRental);
            });
        }

        // 4. Seed Initial Community Reviews (US017)
        if (reviewRepository.count() == 0 && demoUser != null) {
            mediaRepository.findById(1L).ifPresent(m -> {
                Review r = new Review(demoUser, m, 5, "Un chef-d'œuvre absolu de Christopher Nolan ! La mise en scène et la bande sonore de Hans Zimmer sont inoubliables.");
                reviewRepository.save(r);
            });

            mediaRepository.findById(2L).ifPresent(m -> {
                Review r = new Review(demoUser, m, 5, "Une expérience cinématographique sensorielle et émouvante à couper le souffle.");
                reviewRepository.save(r);
            });

            mediaRepository.findById(3L).ifPresent(m -> {
                Review r = new Review(demoUser, m, 5, "La prestation de Heath Ledger en Joker reste légendaire. Le meilleur film de super-héros jamais produit.");
                reviewRepository.save(r);
            });

            mediaRepository.findById(4L).ifPresent(m -> {
                Review r = new Review(demoUser, m, 4, "Biopic d'une intensité dramatique magistrale, porté par un Cillian Murphy au sommet de son art.");
                reviewRepository.save(r);
            });
        }
    }
}
