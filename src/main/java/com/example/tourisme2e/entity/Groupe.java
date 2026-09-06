package com.example.tourisme2e.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "groupes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Groupe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String descriptionCourte;

    @Column(columnDefinition = "TEXT")
    private String descriptionLongue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeGroupe typeGroupe; // FERME ou OUVERT

    // Uniquement pertinent si typeGroupe = FERME
    @Enumerated(EnumType.STRING)
    private TypeGroupeFerme typeGroupeFerme; // ASSOCIATION / ENTREPRISE / FAMILLE

    @Column(nullable = false)
    private LocalDate dateDebut;

    @Column(nullable = false)
    private LocalDate dateFin;

    @Column(nullable = false)
    private Integer capaciteMin; // fixe pour FERME, 10 par défaut pour OUVERT

    @Column(nullable = false)
    private Integer capaciteMax; // 20 max pour OUVERT

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategorieSite categorieSitePrincipale; // simplifié ici ; multi-select sites via table de liaison plus tard

    @Column(columnDefinition = "TEXT")
    private String activitesIncluses;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private HotelCentre hotel;

    private Integer niveauConfort; // 2 à 5 étoiles

    @Enumerated(EnumType.STRING)
    private Pension pension;

    @Column(nullable = false)
    private BigDecimal prixBase; // MAD/personne

    @Column(columnDefinition = "TEXT")
    private String servicesAdditionnels;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutGroupe statut;

    // Le responsable pour un groupe FERME, ou le créateur pour un groupe OUVERT
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createur_id", nullable = false)
    private Utilisateur createur;

    // Message du créateur pour un groupe OUVERT (ex: "Besoin de personnes sérieuses, +45 ans")
    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(columnDefinition = "TEXT")
    private String commentaireValidation;

    private BigDecimal montantHebergement;

    private BigDecimal montantRestauration;

    private BigDecimal montantTransport;

    private BigDecimal montantServices;

    private BigDecimal montantReductions;

    private BigDecimal montantTotalDevis;

    private BigDecimal acompteDevis;

    private BigDecimal soldeDevis;

    private String devisPdfUrl;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @OneToMany(mappedBy = "groupe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
        if (this.statut == null) {
            this.statut = (typeGroupe == TypeGroupe.OUVERT) ? StatutGroupe.EN_FORMATION : StatutGroupe.BROUILLON;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Groupe)) return false;
        Groupe groupe = (Groupe) o;
        return id != null && id.equals(groupe.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
