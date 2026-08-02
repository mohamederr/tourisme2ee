package com.example.tourisme2e.dto;

import com.example.tourisme2e.entity.Segment;
import com.example.tourisme2e.entity.StatutOffre;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OffreResponse {
    private Long id;
    private String titre;
    private String description;
    private Segment segment;
    private BigDecimal prixIndicatif;
    private Integer duree;
    private String photos;
    private StatutOffre statut;
    private LocalDateTime dateCreation;

}
