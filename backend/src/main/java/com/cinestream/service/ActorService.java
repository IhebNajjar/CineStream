package com.cinestream.service;

import com.cinestream.dto.ActorDto;
import com.cinestream.repository.ActorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ActorService {

    private final ActorRepository actorRepository;

    public ActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public List<ActorDto> getAllActors() {
        return actorRepository.findAllByOrderByNameAsc().stream()
                .map(a -> new ActorDto(a.getId(), a.getName(), a.getBio(), a.getBirthDate(), a.getPhotoUrl()))
                .collect(Collectors.toList());
    }
}
