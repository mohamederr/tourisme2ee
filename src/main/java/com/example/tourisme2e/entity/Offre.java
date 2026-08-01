package com.example.tourisme2e.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
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

    // Le cahier des charges impose un segment SENIOR ou MICE
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Segment segment;

    private BigDecimal prixIndicatif;

    private Integer duree; // Durée en jours

    private String photos; // URL ou chemin de l'image (ou JSON si plusieurs)

    // Le statut doit être ACTIF ou INACTIF
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutOffre statut;

    // Relation 1..N vers Disponibilite (une offre a plusieurs créneaux)
    @OneToMany(mappedBy = "offre", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Disponibilite> disponibilites = new ArrayList<>();

    // Relation 1..N vers DemandeDevis (une offre peut recevoir plusieurs demandes)
    @OneToMany(mappedBy = "offre", cascade = CascadeType.ALL)
    private List<DemandeDevis> demandesDevis = new ArrayList<>();

    // equals/hashCode basés sur l'id (bonne pratique pour les entités JPA)
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