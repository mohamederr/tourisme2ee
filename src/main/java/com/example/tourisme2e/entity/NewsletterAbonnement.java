package com.example.tourisme2e.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "newsletter_abonnements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsletterAbonnement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private Boolean actif = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateAbonnement;

    @PrePersist
    protected void onCreate() {
        this.dateAbonnement = LocalDateTime.now();
        if (this.actif == null) {
            this.actif = true;
        }
    }
}
