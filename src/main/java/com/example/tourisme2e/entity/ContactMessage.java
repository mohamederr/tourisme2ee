package com.example.tourisme2e.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "contact_messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String email;

    private String telephone;

    private String sujet;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    private Boolean traite = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateEnvoi;

    @PrePersist
    protected void onCreate() {
        this.dateEnvoi = LocalDateTime.now();
        if (this.traite == null) {
            this.traite = false;
        }
    }
}
