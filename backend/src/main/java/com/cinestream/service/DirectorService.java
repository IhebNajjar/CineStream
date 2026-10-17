package com.cinestream.service;

import com.cinestream.dto.DirectorDto;
import com.cinestream.repository.DirectorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DirectorService {

    private final DirectorRepository directorRepository;

    public DirectorService(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }

    public List<DirectorDto> getAllDirectors() {
        return directorRepository.findAllByOrderByNameAsc().stream()
                .map(d -> new DirectorDto(d.getId(), d.getName(), d.getBio(), d.getPhotoUrl()))
                .collect(Collectors.toList());
    }
}
