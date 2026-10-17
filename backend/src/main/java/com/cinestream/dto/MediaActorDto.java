package com.cinestream.dto;

public class MediaActorDto {
    private Long actorId;
    private String name;
    private String photoUrl;
    private String characterName;

    public MediaActorDto() {
    }

    public MediaActorDto(Long actorId, String name, String photoUrl, String characterName) {
        this.actorId = actorId;
        this.name = name;
        this.photoUrl = photoUrl;
        this.characterName = characterName;
    }

    public Long getActorId() {
        return actorId;
    }

    public void setActorId(Long actorId) {
        this.actorId = actorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }
}
