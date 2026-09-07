package com.example.tourisme2e.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "sites_touristiques")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SiteTouristique {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategorieSite categorie;

    @Column(columnDefinition = "TEXT")
    private String descriptionDetaillee;

    private Double latitude;

    private Double longitude;

    private String dureeVisiteRecommandee;

    private String meilleurePeriode;

    @Enumerated(EnumType.STRING)
    private NiveauDifficulte niveauDifficulte;

    @Column(columnDefinition = "TEXT")
    private String activitesPossibles;

    private Boolean accessiblePmr;

    @Column(columnDefinition = "TEXT")
    private String galeriePhotos;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SiteTouristique)) return false;
        SiteTouristique that = (SiteTouristique) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
