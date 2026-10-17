package com.cinestream.controller;

import com.cinestream.dto.MediaDetailsDto;
import com.cinestream.dto.MediaSummaryDto;
import com.cinestream.dto.PagedResponse;
import com.cinestream.model.MediaType;
import com.cinestream.service.MediaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/media")
@CrossOrigin(origins = "*")
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    /**
     * US008, US009, US011:
     * Endpoint GET /api/media with pagination (12 by default), title search, and combined filters.
     */
    @GetMapping
    public ResponseEntity<PagedResponse<MediaSummaryDto>> getMediaList(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) MediaType type,
            @RequestParam(required = false) Long genre,
            @RequestParam(required = false) Long actor,
            @RequestParam(required = false) Long director,
            @RequestParam(required = false) Integer year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        PagedResponse<MediaSummaryDto> response = mediaService.getMediaList(
                q, type, genre, actor, director, year, page, size
        );

        return ResponseEntity.ok(response);
    }

    /**
     * US010, US029:
     * Endpoint GET /api/media/{id} returning detailed media information or 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MediaDetailsDto> getMediaDetails(@PathVariable Long id) {
        MediaDetailsDto details = mediaService.getMediaDetails(id);
        return ResponseEntity.ok(details);
    }
}
