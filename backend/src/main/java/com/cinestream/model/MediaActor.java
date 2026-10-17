package com.cinestream.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "media_actor")
public class MediaActor {

    @EmbeddedId
    private MediaActorId id = new MediaActorId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("mediaId")
    @JoinColumn(name = "media_id")
    @JsonIgnore
    private Media media;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("actorId")
    @JoinColumn(name = "actor_id")
    private Actor actor;

    @Column(name = "character_name", nullable = false, length = 150)
    private String characterName;

    public MediaActor() {
    }

    public MediaActor(Media media, Actor actor, String characterName) {
        this.media = media;
        this.actor = actor;
        this.characterName = characterName;
        if (media != null && actor != null) {
            this.id = new MediaActorId(media.getId(), actor.getId());
        }
    }

    public MediaActorId getId() {
        return id;
    }

    public void setId(MediaActorId id) {
        this.id = id;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
        if (media != null) {
            this.id.setMediaId(media.getId());
        }
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
        if (actor != null) {
            this.id.setActorId(actor.getId());
        }
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MediaActor that = (MediaActor) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
