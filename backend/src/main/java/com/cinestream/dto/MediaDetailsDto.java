package com.cinestream.dto;

import com.cinestream.model.MediaType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MediaDetailsDto {
    private Long id;
    private String title;
    private String description;
    private int releaseYear;
    private MediaType type;
    private Integer duration;
    private String language;
    private String country;
    private int minimumAge;
    private String posterUrl;
    private String trailerUrl;
    private String videoKey;
    private BigDecimal rentalPricePerDay;
    private List<GenreDto> genres = new ArrayList<>();
    private List<DirectorDto> directors = new ArrayList<>();
    private List<MediaActorDto> actors = new ArrayList<>();
    private List<SeasonDto> seasons = new ArrayList<>();
    private Double ratingAverage;
    private Long reviewsCount;

    public MediaDetailsDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public MediaType getType() {
        return type;
    }

    public void setType(MediaType type) {
        this.type = type;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getMinimumAge() {
        return minimumAge;
    }

    public void setMinimumAge(int minimumAge) {
        this.minimumAge = minimumAge;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }

    public String getVideoKey() {
        return videoKey;
    }

    public void setVideoKey(String videoKey) {
        this.videoKey = videoKey;
    }

    public BigDecimal getRentalPricePerDay() {
        return rentalPricePerDay;
    }

    public void setRentalPricePerDay(BigDecimal rentalPricePerDay) {
        this.rentalPricePerDay = rentalPricePerDay;
    }

    public List<GenreDto> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreDto> genres) {
        this.genres = genres;
    }

    public List<DirectorDto> getDirectors() {
        return directors;
    }

    public void setDirectors(List<DirectorDto> directors) {
        this.directors = directors;
    }

    public List<MediaActorDto> getActors() {
        return actors;
    }

    public void setActors(List<MediaActorDto> actors) {
        this.actors = actors;
    }

    public List<SeasonDto> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<SeasonDto> seasons) {
        this.seasons = seasons;
    }

    public Double getRatingAverage() {
        return ratingAverage;
    }

    public void setRatingAverage(Double ratingAverage) {
        this.ratingAverage = ratingAverage;
    }

    public Long getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(Long reviewsCount) {
        this.reviewsCount = reviewsCount;
    }
}
