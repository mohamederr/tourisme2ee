package com.example.tourisme2e.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "pavillons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pavillon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private Integer capaciteMin;

    @Column(nullable = false)
    private Integer capaciteMax;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutPavillon statut;

    @OneToMany(mappedBy = "pavillon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Disponibilite> disponibilites = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pavillon)) return false;
        Pavillon pavillon = (Pavillon) o;
        return id != null && id.equals(pavillon.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}