package com.cinestream.controller;

import com.cinestream.dto.AuthResponse;
import com.cinestream.dto.LoginRequest;
import com.cinestream.dto.RegisterRequest;
import com.cinestream.security.SecurityUtils;
import com.cinestream.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final SecurityUtils securityUtils;

    public AuthController(AuthService authService, SecurityUtils securityUtils) {
        this.authService = authService;
        this.securityUtils = securityUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse> getCurrentUser() {
        String email = securityUtils.getCurrentUserEmail()
                .orElseThrow(() -> new org.springframework.security.access.AccessDeniedException("Non connecté"));
        return ResponseEntity.ok(authService.getCurrentUserResponse(email));
    }
}
