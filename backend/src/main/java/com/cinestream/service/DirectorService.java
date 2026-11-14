package com.cinestream.service;

import com.cinestream.dto.DirectorDetailsDto;
import com.cinestream.dto.DirectorDto;
import com.cinestream.dto.MediaSummaryDto;
import com.cinestream.exception.ResourceNotFoundException;
import com.cinestream.model.Director;
import com.cinestream.model.Media;
import com.cinestream.repository.DirectorRepository;
import com.cinestream.repository.MediaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DirectorService {

    private final DirectorRepository directorRepository;
    private final MediaRepository mediaRepository;
    private final MediaService mediaService;

    public DirectorService(DirectorRepository directorRepository, MediaRepository mediaRepository, MediaService mediaService) {
        this.directorRepository = directorRepository;
        this.mediaRepository = mediaRepository;
        this.mediaService = mediaService;
    }

    public List<DirectorDto> getAllDirectors() {
        return directorRepository.findAllByOrderByNameAsc().stream()
                .map(d -> new DirectorDto(d.getId(), d.getName(), d.getBio(), d.getPhotoUrl()))
                .collect(Collectors.toList());
    }

    /**
     * US014: Get director details and full filmography.
     */
    public DirectorDetailsDto getDirectorDetails(Long id) {
        Director director = directorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Réalisateur introuvable avec l'id: " + id));

        List<Media> mediaList = mediaRepository.findByDirectorId(id);
        List<MediaSummaryDto> filmography = mediaList.stream()
                .map(mediaService::mapToSummaryDto)
                .collect(Collectors.toList());

        return new DirectorDetailsDto(
                director.getId(),
                director.getName(),
                director.getBio(),
                director.getPhotoUrl(),
                filmography
        );
    }
}
