package com.cinestream.controller;

import com.cinestream.dto.DirectorDetailsDto;
import com.cinestream.dto.DirectorDto;
import com.cinestream.service.DirectorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/directors")
public class DirectorController {

    private final DirectorService directorService;

    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @GetMapping
    public ResponseEntity<List<DirectorDto>> getAllDirectors() {
        return ResponseEntity.ok(directorService.getAllDirectors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DirectorDetailsDto> getDirectorDetails(@PathVariable Long id) {
        return ResponseEntity.ok(directorService.getDirectorDetails(id));
    }
}
