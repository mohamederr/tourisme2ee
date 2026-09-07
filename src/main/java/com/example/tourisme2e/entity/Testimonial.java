package com.example.tourisme2e.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "testimonials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Testimonial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomAuteur;

    private String organisme;

    private String pays;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String texteAvis;

    @Column(nullable = false)
    private Integer noteEtoiles;

    private String photoAuteurUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutModeration statut;

    private Boolean afficherAccueil = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Testimonial)) return false;
        Testimonial that = (Testimonial) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
