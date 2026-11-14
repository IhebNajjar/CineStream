package com.cinestream.controller;

import com.cinestream.dto.UpdateEmailRequest;
import com.cinestream.dto.UpdatePasswordRequest;
import com.cinestream.model.User;
import com.cinestream.security.SecurityUtils;
import com.cinestream.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final SecurityUtils securityUtils;

    public UserController(UserService userService, SecurityUtils securityUtils) {
        this.userService = userService;
        this.securityUtils = securityUtils;
    }

    @PutMapping("/email")
    public ResponseEntity<Map<String, Object>> updateEmail(@Valid @RequestBody UpdateEmailRequest request) {
        User currentUser = securityUtils.getCurrentUserOrThrow();
        return ResponseEntity.ok(userService.updateEmail(currentUser, request));
    }

    @PutMapping("/password")
    public ResponseEntity<Map<String, String>> updatePassword(@Valid @RequestBody UpdatePasswordRequest request) {
        User currentUser = securityUtils.getCurrentUserOrThrow();
        return ResponseEntity.ok(userService.updatePassword(currentUser, request));
    }
}
