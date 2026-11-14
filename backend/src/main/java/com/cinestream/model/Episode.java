package com.cinestream.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "episode", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"season_id", "episode_number"})
})
public class Episode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    @JsonIgnore
    private Season season;

    @Column(name = "episode_number", nullable = false)
    private Integer episodeNumber;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer duration;

    @Column(name = "video_key", length = 255)
    private String videoKey;

    public Episode() {
    }

    public Episode(Long id, Season season, Integer episodeNumber, String title, String description, Integer duration, String videoKey) {
        this.id = id;
        this.season = season;
        this.episodeNumber = episodeNumber;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.videoKey = videoKey;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getVideoKey() {
        return videoKey;
    }

    public void setVideoKey(String videoKey) {
        this.videoKey = videoKey;
    }
}
