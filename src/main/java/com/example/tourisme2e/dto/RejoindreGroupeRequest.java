package com.example.tourisme2e.dto;

import com.example.tourisme2e.entity.Sexe;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RejoindreGroupeRequest {

    @NotBlank
    private String nom;

    @NotBlank
    private String prenom;

    @NotBlank
    @Email
    private String email;

    private String telephone;

    @Positive
    @NotNull
    private Integer age;

    @NotNull
    private Sexe sexe;

    @NotBlank
    private String pays;

    private String message;
}