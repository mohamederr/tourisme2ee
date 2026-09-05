package com.example.tourisme2e.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ReductionResponse {
    private BigDecimal prixBaseUnitaire;
    private Integer nbParticipants;
    private BigDecimal tauxReductionVolume;   // ex: 0.20 pour 20%
    private BigDecimal tauxReductionDelai;    // ex: 0.30 pour 30%
    private BigDecimal tauxReductionTotal;    // somme cumulée
    private BigDecimal prixUnitaireFinal;
    private BigDecimal prixTotalFinal;
    private BigDecimal economieTotale;
}