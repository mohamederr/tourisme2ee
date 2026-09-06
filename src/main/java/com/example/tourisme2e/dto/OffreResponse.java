package com.example.tourisme2e.dto;

import com.example.tourisme2e.entity.Pension;
import com.example.tourisme2e.entity.Segment;
import com.example.tourisme2e.entity.StatutOffre;
import com.example.tourisme2e.entity.TypeGroupe;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OffreResponse {
    private Long id;
    private String titre;
    private String description;
    private String descriptionCourte;
    private String descriptionLongue;
    private Segment segment;
    private TypeGroupe typeGroupe;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Integer capaciteMin;
    private Integer capaciteMax;
    private BigDecimal prixIndicatif;
    private Integer duree;
    private String photos;
    private String sitesTouristiques;
    private String activitesIncluses;
    private Long hotelId;
    private String hotelNom;
    private Integer niveauConfort;
    private Pension pension;
    private BigDecimal prixBase;
    private String servicesAdditionnels;
    private StatutOffre statut;
    private LocalDateTime dateCreation;
}
