package com.example.tourisme2e.dto;

import com.example.tourisme2e.entity.Sexe;
import com.example.tourisme2e.entity.StatutParticipant;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ParticipantAdminResponse {
    private Long id;
    private Long groupeId;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private Integer age;
    private Sexe sexe;
    private String pays;
    private String message;
    private StatutParticipant statut;
    private BigDecimal montantAcompte;
    private LocalDateTime dateInscription;
}