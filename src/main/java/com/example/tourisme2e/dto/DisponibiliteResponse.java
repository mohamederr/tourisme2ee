package com.example.tourisme2e.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DisponibiliteResponse {
    private Long id;
    private Long offreId;
    private Long pavillonId;
    private String pavillonNom;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Integer placesTotales;
    private Integer placesRestantes;
}