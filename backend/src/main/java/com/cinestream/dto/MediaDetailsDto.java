package com.cinestream.dto;

import com.cinestream.model.MediaType;
import java.math.BigDecimal;
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
    private BigDecimal rentalPricePerDay;
    private List<GenreDto> genres;
    private List<DirectorDto> directors;
    private List<MediaActorDto> actors;

    public MediaDetailsDto() {
    }

    public MediaDetailsDto(Long id, String title, String description, int releaseYear, MediaType type,
                           Integer duration, String language, String country, int minimumAge,
                           String posterUrl, String trailerUrl, BigDecimal rentalPricePerDay,
                           List<GenreDto> genres, List<DirectorDto> directors, List<MediaActorDto> actors) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.type = type;
        this.duration = duration;
        this.language = language;
        this.country = country;
        this.minimumAge = minimumAge;
        this.posterUrl = posterUrl;
        this.trailerUrl = trailerUrl;
        this.rentalPricePerDay = rentalPricePerDay;
        this.genres = genres;
        this.directors = directors;
        this.actors = actors;
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
}
