package com.example.tourisme2e.dto;

import com.example.tourisme2e.entity.CategorieSite;
import com.example.tourisme2e.entity.Pension;
import com.example.tourisme2e.entity.TypeGroupeFerme;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CreerGroupeFermeRequest {

    @NotBlank
    private String titre;

    private String descriptionCourte;
    private String descriptionLongue;

    @NotNull
    private TypeGroupeFerme typeGroupeFerme;

    @NotNull
    private LocalDate dateDebut;

    @NotNull
    private LocalDate dateFin;

    @Positive
    @NotNull
    private Integer nbParticipants; // fixe et connu d'avance pour un groupe fermé

    // Catégorie principale (optionnel si siteTouristiqueIds est renseigné)
    private CategorieSite categorieSitePrincipale;

    // Multi-select des sites touristiques (identifiants)
    private List<Long> siteTouristiqueIds = new ArrayList<>();

    private String activitesIncluses;

    private Long hotelId;

    @Min(2) @Max(5)
    private Integer niveauConfort;

    private Pension pension;

    private String servicesAdditionnels;

    private String demandesSpeciales;
}