package com.cinestream.dto;

import java.time.LocalDateTime;

public class ReviewDto {
    private Long id;
    private Long userId;
    private String userEmail;
    private Long mediaId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private boolean ownReview;

    public ReviewDto() {
    }

    public ReviewDto(Long id, Long userId, String userEmail, Long mediaId, Integer rating, String comment, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.userEmail = userEmail;
        this.mediaId = mediaId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isOwnReview() {
        return ownReview;
    }

    public void setOwnReview(boolean ownReview) {
        this.ownReview = ownReview;
    }
}
