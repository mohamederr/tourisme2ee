package com.example.tourisme2e.service;

import com.example.tourisme2e.dto.ReductionResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;

@Service
public class ReductionService {

    /**
     * Réduction par volume (nombre de participants), paliers du §5 du cahier des charges.
     */
    public BigDecimal tauxReductionVolume(int nbParticipants) {
        if (nbParticipants >= 20) return new BigDecimal("0.25");
        if (nbParticipants >= 12) return new BigDecimal("0.20");
        if (nbParticipants >= 8)  return new BigDecimal("0.15");
        if (nbParticipants >= 4)  return new BigDecimal("0.10");
        return BigDecimal.ZERO;
    }

    /**
     * Réduction par délai de réservation : réservation à plus de 2 mois du séjour
     * donne droit à une réduction supplémentaire (§5). Le cahier des charges ne
     * précise pas de palier progressif au-delà de 2 mois — on applique donc le
     * taux maximal (30%) dès que le seuil de 2 mois est atteint.
     * À ajuster si le client précise une grille plus fine (ex: 2 mois = 15%, 4 mois = 30%).
     */
    public BigDecimal tauxReductionDelai(LocalDate dateReservation, LocalDate dateDebutSejour) {
        int moisAvance = Period.between(dateReservation, dateDebutSejour).getMonths()
                + Period.between(dateReservation, dateDebutSejour).getYears() * 12;
        return moisAvance >= 2 ? new BigDecimal("0.30") : BigDecimal.ZERO;
    }

    public ReductionResponse calculer(BigDecimal prixBaseUnitaire, int nbParticipants,
                                      LocalDate dateReservation, LocalDate dateDebutSejour) {
        BigDecimal tauxVolume = tauxReductionVolume(nbParticipants);
        BigDecimal tauxDelai = tauxReductionDelai(dateReservation, dateDebutSejour);
        BigDecimal tauxTotal = tauxVolume.add(tauxDelai); // cumul additif, cf. exemple §5 (25% + 30% = 55%)

        BigDecimal prixUnitaireFinal = prixBaseUnitaire
                .multiply(BigDecimal.ONE.subtract(tauxTotal))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal prixTotalFinal = prixUnitaireFinal.multiply(BigDecimal.valueOf(nbParticipants));
        BigDecimal prixTotalSansReduction = prixBaseUnitaire.multiply(BigDecimal.valueOf(nbParticipants));
        BigDecimal economie = prixTotalSansReduction.subtract(prixTotalFinal);

        return new ReductionResponse(prixBaseUnitaire, nbParticipants, tauxVolume, tauxDelai,
                tauxTotal, prixUnitaireFinal, prixTotalFinal, economie);
    }
}