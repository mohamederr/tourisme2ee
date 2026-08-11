package com.example.tourisme2e.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class DemandeDevisRequest {

    @NotNull
    private Long offreId;

    @NotNull
    private LocalDate dateSouhaitee;

    @Positive
    @NotNull
    private Integer nbParticipants;

    // Champs spécifiques SENIOR
    private String paysOrigine;
    private String besoinsSpecifiques;

    // Champs spécifiques MICE
    private Integer dureeJours;
    private List<String> equipementRequis;

    // Commun
    private String message;
}