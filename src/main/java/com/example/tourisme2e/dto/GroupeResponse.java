package com.example.tourisme2e.dto;

import com.example.tourisme2e.entity.StatutGroupe;
import com.example.tourisme2e.entity.TypeGroupe;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class GroupeResponse {
    private Long id;
    private String titre;
    private String descriptionCourte;
    private TypeGroupe typeGroupe;
    private StatutGroupe statut;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Integer capaciteMin;
    private Integer capaciteMax;
    private long nbParticipantsConfirmes;
    private int placesRestantes;
    private BigDecimal prixBase;
    private String message;
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
}
