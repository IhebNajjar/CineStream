package com.cinestream.security;

import com.cinestream.model.User;
import com.cinestream.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityUtils {

    private final UserRepository userRepository;

    public SecurityUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<String> getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.empty();
        }
        return Optional.ofNullable(authentication.getName());
    }

    public Optional<User> getCurrentUser() {
        return getCurrentUserEmail().flatMap(userRepository::findByEmail);
    }

    public User getCurrentUserOrThrow() {
        return getCurrentUser()
                .orElseThrow(() -> new org.springframework.security.access.AccessDeniedException("Accès non authentifié. Veuillez vous connecter."));
    }
}
