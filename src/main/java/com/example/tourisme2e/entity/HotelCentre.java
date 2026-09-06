package com.example.tourisme2e.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "hotels_centres")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HotelCentre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private String type;

    @Enumerated(EnumType.STRING)
    private TypeHebergement typeHebergement;

    private String localisation;

    private Integer classement;

    @Column(nullable = false)
    private Integer capaciteLits;

    @Column(nullable = false)
    private BigDecimal tarifJourPersonne;

    private LocalDate disponibiliteDebut;

    private LocalDate disponibiliteFin;

    @Column(columnDefinition = "TEXT")
    private String equipements;

    private Boolean accessiblePmr;

    private String typeCuisine;

    private BigDecimal commissionPourcentage;

    private String conventionPdfUrl;

    @Column(columnDefinition = "TEXT")
    private String photosGalerie;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HotelCentre)) return false;
        HotelCentre that = (HotelCentre) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
