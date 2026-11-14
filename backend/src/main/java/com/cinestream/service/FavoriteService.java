package com.cinestream.service;

import com.cinestream.dto.FavoriteDto;
import com.cinestream.dto.MediaSummaryDto;
import com.cinestream.exception.ResourceNotFoundException;
import com.cinestream.model.Favorite;
import com.cinestream.model.Media;
import com.cinestream.model.User;
import com.cinestream.repository.FavoriteRepository;
import com.cinestream.repository.MediaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MediaRepository mediaRepository;
    private final MediaService mediaService;

    public FavoriteService(FavoriteRepository favoriteRepository, MediaRepository mediaRepository, MediaService mediaService) {
        this.favoriteRepository = favoriteRepository;
        this.mediaRepository = mediaRepository;
        this.mediaService = mediaService;
    }

    @Transactional
    public Map<String, Object> toggleFavorite(User user, Long mediaId) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Média non trouvé avec l'id: " + mediaId));

        return favoriteRepository.findByUserIdAndMediaId(user.getId(), mediaId)
                .map(fav -> {
                    favoriteRepository.delete(fav);
                    return Map.<String, Object>of("isFavorite", false, "message", "Retiré de vos favoris.");
                })
                .orElseGet(() -> {
                    Favorite fav = new Favorite(user, media);
                    favoriteRepository.save(fav);
                    return Map.<String, Object>of("isFavorite", true, "message", "Ajouté à vos favoris.");
                });
    }

    public boolean isFavorite(User user, Long mediaId) {
        return favoriteRepository.existsByUserIdAndMediaId(user.getId(), mediaId);
    }

    @Transactional(readOnly = true)
    public List<FavoriteDto> getUserFavorites(User user) {
        return favoriteRepository.findByUserIdOrderByAddedAtDesc(user.getId())
                .stream()
                .map(f -> {
                    MediaSummaryDto summary = mediaService.mapToSummaryDto(f.getMedia());
                    return new FavoriteDto(f.getId(), user.getId(), f.getMedia().getId(), summary, f.getAddedAt());
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeFavorite(User user, Long mediaId) {
        favoriteRepository.findByUserIdAndMediaId(user.getId(), mediaId)
                .ifPresent(favoriteRepository::delete);
    }
}
