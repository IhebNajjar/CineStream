package com.cinestream.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "season", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"media_id", "season_number"})
})
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id", nullable = false)
    @JsonIgnore
    private Media media;

    @Column(name = "season_number", nullable = false)
    private Integer seasonNumber;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(name = "release_year")
    private Integer releaseYear;

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("episodeNumber ASC")
    private List<Episode> episodes = new ArrayList<>();

    public Season() {
    }

    public Season(Long id, Media media, Integer seasonNumber, String title, Integer releaseYear) {
        this.id = id;
        this.media = media;
        this.seasonNumber = seasonNumber;
        this.title = title;
        this.releaseYear = releaseYear;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
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

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }
}
