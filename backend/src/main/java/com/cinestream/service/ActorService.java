package com.cinestream.service;

import com.cinestream.dto.ActorDetailsDto;
import com.cinestream.dto.ActorDto;
import com.cinestream.exception.ResourceNotFoundException;
import com.cinestream.model.Actor;
import com.cinestream.model.Media;
import com.cinestream.model.MediaActor;
import com.cinestream.repository.ActorRepository;
import com.cinestream.repository.MediaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ActorService {

    private final ActorRepository actorRepository;
    private final MediaRepository mediaRepository;

    public ActorService(ActorRepository actorRepository, MediaRepository mediaRepository) {
        this.actorRepository = actorRepository;
        this.mediaRepository = mediaRepository;
    }

    public List<ActorDto> getAllActors() {
        return actorRepository.findAllByOrderByNameAsc().stream()
                .map(a -> new ActorDto(a.getId(), a.getName(), a.getBio(), a.getBirthDate(), a.getPhotoUrl()))
                .collect(Collectors.toList());
    }

    /**
     * US013: Get actor details and full filmography with character names.
     */
    public ActorDetailsDto getActorDetails(Long id) {
        Actor actor = actorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Acteur introuvable avec l'id: " + id));

        List<Media> mediaList = mediaRepository.findByActorId(id);
        List<ActorDetailsDto.ActorRoleDto> roles = new ArrayList<>();

        for (Media m : mediaList) {
            String charName = m.getMediaActors().stream()
                    .filter(ma -> ma.getActor().getId().equals(id))
                    .map(MediaActor::getCharacterName)
                    .findFirst()
                    .orElse("Rôle principal");

            roles.add(new ActorDetailsDto.ActorRoleDto(
                    m.getId(),
                    m.getTitle(),
                    m.getPosterUrl(),
                    m.getReleaseYear(),
                    m.getType().name(),
                    charName
            ));
        }

        return new ActorDetailsDto(
                actor.getId(),
                actor.getName(),
                actor.getBio(),
                actor.getBirthDate(),
                actor.getPhotoUrl(),
                roles
        );
    }
}
