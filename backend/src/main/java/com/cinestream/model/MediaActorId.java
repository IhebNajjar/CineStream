package com.cinestream.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MediaActorId implements Serializable {

    @Column(name = "media_id")
    private Long mediaId;

    @Column(name = "actor_id")
    private Long actorId;

    public MediaActorId() {
    }

    public MediaActorId(Long mediaId, Long actorId) {
        this.mediaId = mediaId;
        this.actorId = actorId;
    }

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public Long getActorId() {
        return actorId;
    }

    public void setActorId(Long actorId) {
        this.actorId = actorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MediaActorId that = (MediaActorId) o;
        return Objects.equals(mediaId, that.mediaId) && Objects.equals(actorId, that.actorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mediaId, actorId);
    }
}
