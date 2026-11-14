package com.cinestream.dto;

public class EpisodeDto {
    private Long id;
    private Integer episodeNumber;
    private String title;
    private String description;
    private Integer duration;
    private String videoKey;

    public EpisodeDto() {
    }

    public EpisodeDto(Long id, Integer episodeNumber, String title, String description, Integer duration, String videoKey) {
        this.id = id;
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
