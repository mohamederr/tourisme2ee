package com.example.tourisme2e.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "participants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupe_id", nullable = false)
    private Groupe groupe;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private String email;

    private String telephone;

    @Column(nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sexe sexe;

    @Column(nullable = false)
    private String pays;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutParticipant statut;

    private BigDecimal montantAcompte; // 10% versé

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateInscription;

    @PrePersist
    protected void onCreate() {
        this.dateInscription = LocalDateTime.now();
        if (this.statut == null) {
            this.statut = StatutParticipant.EN_ATTENTE;
        }
    }

    /**
     * Profil public affiché pour un groupe ouvert — initiales seules,
     * jamais le nom complet (confidentialité, §8 ETAPE 2 du cahier des charges).
     */
    @Transient
    public String getProfilPublic() {
        String initiale = prenom.isEmpty() ? "" : prenom.charAt(0) + ".";
        String initialeNom = nom.isEmpty() ? "" : nom.charAt(0) + ".";
        return initiale + initialeNom + " | " + age + " ans | " + (sexe == Sexe.HOMME ? "Homme" : "Femme");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Participant)) return false;
        Participant that = (Participant) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}