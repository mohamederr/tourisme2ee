package com.example.tourisme2e.dto;

import com.example.tourisme2e.entity.CategorieSite;
import com.example.tourisme2e.entity.Sexe;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CreerGroupeOuvertRequest {

    @NotBlank
    private String titre;

    @NotNull
    private LocalDate dateDebut;

    @NotNull
    private LocalDate dateFin;

    // Catégorie principale pour rétrocompatibilité
    private CategorieSite categorieSitePrincipale;

    // Multi-select des sites touristiques (identifiants)
    private List<Long> siteTouristiqueIds = new ArrayList<>();

    private String activitesIncluses;

    @Min(2)
    private Integer niveauConfort;

    private String message; // ex: "Besoin de personnes sérieuses, +45 ans de préférence"

    // Informations du créateur pour son profil public en tant que 1er participant
    @NotNull(message = "L'âge du créateur est obligatoire")
    private Integer ageCréateur;

    @NotNull(message = "Le sexe du créateur est obligatoire")
    private Sexe sexeCréateur;

    @NotBlank(message = "Le pays du créateur est obligatoire")
    private String paysCréateur;
}