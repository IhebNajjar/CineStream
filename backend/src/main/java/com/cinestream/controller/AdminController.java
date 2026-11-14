package com.cinestream.controller;

import com.cinestream.dto.*;
import com.cinestream.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * US036: Admin Dashboard Stats
     */
    @GetMapping("/dashboard/stats")
    public ResponseEntity<DashboardStatsDto> getDashboardStats() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    /**
     * US032, US033, US037: Create Media
     */
    @PostMapping("/media")
    public ResponseEntity<MediaDetailsDto> createMedia(@Valid @RequestBody MediaCreateUpdateDto dto) {
        return new ResponseEntity<>(adminService.createMedia(dto), HttpStatus.CREATED);
    }

    /**
     * US032, US033, US037: Update Media
     */
    @PutMapping("/media/{id}")
    public ResponseEntity<MediaDetailsDto> updateMedia(@PathVariable Long id,
                                                       @Valid @RequestBody MediaCreateUpdateDto dto) {
        return ResponseEntity.ok(adminService.updateMedia(id, dto));
    }

    /**
     * US032: Delete Media
     */
    @DeleteMapping("/media/{id}")
    public ResponseEntity<Map<String, String>> deleteMedia(@PathVariable Long id) {
        adminService.deleteMedia(id);
        return ResponseEntity.ok(Map.of("message", "Média supprimé avec succès."));
    }

    /**
     * US034: Add Season to Media
     */
    @PostMapping("/media/{mediaId}/seasons")
    public ResponseEntity<SeasonDto> addSeason(@PathVariable Long mediaId,
                                               @Valid @RequestBody SeasonDto dto) {
        return new ResponseEntity<>(adminService.addSeason(mediaId, dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/seasons/{seasonId}")
    public ResponseEntity<Map<String, String>> deleteSeason(@PathVariable Long seasonId) {
        adminService.deleteSeason(seasonId);
        return ResponseEntity.ok(Map.of("message", "Saison supprimée."));
    }

    /**
     * US034: Add Episode to Season
     */
    @PostMapping("/seasons/{seasonId}/episodes")
    public ResponseEntity<EpisodeDto> addEpisode(@PathVariable Long seasonId,
                                                 @Valid @RequestBody EpisodeDto dto) {
        return new ResponseEntity<>(adminService.addEpisode(seasonId, dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/episodes/{episodeId}")
    public ResponseEntity<Map<String, String>> deleteEpisode(@PathVariable Long episodeId) {
        adminService.deleteEpisode(episodeId);
        return ResponseEntity.ok(Map.of("message", "Épisode supprimé."));
    }

    /**
     * US035: Manage Genres
     */
    @PostMapping("/genres")
    public ResponseEntity<GenreDto> createGenre(@RequestBody GenreDto dto) {
        return new ResponseEntity<>(adminService.createGenre(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/genres/{id}")
    public ResponseEntity<Map<String, String>> deleteGenre(@PathVariable Long id) {
        adminService.deleteGenre(id);
        return ResponseEntity.ok(Map.of("message", "Genre supprimé."));
    }

    /**
     * US035: Manage Actors
     */
    @PostMapping("/actors")
    public ResponseEntity<ActorDto> createActor(@RequestBody ActorDto dto) {
        return new ResponseEntity<>(adminService.createActor(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/actors/{id}")
    public ResponseEntity<Map<String, String>> deleteActor(@PathVariable Long id) {
        adminService.deleteActor(id);
        return ResponseEntity.ok(Map.of("message", "Acteur supprimé."));
    }

    /**
     * US035: Manage Directors
     */
    @PostMapping("/directors")
    public ResponseEntity<DirectorDto> createDirector(@RequestBody DirectorDto dto) {
        return new ResponseEntity<>(adminService.createDirector(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/directors/{id}")
    public ResponseEntity<Map<String, String>> deleteDirector(@PathVariable Long id) {
        adminService.deleteDirector(id);
        return ResponseEntity.ok(Map.of("message", "Réalisateur supprimé."));
    }
}
