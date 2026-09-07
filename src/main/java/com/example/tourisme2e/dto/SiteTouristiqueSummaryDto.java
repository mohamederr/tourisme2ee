package com.example.tourisme2e.dto;

import com.example.tourisme2e.entity.CategorieSite;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SiteTouristiqueSummaryDto {
    private Long id;
    private String nom;
    private CategorieSite categorie;
}
