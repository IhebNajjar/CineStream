package com.cinestream.dto;

import java.util.ArrayList;
import java.util.List;

public class SeasonDto {
    private Long id;
    private Integer seasonNumber;
    private String title;
    private Integer releaseYear;
    private List<EpisodeDto> episodes = new ArrayList<>();

    public SeasonDto() {
    }

    public SeasonDto(Long id, Integer seasonNumber, String title, Integer releaseYear, List<EpisodeDto> episodes) {
        this.id = id;
        this.seasonNumber = seasonNumber;
        this.title = title;
        this.releaseYear = releaseYear;
        this.episodes = episodes != null ? episodes : new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(Integer seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public List<EpisodeDto> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<EpisodeDto> episodes) {
        this.episodes = episodes;
    }
}
