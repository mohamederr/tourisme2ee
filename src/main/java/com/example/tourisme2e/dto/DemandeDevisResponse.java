package com.example.tourisme2e.dto;

import com.example.tourisme2e.entity.StatutDevis;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DemandeDevisResponse {
    private Long id;
    private Long utilisateurId;
    private Long offreId;
    private String offreTitre;
    private LocalDate dateSouhaitee;
    private Integer nbParticipants;
    private String besoinsSpecifiques;
    private String message;
    private StatutDevis statut;
    private LocalDateTime dateCreation;
}
