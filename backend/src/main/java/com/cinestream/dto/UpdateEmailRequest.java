package com.cinestream.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UpdateEmailRequest {

    @NotBlank(message = "Le nouvel email est requis")
    @Email(message = "Format d'email invalide")
    private String email;

    public UpdateEmailRequest() {
    }

    public UpdateEmailRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
