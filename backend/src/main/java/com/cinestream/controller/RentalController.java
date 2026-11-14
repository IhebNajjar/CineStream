package com.cinestream.controller;

import com.cinestream.dto.RentalDto;
import com.cinestream.dto.RentalRequest;
import com.cinestream.dto.WatchLinkResponse;
import com.cinestream.model.User;
import com.cinestream.security.SecurityUtils;
import com.cinestream.service.RentalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;
    private final SecurityUtils securityUtils;

    public RentalController(RentalService rentalService, SecurityUtils securityUtils) {
        this.rentalService = rentalService;
        this.securityUtils = securityUtils;
    }

    @PostMapping
    public ResponseEntity<RentalDto> rentMedia(@Valid @RequestBody RentalRequest request) {
        User currentUser = securityUtils.getCurrentUserOrThrow();
        RentalDto created = rentalService.createRental(currentUser, request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/my")
    public ResponseEntity<List<RentalDto>> getMyRentals() {
        User currentUser = securityUtils.getCurrentUserOrThrow();
        return ResponseEntity.ok(rentalService.getUserRentals(currentUser));
    }

    @GetMapping("/{id}/watch-link")
    public ResponseEntity<WatchLinkResponse> getWatchLink(@PathVariable Long id) {
        User currentUser = securityUtils.getCurrentUserOrThrow();
        return ResponseEntity.ok(rentalService.getWatchLink(currentUser, id));
    }
}
