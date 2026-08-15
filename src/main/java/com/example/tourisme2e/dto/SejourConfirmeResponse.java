package com.example.tourisme2e.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class SejourConfirmeResponse {
    private Long reservationId;
    private String offreTitre;
    private String pavillonNom;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Integer nbParticipants;
}