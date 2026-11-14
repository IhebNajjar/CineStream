package com.cinestream.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ActorDetailsDto {
    private Long id;
    private String name;
    private String bio;
    private LocalDate birthDate;
    private String photoUrl;
    private List<ActorRoleDto> filmography = new ArrayList<>();

    public ActorDetailsDto() {
    }

    public ActorDetailsDto(Long id, String name, String bio, LocalDate birthDate, String photoUrl, List<ActorRoleDto> filmography) {
        this.id = id;
        this.name = name;
        this.bio = bio;
        this.birthDate = birthDate;
        this.photoUrl = photoUrl;
        this.filmography = filmography != null ? filmography : new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<ActorRoleDto> getFilmography() {
        return filmography;
    }

    public void setFilmography(List<ActorRoleDto> filmography) {
        this.filmography = filmography;
    }

    public static class ActorRoleDto {
        private Long mediaId;
        private String mediaTitle;
        private String posterUrl;
        private int releaseYear;
        private String type;
        private String characterName;

        public ActorRoleDto() {
        }

        public ActorRoleDto(Long mediaId, String mediaTitle, String posterUrl, int releaseYear, String type, String characterName) {
            this.mediaId = mediaId;
            this.mediaTitle = mediaTitle;
            this.posterUrl = posterUrl;
            this.releaseYear = releaseYear;
            this.type = type;
            this.characterName = characterName;
        }

        public Long getMediaId() {
            return mediaId;
        }

        public void setMediaId(Long mediaId) {
            this.mediaId = mediaId;
        }

        public String getMediaTitle() {
            return mediaTitle;
        }

        public void setMediaTitle(String mediaTitle) {
            this.mediaTitle = mediaTitle;
        }

        public String getPosterUrl() {
            return posterUrl;
        }

        public void setPosterUrl(String posterUrl) {
            this.posterUrl = posterUrl;
        }

        public int getReleaseYear() {
            return releaseYear;
        }

        public void setReleaseYear(int releaseYear) {
            this.releaseYear = releaseYear;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCharacterName() {
            return characterName;
        }

        public void setCharacterName(String characterName) {
            this.characterName = characterName;
        }
    }
}
