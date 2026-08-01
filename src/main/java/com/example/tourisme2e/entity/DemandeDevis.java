package com.example.tourisme2e.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "demandes_devis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DemandeDevis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offre_id", nullable = false)
    private Offre offre;

    @Column(nullable = false)
    private LocalDate dateSouhaitee;

    @Column(nullable = false)
    private Integer nbParticipants;

    @Column(columnDefinition = "TEXT")
    private String besoinsSpecifiques;

    private java.math.BigDecimal budget;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutDevis statut;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @OneToOne(mappedBy = "demandeDevis", cascade = CascadeType.ALL)
    private Reservation reservation;

    @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
        if (this.statut == null) {
            this.statut = StatutDevis.EN_ATTENTE;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DemandeDevis)) return false;
        DemandeDevis that = (DemandeDevis) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}