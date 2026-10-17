package com.cinestream.service;

import com.cinestream.dto.*;
import com.cinestream.exception.ResourceNotFoundException;
import com.cinestream.model.Media;
import com.cinestream.model.MediaType;
import com.cinestream.repository.MediaRepository;
import com.cinestream.specification.MediaSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MediaService {

    private final MediaRepository mediaRepository;

    public MediaService(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    public PagedResponse<MediaSummaryDto> getMediaList(
            String search,
            MediaType type,
            Long genreId,
            Long actorId,
            Long directorId,
            Integer year,
            int page,
            int size) {

        // Validate and enforce default pagination parameters
        int pageNumber = Math.max(0, page);
        int pageSize = (size <= 0) ? 12 : size;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        Specification<Media> spec = MediaSpecification.filterMedia(search, type, genreId, actorId, directorId, year);

        Page<Media> mediaPage = mediaRepository.findAll(spec, pageable);

        List<MediaSummaryDto> content = mediaPage.getContent().stream()
                .map(this::mapToSummaryDto)
                .collect(Collectors.toList());

        return new PagedResponse<>(
                content,
                mediaPage.getNumber(),
                mediaPage.getSize(),
                mediaPage.getTotalElements(),
                mediaPage.getTotalPages(),
                mediaPage.isLast()
        );
    }

    public MediaDetailsDto getMediaDetails(Long id) {
        if (id == null || id <= 0) {
            throw new ResourceNotFoundException("Titre introuvable");
        }

        Media media = mediaRepository.findByIdWithGenresAndDirectors(id)
                .orElseThrow(() -> new ResourceNotFoundException("Titre introuvable"));

        return mapToDetailsDto(media);
    }

    private MediaSummaryDto mapToSummaryDto(Media media) {
        List<GenreDto> genreDtos = media.getGenres().stream()
                .map(g -> new GenreDto(g.getId(), g.getName(), g.getDescription()))
                .collect(Collectors.toList());

        return new MediaSummaryDto(
                media.getId(),
                media.getTitle(),
                media.getReleaseYear(),
                media.getType(),
                media.getDuration(),
                media.getMinimumAge(),
                media.getPosterUrl(),
                media.getRentalPricePerDay(),
                genreDtos
        );
    }

    private MediaDetailsDto mapToDetailsDto(Media media) {
        List<GenreDto> genreDtos = media.getGenres().stream()
                .map(g -> new GenreDto(g.getId(), g.getName(), g.getDescription()))
                .collect(Collectors.toList());

        List<DirectorDto> directorDtos = media.getDirectors().stream()
                .map(d -> new DirectorDto(d.getId(), d.getName(), d.getBio(), d.getPhotoUrl()))
                .collect(Collectors.toList());

        List<MediaActorDto> actorDtos = media.getMediaActors().stream()
                .map(ma -> new MediaActorDto(
                        ma.getActor().getId(),
                        ma.getActor().getName(),
                        ma.getActor().getPhotoUrl(),
                        ma.getCharacterName()
                ))
                .collect(Collectors.toList());

        return new MediaDetailsDto(
                media.getId(),
                media.getTitle(),
                media.getDescription(),
                media.getReleaseYear(),
                media.getType(),
                media.getDuration(),
                media.getLanguage(),
                media.getCountry(),
                media.getMinimumAge(),
                media.getPosterUrl(),
                media.getTrailerUrl(),
                media.getRentalPricePerDay(),
                genreDtos,
                directorDtos,
                actorDtos
        );
    }
}
