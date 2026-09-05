package com.example.tourisme2e.dto;

import com.example.tourisme2e.entity.CategorieSite;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreerGroupeOuvertRequest {

    @NotBlank
    private String titre;

    @NotNull
    private LocalDate dateDebut;

    @NotNull
    private LocalDate dateFin;

    @NotNull
    private CategorieSite categorieSitePrincipale;

    private String activitesIncluses;

    @Min(2)
    private Integer niveauConfort;

    private String message; // ex: "Besoin de personnes sérieuses, +45 ans de préférence"
}