package com.cinestream.dto;

import com.cinestream.model.MediaType;
import java.math.BigDecimal;
import java.util.List;

public class MediaSummaryDto {
    private Long id;
    private String title;
    private int releaseYear;
    private MediaType type;
    private Integer duration;
    private int minimumAge;
    private String posterUrl;
    private BigDecimal rentalPricePerDay;
    private List<GenreDto> genres;

    public MediaSummaryDto() {
    }

    public MediaSummaryDto(Long id, String title, int releaseYear, MediaType type, Integer duration,
                           int minimumAge, String posterUrl, BigDecimal rentalPricePerDay, List<GenreDto> genres) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.type = type;
        this.duration = duration;
        this.minimumAge = minimumAge;
        this.posterUrl = posterUrl;
        this.rentalPricePerDay = rentalPricePerDay;
        this.genres = genres;
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
}
