package com.example.tourisme2e.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "offre")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Offre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String descriptionCourte;

    @Column(columnDefinition = "TEXT")
    private String descriptionLongue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Segment segment;

    @Enumerated(EnumType.STRING)
    private TypeGroupe typeGroupe;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    private Integer capaciteMin;

    private Integer capaciteMax;

    private BigDecimal prixIndicatif;

    private Integer duree;

    private String photos;

    @Column(columnDefinition = "TEXT")
    private String sitesTouristiques;

    @Column(columnDefinition = "TEXT")
    private String activitesIncluses;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private HotelCentre hotel;

    private Integer niveauConfort;

    @Enumerated(EnumType.STRING)
    private Pension pension;

    private BigDecimal prixBase;

    @Column(columnDefinition = "TEXT")
    private String servicesAdditionnels;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutOffre statut;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
        if (this.statut == null) {
            this.statut = StatutOffre.BROUILLON;
        }
    }

    @OneToMany(mappedBy = "offre", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Disponibilite> disponibilites = new ArrayList<>();

    @OneToMany(mappedBy = "offre", cascade = CascadeType.ALL)
    private List<DemandeDevis> demandesDevis = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Offre)) return false;
        Offre offre = (Offre) o;
        return id != null && id.equals(offre.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
