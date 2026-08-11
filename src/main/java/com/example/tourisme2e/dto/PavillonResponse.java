package com.example.tourisme2e.dto;

import com.example.tourisme2e.entity.StatutPavillon;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PavillonResponse {
    private Long id;
    private String nom;
    private Integer capaciteMin;
    private Integer capaciteMax;
    private StatutPavillon statut;
}
