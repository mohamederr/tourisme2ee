package com.example.tourisme2e.dto;

import com.example.tourisme2e.entity.Segment;
import com.example.tourisme2e.entity.StatutOffre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OffreRequest {

    @NotBlank
    private String titre;

    private String description;

    @NotNull
    private Segment segment;

    @Positive
    private BigDecimal prixIndicatif;

    @Positive
    private Integer duree;

    private String photos;

    @NotNull
    private StatutOffre statut;
}