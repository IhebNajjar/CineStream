package com.cinestream.service;

import com.cinestream.dto.*;
import com.cinestream.exception.ResourceNotFoundException;
import com.cinestream.model.*;
import com.cinestream.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final MediaRepository mediaRepository;
    private final GenreRepository genreRepository;
    private final ActorRepository actorRepository;
    private final DirectorRepository directorRepository;
    private final SeasonRepository seasonRepository;
    private final EpisodeRepository episodeRepository;
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final MediaService mediaService;

    public AdminService(MediaRepository mediaRepository, GenreRepository genreRepository,
                        ActorRepository actorRepository, DirectorRepository directorRepository,
                        SeasonRepository seasonRepository, EpisodeRepository episodeRepository,
                        RentalRepository rentalRepository, UserRepository userRepository,
                        MediaService mediaService) {
        this.mediaRepository = mediaRepository;
        this.genreRepository = genreRepository;
        this.actorRepository = actorRepository;
        this.directorRepository = directorRepository;
        this.seasonRepository = seasonRepository;
        this.episodeRepository = episodeRepository;
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
        this.mediaService = mediaService;
    }

    /**
     * US036: Admin Dashboard Stats
     */
    public DashboardStatsDto getDashboardStats() {
        long totalMedia = mediaRepository.count();
        long moviesCount = mediaRepository.countByType(MediaType.FILM);
        long seriesCount = mediaRepository.countByType(MediaType.SERIE);
        long actorsCount = actorRepository.count();
        long directorsCount = directorRepository.count();
        long genresCount = genreRepository.count();
        long activeRentalsCount = rentalRepository.countByStatus(RentalStatus.EN_COURS);
        BigDecimal totalRevenue = rentalRepository.sumTotalRevenue();
        long usersCount = userRepository.count();

        return new DashboardStatsDto(
                totalMedia, moviesCount, seriesCount, actorsCount, directorsCount,
                genresCount, activeRentalsCount, totalRevenue != null ? totalRevenue : BigDecimal.ZERO, usersCount
        );
    }

    /**
     * US032, US033, US037: Create Media with pricing, trailer, videoKey
     */
    @Transactional
    public MediaDetailsDto createMedia(MediaCreateUpdateDto dto) {
        Media media = new Media();
        applyMediaDto(media, dto);
        Media saved = mediaRepository.save(media);
        return mediaService.getMediaDetails(saved.getId());
    }

    /**
     * US032, US033, US037: Update Media
     */
    @Transactional
    public MediaDetailsDto updateMedia(Long id, MediaCreateUpdateDto dto) {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Média non trouvé avec l'id: " + id));
        applyMediaDto(media, dto);
        Media saved = mediaRepository.save(media);
        return mediaService.getMediaDetails(saved.getId());
    }

    /**
     * US032: Delete Media
     */
    @Transactional
    public void deleteMedia(Long id) {
        if (!mediaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Média non trouvé avec l'id: " + id);
        }
        mediaRepository.deleteById(id);
    }

    /**
     * US034: Manage Seasons & Episodes
     */
    @Transactional
    public SeasonDto addSeason(Long mediaId, SeasonDto dto) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Média non trouvé avec l'id: " + mediaId));

        Season season = new Season();
        season.setMedia(media);
        season.setSeasonNumber(dto.getSeasonNumber());
        season.setTitle(dto.getTitle());
        season.setReleaseYear(dto.getReleaseYear());

        Season saved = seasonRepository.save(season);
        return new SeasonDto(saved.getId(), saved.getSeasonNumber(), saved.getTitle(), saved.getReleaseYear(), List.of());
    }

    @Transactional
    public EpisodeDto addEpisode(Long seasonId, EpisodeDto dto) {
        Season season = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new ResourceNotFoundException("Saison non trouvée avec l'id: " + seasonId));

        Episode episode = new Episode();
        episode.setSeason(season);
        episode.setEpisodeNumber(dto.getEpisodeNumber());
        episode.setTitle(dto.getTitle());
        episode.setDescription(dto.getDescription());
        episode.setDuration(dto.getDuration());
        episode.setVideoKey(dto.getVideoKey());

        Episode saved = episodeRepository.save(episode);
        return new EpisodeDto(saved.getId(), saved.getEpisodeNumber(), saved.getTitle(), saved.getDescription(), saved.getDuration(), saved.getVideoKey());
    }

    @Transactional
    public void deleteSeason(Long seasonId) {
        seasonRepository.deleteById(seasonId);
    }

    @Transactional
    public void deleteEpisode(Long episodeId) {
        episodeRepository.deleteById(episodeId);
    }

    /**
     * US035: Manage Genres, Actors, Directors
     */
    @Transactional
    public GenreDto createGenre(GenreDto dto) {
        Genre genre = new Genre(null, dto.getName(), dto.getDescription());
        Genre saved = genreRepository.save(genre);
        return new GenreDto(saved.getId(), saved.getName(), saved.getDescription());
    }

    @Transactional
    public void deleteGenre(Long id) {
        genreRepository.deleteById(id);
    }

    @Transactional
    public ActorDto createActor(ActorDto dto) {
        Actor actor = new Actor(null, dto.getName(), dto.getBio(), dto.getBirthDate(), dto.getPhotoUrl());
        Actor saved = actorRepository.save(actor);
        return new ActorDto(saved.getId(), saved.getName(), saved.getBio(), saved.getBirthDate(), saved.getPhotoUrl());
    }

    @Transactional
    public void deleteActor(Long id) {
        actorRepository.deleteById(id);
    }

    @Transactional
    public DirectorDto createDirector(DirectorDto dto) {
        Director director = new Director(null, dto.getName(), dto.getBio(), dto.getPhotoUrl());
        Director saved = directorRepository.save(director);
        return new DirectorDto(saved.getId(), saved.getName(), saved.getBio(), saved.getPhotoUrl());
    }

    @Transactional
    public void deleteDirector(Long id) {
        directorRepository.deleteById(id);
    }

    private void applyMediaDto(Media media, MediaCreateUpdateDto dto) {
        media.setTitle(dto.getTitle().trim());
        media.setDescription(dto.getDescription());
        media.setReleaseYear(dto.getReleaseYear());
        media.setType(dto.getType());
        media.setDuration(dto.getDuration());
        media.setLanguage(dto.getLanguage().trim());
        media.setCountry(dto.getCountry().trim());
        media.setMinimumAge(dto.getMinimumAge());
        media.setPosterUrl(dto.getPosterUrl());
        media.setTrailerUrl(dto.getTrailerUrl());
        media.setVideoKey(dto.getVideoKey());
        media.setRentalPricePerDay(dto.getRentalPricePerDay());

        if (dto.getGenreIds() != null) {
            Set<Genre> genres = new HashSet<>(genreRepository.findAllById(dto.getGenreIds()));
            media.setGenres(genres);
        }

        if (dto.getDirectorIds() != null) {
            Set<Director> directors = new HashSet<>(directorRepository.findAllById(dto.getDirectorIds()));
            media.setDirectors(directors);
        }

        if (dto.getActors() != null) {
            media.getMediaActors().clear();
            for (MediaCreateUpdateDto.ActorRoleInputDto actorInput : dto.getActors()) {
                Actor actor = actorRepository.findById(actorInput.getActorId()).orElse(null);
                if (actor != null) {
                    MediaActor ma = new MediaActor(media, actor, actorInput.getCharacterName());
                    media.getMediaActors().add(ma);
                }
            }
        }
    }
}
