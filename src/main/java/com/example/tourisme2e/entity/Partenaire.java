package com.example.tourisme2e.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "partenaires")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Partenaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypePartenaire type;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String logoUrl;

    private String siteWeb;

    private Boolean actif = true;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Partenaire)) return false;
        Partenaire that = (Partenaire) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
