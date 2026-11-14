package com.cinestream.controller;

import com.cinestream.dto.ActorDetailsDto;
import com.cinestream.dto.ActorDto;
import com.cinestream.service.ActorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actors")
public class ActorController {

    private final ActorService actorService;

    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    @GetMapping
    public ResponseEntity<List<ActorDto>> getAllActors() {
        return ResponseEntity.ok(actorService.getAllActors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActorDetailsDto> getActorDetails(@PathVariable Long id) {
        return ResponseEntity.ok(actorService.getActorDetails(id));
    }
}
