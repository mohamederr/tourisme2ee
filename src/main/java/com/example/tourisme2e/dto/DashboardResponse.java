package com.example.tourisme2e.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class DashboardResponse {
    private long demandesEnAttenteValidation;
    private long groupesActifsTotal;
    private long groupesActifsFermes;
    private long groupesActifsOuverts;
    private BigDecimal revenusDuMois;
    private double tauxRemplissageMoyen;
    private long nouveauxUtilisateursCeMois;
    private List<String> alertesPrioritaires;
}