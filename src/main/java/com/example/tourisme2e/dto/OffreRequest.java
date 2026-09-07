package com.example.tourisme2e.dto;

import com.example.tourisme2e.entity.Pension;
import com.example.tourisme2e.entity.Segment;
import com.example.tourisme2e.entity.StatutOffre;
import com.example.tourisme2e.entity.TypeGroupe;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OffreRequest {

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    private String description;

    private String descriptionCourte;

    private String descriptionLongue;

    private Segment segment;

    private TypeGroupe typeGroupe;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    @Min(value = 10, message = "La capacité minimale doit être d'au moins 10")
    @Max(value = 20, message = "La capacité minimale ne peut pas dépasser 20")
    private Integer capaciteMin = 10;

    @Min(value = 10, message = "La capacité maximale doit être d'au moins 10")
    @Max(value = 20, message = "La capacité maximale ne peut pas dépasser 20")
    private Integer capaciteMax = 20;

    private BigDecimal prixIndicatif;

    private Integer duree;

    private String photos;

    // Multi-select : liste des identifiants des sites touristiques sélectionnés
    private List<Long> siteTouristiqueIds = new ArrayList<>();

    // Rétrocompatibilité textuelle
    private String sitesTouristiques;

    private String activitesIncluses;

    private Long hotelId;

    @JsonProperty("hotelCentreId")
    public void setHotelCentreId(Long hotelCentreId) {
        if (this.hotelId == null) {
            this.hotelId = hotelCentreId;
        }
    }

    public Long getHotelCentreId() {
        return this.hotelId;
    }

    @JsonProperty("sitesTouristiquesIds")
    public void setSitesTouristiquesIds(List<Long> ids) {
        if (ids != null) {
            this.siteTouristiqueIds = ids;
        }
    }

    @Min(value = 2, message = "Le niveau de confort doit être au minimum de 2 étoiles")
    @Max(value = 5, message = "Le niveau de confort ne peut pas dépasser 5 étoiles")
    private Integer niveauConfort;

    private Pension pension;

    @Positive(message = "Le prix de base doit être supérieur à 0")
    private BigDecimal prixBase;

    private String servicesAdditionnels;

    private StatutOffre statut;
}
