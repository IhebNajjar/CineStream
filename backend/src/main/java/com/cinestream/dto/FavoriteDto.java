package com.cinestream.dto;

import java.time.LocalDateTime;

public class FavoriteDto {
    private Long id;
    private Long userId;
    private Long mediaId;
    private MediaSummaryDto media;
    private LocalDateTime addedAt;

    public FavoriteDto() {
    }

    public FavoriteDto(Long id, Long userId, Long mediaId, MediaSummaryDto media, LocalDateTime addedAt) {
        this.id = id;
        this.userId = userId;
        this.mediaId = mediaId;
        this.media = media;
        this.addedAt = addedAt;
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

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public MediaSummaryDto getMedia() {
        return media;
    }

    public void setMedia(MediaSummaryDto media) {
        this.media = media;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }
}
