package com.cinestream.dto;

import java.util.ArrayList;
import java.util.List;

public class DirectorDetailsDto {
    private Long id;
    private String name;
    private String bio;
    private String photoUrl;
    private List<MediaSummaryDto> filmography = new ArrayList<>();

    public DirectorDetailsDto() {
    }

    public DirectorDetailsDto(Long id, String name, String bio, String photoUrl, List<MediaSummaryDto> filmography) {
        this.id = id;
        this.name = name;
        this.bio = bio;
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<MediaSummaryDto> getFilmography() {
        return filmography;
    }

    public void setFilmography(List<MediaSummaryDto> filmography) {
        this.filmography = filmography;
    }
}
