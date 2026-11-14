package com.cinestream.controller;

import com.cinestream.dto.FavoriteDto;
import com.cinestream.model.User;
import com.cinestream.security.SecurityUtils;
import com.cinestream.service.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final SecurityUtils securityUtils;

    public FavoriteController(FavoriteService favoriteService, SecurityUtils securityUtils) {
        this.favoriteService = favoriteService;
        this.securityUtils = securityUtils;
    }

    @GetMapping
    public ResponseEntity<List<FavoriteDto>> getMyFavorites() {
        User user = securityUtils.getCurrentUserOrThrow();
        return ResponseEntity.ok(favoriteService.getUserFavorites(user));
    }

    @PostMapping("/toggle/{mediaId}")
    public ResponseEntity<Map<String, Object>> toggleFavorite(@PathVariable Long mediaId) {
        User user = securityUtils.getCurrentUserOrThrow();
        return ResponseEntity.ok(favoriteService.toggleFavorite(user, mediaId));
    }

    @GetMapping("/check/{mediaId}")
    public ResponseEntity<Map<String, Boolean>> isFavorite(@PathVariable Long mediaId) {
        return securityUtils.getCurrentUser()
                .map(u -> ResponseEntity.ok(Map.of("isFavorite", favoriteService.isFavorite(u, mediaId))))
                .orElse(ResponseEntity.ok(Map.of("isFavorite", false)));
    }
}
