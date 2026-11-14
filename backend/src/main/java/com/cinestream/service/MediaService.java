package com.cinestream.service;

import com.cinestream.dto.*;
import com.cinestream.exception.ResourceNotFoundException;
import com.cinestream.model.Episode;
import com.cinestream.model.Media;
import com.cinestream.model.MediaType;
import com.cinestream.model.Season;
import com.cinestream.repository.MediaRepository;
import com.cinestream.repository.ReviewRepository;
import com.cinestream.repository.SeasonRepository;
import com.cinestream.specification.MediaSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MediaService {

    private final MediaRepository mediaRepository;
    private final SeasonRepository seasonRepository;
    private final ReviewRepository reviewRepository;

    public MediaService(MediaRepository mediaRepository, SeasonRepository seasonRepository, ReviewRepository reviewRepository) {
        this.mediaRepository = mediaRepository;
        this.seasonRepository = seasonRepository;
        this.reviewRepository = reviewRepository;
    }

    public PagedResponse<MediaSummaryDto> getMediaList(
            String search,
            MediaType type,
            Long genreId,
            Long actorId,
            Long directorId,
            Integer year,
            String sortBy,
            String sortDir,
            int page,
            int size) {

        int pageNumber = Math.max(0, page);
        int pageSize = (size <= 0) ? 12 : size;

        // US015: Sort by title, releaseYear, rating, etc.
        Sort sort;
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        if ("year".equalsIgnoreCase(sortBy) || "releaseYear".equalsIgnoreCase(sortBy)) {
            sort = Sort.by(direction, "releaseYear");
        } else if ("price".equalsIgnoreCase(sortBy) || "rentalPricePerDay".equalsIgnoreCase(sortBy)) {
            sort = Sort.by(direction, "rentalPricePerDay");
        } else if ("title".equalsIgnoreCase(sortBy)) {
            sort = Sort.by(direction, "title");
        } else {
            sort = Sort.by(Sort.Direction.ASC, "id");
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Specification<Media> spec = MediaSpecification.filterMedia(search, type, genreId, actorId, directorId, year);

        Page<Media> mediaPage = mediaRepository.findAll(spec, pageable);

        // Fetch ratings map for efficient batch display
        Map<Long, Double> ratingsMap = new HashMap<>();
        Map<Long, Long> countsMap = new HashMap<>();
        try {
            List<Object[]> ratings = reviewRepository.findAverageRatingsAndCountsGrouped();
            for (Object[] row : ratings) {
                Long mediaId = (Long) row[0];
                Double avg = (Double) row[1];
                Long count = (Long) row[2];
                if (mediaId != null && avg != null) {
                    ratingsMap.put(mediaId, Math.round(avg * 10.0) / 10.0);
                    countsMap.put(mediaId, count);
                }
            }
        } catch (Exception ignored) {
        }

        List<MediaSummaryDto> content = mediaPage.getContent().stream()
                .map(m -> {
                    MediaSummaryDto dto = mapToSummaryDto(m);
                    Double avg = ratingsMap.get(m.getId());
                    dto.setRatingAverage(avg != null ? avg : 8.8); // Default fallback rating if not yet reviewed
                    dto.setRatingCount(countsMap.getOrDefault(m.getId(), 0L));
                    return dto;
                })
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

        MediaDetailsDto dto = mapToDetailsDto(media);

        // US012: Fetch seasons and episodes for series
        if (media.getType() == MediaType.SERIE) {
            List<Season> seasons = seasonRepository.findByMediaIdOrderBySeasonNumberAsc(media.getId());
            List<SeasonDto> seasonDtos = seasons.stream()
                    .map(this::mapToSeasonDto)
                    .collect(Collectors.toList());
            dto.setSeasons(seasonDtos);
        }

        // Ratings & review count
        Double avg = reviewRepository.calculateAverageRatingForMedia(media.getId());
        long count = reviewRepository.countByMediaId(media.getId());
        dto.setRatingAverage(avg != null ? Math.round(avg * 10.0) / 10.0 : 8.8);
        dto.setReviewsCount(count);

        return dto;
    }

    public MediaSummaryDto mapToSummaryDto(Media media) {
        List<GenreDto> genreDtos = media.getGenres().stream()
                .map(g -> new GenreDto(g.getId(), g.getName(), g.getDescription()))
                .collect(Collectors.toList());

        MediaSummaryDto dto = new MediaSummaryDto(
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
        dto.setRatingAverage(8.8);
        dto.setVideoKey(media.getVideoKey());
        return dto;
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

        MediaDetailsDto dto = new MediaDetailsDto();
        dto.setId(media.getId());
        dto.setTitle(media.getTitle());
        dto.setDescription(media.getDescription());
        dto.setReleaseYear(media.getReleaseYear());
        dto.setType(media.getType());
        dto.setDuration(media.getDuration());
        dto.setLanguage(media.getLanguage());
        dto.setCountry(media.getCountry());
        dto.setMinimumAge(media.getMinimumAge());
        dto.setPosterUrl(media.getPosterUrl());
        dto.setTrailerUrl(media.getTrailerUrl());
        dto.setVideoKey(media.getVideoKey());
        dto.setRentalPricePerDay(media.getRentalPricePerDay());
        dto.setGenres(genreDtos);
        dto.setDirectors(directorDtos);
        dto.setActors(actorDtos);
        return dto;
    }

    private SeasonDto mapToSeasonDto(Season season) {
        List<EpisodeDto> episodeDtos = season.getEpisodes().stream()
                .map(e -> new EpisodeDto(
                        e.getId(),
                        e.getEpisodeNumber(),
                        e.getTitle(),
                        e.getDescription(),
                        e.getDuration(),
                        e.getVideoKey()
                ))
                .collect(Collectors.toList());

        return new SeasonDto(
                season.getId(),
                season.getSeasonNumber(),
                season.getTitle(),
                season.getReleaseYear(),
                episodeDtos
        );
    }
}
