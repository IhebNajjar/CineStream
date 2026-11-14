package com.cinestream.service;

import com.cinestream.dto.ReviewDto;
import com.cinestream.dto.ReviewRequest;
import com.cinestream.exception.ResourceNotFoundException;
import com.cinestream.model.Media;
import com.cinestream.model.Review;
import com.cinestream.model.Role;
import com.cinestream.model.User;
import com.cinestream.repository.MediaRepository;
import com.cinestream.repository.ReviewRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MediaRepository mediaRepository;

    public ReviewService(ReviewRepository reviewRepository, MediaRepository mediaRepository) {
        this.reviewRepository = reviewRepository;
        this.mediaRepository = mediaRepository;
    }

    public List<ReviewDto> getReviewsForMedia(Long mediaId, Optional<User> currentUser) {
        return reviewRepository.findByMediaIdOrderByCreatedAtDesc(mediaId)
                .stream()
                .map(r -> mapToDto(r, currentUser))
                .collect(Collectors.toList());
    }

    public Double getAverageRating(Long mediaId) {
        Double avg = reviewRepository.calculateAverageRatingForMedia(mediaId);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : null;
    }

    @Transactional
    public ReviewDto addOrUpdateReview(User user, Long mediaId, ReviewRequest request) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Média non trouvé avec l'id: " + mediaId));

        Review review = reviewRepository.findByUserIdAndMediaId(user.getId(), mediaId)
                .orElseGet(() -> {
                    Review r = new Review();
                    r.setUser(user);
                    r.setMedia(media);
                    return r;
                });

        review.setRating(request.getRating());
        review.setComment(request.getComment() != null ? request.getComment().trim() : null);

        Review saved = reviewRepository.save(review);
        return mapToDto(saved, Optional.of(user));
    }

    @Transactional
    public ReviewDto updateReview(User user, Long reviewId, ReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Avis non trouvé avec l'id: " + reviewId));

        if (!review.getUser().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Vous ne pouvez pas modifier un avis qui ne vous appartient pas.");
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment() != null ? request.getComment().trim() : null);

        Review saved = reviewRepository.save(review);
        return mapToDto(saved, Optional.of(user));
    }

    @Transactional
    public void deleteReview(User user, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Avis non trouvé avec l'id: " + reviewId));

        // US018 (own review) & US019 (admin moderation)
        if (!review.getUser().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Vous n'êtes pas autorisé à supprimer cet avis.");
        }

        reviewRepository.delete(review);
    }

    private ReviewDto mapToDto(Review review, Optional<User> currentUser) {
        ReviewDto dto = new ReviewDto();
        dto.setId(review.getId());
        dto.setUserId(review.getUser().getId());
        dto.setUserEmail(maskEmail(review.getUser().getEmail()));
        dto.setMediaId(review.getMedia().getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());

        boolean isOwn = currentUser.isPresent() &&
                (currentUser.get().getId().equals(review.getUser().getId()) || currentUser.get().getRole() == Role.ADMIN);
        dto.setOwnReview(isOwn);
        return dto;
    }

    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) return "Utilisateur";
        String[] parts = email.split("@");
        String name = parts[0];
        if (name.length() <= 2) return name + "***@" + parts[1];
        return name.substring(0, 2) + "***@" + parts[1];
    }
}
