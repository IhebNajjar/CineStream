package com.cinestream.service;

import com.cinestream.dto.AuthResponse;
import com.cinestream.dto.LoginRequest;
import com.cinestream.dto.RegisterRequest;
import com.cinestream.exception.ResourceNotFoundException;
import com.cinestream.model.Role;
import com.cinestream.model.User;
import com.cinestream.model.Wallet;
import com.cinestream.repository.UserRepository;
import com.cinestream.repository.WalletRepository;
import com.cinestream.security.JwtTokenProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthService(UserRepository userRepository, WalletRepository walletRepository,
                       PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail().trim().toLowerCase())) {
            throw new IllegalArgumentException("Un compte existe déjà avec cette adresse email.");
        }

        User user = new User();
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        User savedUser = userRepository.save(user);

        // US001: Automatic wallet initialization with 0.00 €
        Wallet wallet = new Wallet(savedUser, BigDecimal.ZERO);
        walletRepository.save(wallet);

        String token = tokenProvider.createToken(savedUser.getId(), savedUser.getEmail(), savedUser.getRole());
        return new AuthResponse(token, savedUser.getId(), savedUser.getEmail(), savedUser.getRole());
    }

    public AuthResponse login(LoginRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Email ou mot de passe incorrect."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Email ou mot de passe incorrect.");
        }

        String token = tokenProvider.createToken(user.getId(), user.getEmail(), user.getRole());
        return new AuthResponse(token, user.getId(), user.getEmail(), user.getRole());
    }

    public AuthResponse getCurrentUserResponse(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé: " + email));
        String token = tokenProvider.createToken(user.getId(), user.getEmail(), user.getRole());
        return new AuthResponse(token, user.getId(), user.getEmail(), user.getRole());
    }
}
