package com.example.tourisme2e.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangerMotDePasseAdminRequest {
    @NotBlank
    private String motDePasse;
}
