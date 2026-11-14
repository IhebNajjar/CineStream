package com.cinestream.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class RentalRequest {

    @NotNull(message = "L'identifiant du média est requis")
    private Long mediaId;

    @NotNull(message = "Le nombre de jours est requis")
    @Min(value = 1, message = "La durée minimale est de 1 jour")
    @Max(value = 90, message = "La durée maximale est de 90 jours")
    private Integer days = 1;

    public RentalRequest() {
    }

    public RentalRequest(Long mediaId, Integer days) {
        this.mediaId = mediaId;
        this.days = days != null ? days : 1;
    }

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }
}
