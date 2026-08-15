package com.example.tourisme2e.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DashboardResponse {
    private long devisEnAttenteSenior;
    private long devisEnAttenteMice;
    private Double tauxRemplissagePavillon1; // en pourcentage, null si pas de données
    private List<SejourConfirmeResponse> prochainsSejoursConfirmes;
}
