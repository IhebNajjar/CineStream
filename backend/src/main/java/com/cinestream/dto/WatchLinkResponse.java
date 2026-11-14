package com.cinestream.dto;

import java.time.LocalDateTime;

public class WatchLinkResponse {
    private String watchUrl;
    private LocalDateTime expiresAt;
    private String title;
    private String mediaType;
    private Long rentalId;

    public WatchLinkResponse() {
    }

    public WatchLinkResponse(String watchUrl, LocalDateTime expiresAt, String title, String mediaType, Long rentalId) {
        this.watchUrl = watchUrl;
        this.expiresAt = expiresAt;
        this.title = title;
        this.mediaType = mediaType;
        this.rentalId = rentalId;
    }

    public String getWatchUrl() {
        return watchUrl;
    }

    public void setWatchUrl(String watchUrl) {
        this.watchUrl = watchUrl;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public Long getRentalId() {
        return rentalId;
    }

    public void setRentalId(Long rentalId) {
        this.rentalId = rentalId;
    }
}
