package com.cinestream.controller;

import com.cinestream.dto.ReviewDto;
import com.cinestream.dto.ReviewRequest;
import com.cinestream.model.User;
import com.cinestream.security.SecurityUtils;
import com.cinestream.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class ReviewController {

    private final ReviewService reviewService;
    private final SecurityUtils securityUtils;

    public ReviewController(ReviewService reviewService, SecurityUtils securityUtils) {
        this.reviewService = reviewService;
        this.securityUtils = securityUtils;
    }

    @GetMapping("/api/media/{mediaId}/reviews")
    public ResponseEntity<Map<String, Object>> getReviews(@PathVariable Long mediaId) {
        Optional<User> currentUser = securityUtils.getCurrentUser();
        List<ReviewDto> reviews = reviewService.getReviewsForMedia(mediaId, currentUser);
        Double avg = reviewService.getAverageRating(mediaId);

        return ResponseEntity.ok(Map.of(
                "reviews", reviews,
                "averageRating", avg != null ? avg : 0.0,
                "count", reviews.size()
        ));
    }

    @PostMapping("/api/media/{mediaId}/reviews")
    public ResponseEntity<ReviewDto> postReview(@PathVariable Long mediaId,
                                                @Valid @RequestBody ReviewRequest request) {
        User currentUser = securityUtils.getCurrentUserOrThrow();
        ReviewDto review = reviewService.addOrUpdateReview(currentUser, mediaId, request);
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    @PutMapping("/api/reviews/{id}")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable Long id,
                                                  @Valid @RequestBody ReviewRequest request) {
        User currentUser = securityUtils.getCurrentUserOrThrow();
        return ResponseEntity.ok(reviewService.updateReview(currentUser, id, request));
    }

    @DeleteMapping("/api/reviews/{id}")
    public ResponseEntity<Map<String, String>> deleteReview(@PathVariable Long id) {
        User currentUser = securityUtils.getCurrentUserOrThrow();
        reviewService.deleteReview(currentUser, id);
        return ResponseEntity.ok(Map.of("message", "Avis supprimé avec succès."));
    }
}
