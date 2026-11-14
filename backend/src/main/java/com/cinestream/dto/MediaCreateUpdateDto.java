package com.cinestream.dto;

import com.cinestream.model.MediaType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MediaCreateUpdateDto {

    @NotBlank(message = "Le titre du média est obligatoire")
    @Size(max = 255, message = "Le titre ne doit pas dépasser 255 caractères")
    private String title;

    private String description;

    @NotNull(message = "L'année de sortie est obligatoire")
    @Min(value = 1888, message = "L'année de sortie doit être supérieure à 1888")
    @Max(value = 2100, message = "L'année de sortie est invalide")
    private Integer releaseYear;

    @NotNull(message = "Le type de média (FILM ou SERIE) est obligatoire")
    private MediaType type;

    @Min(value = 1, message = "La durée doit être supérieure à 0 minute")
    private Integer duration;

    @NotBlank(message = "La langue est obligatoire")
    private String language;

    @NotBlank(message = "Le pays est obligatoire")
    private String country;

    @Min(value = 0, message = "L'âge minimum doit être positif ou nul")
    @Max(value = 21, message = "L'âge minimum ne peut pas dépasser 21")
    private int minimumAge = 0;

    private String posterUrl;
    private String trailerUrl;
    private String videoKey;

    @NotNull(message = "Le tarif journalier est obligatoire")
    @DecimalMin(value = "0.01", message = "Le tarif de location doit être strictement supérieur à 0")
    private BigDecimal rentalPricePerDay;

    private List<Long> genreIds = new ArrayList<>();
    private List<Long> directorIds = new ArrayList<>();
    private List<ActorRoleInputDto> actors = new ArrayList<>();

    public MediaCreateUpdateDto() {
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

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
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

    public List<Long> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Long> genreIds) {
        this.genreIds = genreIds;
    }

    public List<Long> getDirectorIds() {
        return directorIds;
    }

    public void setDirectorIds(List<Long> directorIds) {
        this.directorIds = directorIds;
    }

    public List<ActorRoleInputDto> getActors() {
        return actors;
    }

    public void setActors(List<ActorRoleInputDto> actors) {
        this.actors = actors;
    }

    public static class ActorRoleInputDto {
        @NotNull(message = "L'acteur est requis")
        private Long actorId;

        @NotBlank(message = "Le nom du personnage est requis")
        private String characterName;

        public ActorRoleInputDto() {
        }

        public ActorRoleInputDto(Long actorId, String characterName) {
            this.actorId = actorId;
            this.characterName = characterName;
        }

        public Long getActorId() {
            return actorId;
        }

        public void setActorId(Long actorId) {
            this.actorId = actorId;
        }

        public String getCharacterName() {
            return characterName;
        }

        public void setCharacterName(String characterName) {
            this.characterName = characterName;
        }
    }
}
