package com.example.tourisme2e.dto;

import com.example.tourisme2e.entity.Pension;
import com.example.tourisme2e.entity.Segment;
import com.example.tourisme2e.entity.StatutOffre;
import com.example.tourisme2e.entity.TypeGroupe;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class OffreRequest {

    @NotBlank
    private String titre;

    private String description;

    private String descriptionCourte;

    private String descriptionLongue;

    @NotNull
    private Segment segment;

    private TypeGroupe typeGroupe;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    @Min(10)
    private Integer capaciteMin;

    @Max(20)
    private Integer capaciteMax;

    @Positive
    private BigDecimal prixIndicatif;

    @Positive
    private Integer duree;

    private String photos;

    private String sitesTouristiques;

    private String activitesIncluses;

    private Long hotelId;

    @Min(2)
    @Max(5)
    private Integer niveauConfort;

    private Pension pension;

    @Positive
    private BigDecimal prixBase;

    private String servicesAdditionnels;

    @NotNull
    private StatutOffre statut;
}
