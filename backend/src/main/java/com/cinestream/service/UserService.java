package com.cinestream.service;

import com.cinestream.dto.UpdateEmailRequest;
import com.cinestream.dto.UpdatePasswordRequest;
import com.cinestream.exception.ResourceNotFoundException;
import com.cinestream.model.User;
import com.cinestream.repository.UserRepository;
import com.cinestream.security.JwtTokenProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public Map<String, Object> updateEmail(User currentUser, UpdateEmailRequest request) {
        String newEmail = request.getEmail().trim().toLowerCase();
        if (newEmail.equalsIgnoreCase(currentUser.getEmail())) {
            return Map.of("message", "L'adresse email est identique.", "email", newEmail);
        }

        if (userRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("Cette adresse email est déjà utilisée.");
        }

        currentUser.setEmail(newEmail);
        userRepository.save(currentUser);

        // Generate a new token with updated email
        String newToken = tokenProvider.createToken(currentUser.getId(), currentUser.getEmail(), currentUser.getRole());
        return Map.of("message", "Adresse email mise à jour avec succès.", "token", newToken, "email", newEmail);
    }

    @Transactional
    public Map<String, String> updatePassword(User currentUser, UpdatePasswordRequest request) {
        if (!passwordEncoder.matches(request.getCurrentPassword(), currentUser.getPasswordHash())) {
            throw new BadCredentialsException("Le mot de passe actuel est incorrect.");
        }

        currentUser.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(currentUser);

        return Map.of("message", "Mot de passe modifié avec succès.");
    }
}
