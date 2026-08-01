package com.example.tourisme2e.dto;

import com.example.tourisme2e.entity.TypeProfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank
    private String nom;

    @NotBlank
    private String prenom;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String motDePasse;

    private TypeProfil typeProfil;

    private String organisation;

    private String pays;
}
