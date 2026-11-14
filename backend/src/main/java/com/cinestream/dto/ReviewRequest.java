package com.cinestream.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ReviewRequest {

    @NotNull(message = "La note est requise")
    @Min(value = 1, message = "La note minimale est 1")
    @Max(value = 5, message = "La note maximale est 5")
    private Integer rating;

    private String comment;

    public ReviewRequest() {
    }

    public ReviewRequest(Integer rating, String comment) {
        this.rating = rating;
        this.comment = comment;
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
}
