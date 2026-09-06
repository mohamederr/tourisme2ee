package com.example.tourisme2e.service;

import com.example.tourisme2e.dto.DashboardResponse;
import com.example.tourisme2e.entity.*;
import com.example.tourisme2e.repository.GroupeRepository;
import com.example.tourisme2e.repository.ParticipantRepository;
import com.example.tourisme2e.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private static final int SEUIL_ALERTE_PLACES_RESTANTES = 2;

    private final GroupeRepository groupeRepository;
    private final ParticipantRepository participantRepository;
    private final UtilisateurRepository utilisateurRepository;

    @Transactional(readOnly = true)
    public DashboardResponse getDashboard() {
        List<Groupe> brouillon = groupeRepository.findByStatut(StatutGroupe.BROUILLON);
        List<Groupe> enAttenteValidation = groupeRepository.findByStatut(StatutGroupe.EN_ATTENTE_VALIDATION);
        long demandesEnAttenteValidation = brouillon.size() + enAttenteValidation.size();

        List<StatutGroupe> statutsActifs = List.of(StatutGroupe.EN_FORMATION, StatutGroupe.ACTIF, StatutGroupe.COMPLET);
        List<Groupe> groupesActifs = groupeRepository.findByStatutIn(statutsActifs);

        long groupesActifsFermes = groupesActifs.stream().filter(g -> g.getTypeGroupe() == TypeGroupe.FERME).count();
        long groupesActifsOuverts = groupesActifs.stream().filter(g -> g.getTypeGroupe() == TypeGroupe.OUVERT).count();

        LocalDateTime debutMois = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        BigDecimal revenusDuMois = participantRepository.sommeAcomptesDuMois(debutMois);

        List<Groupe> groupesOuvertsActifs = groupesActifs.stream()
                .filter(g -> g.getTypeGroupe() == TypeGroupe.OUVERT)
                .toList();

        double tauxRemplissageMoyen = groupesOuvertsActifs.isEmpty() ? 0.0 :
                groupesOuvertsActifs.stream()
                        .mapToDouble(g -> {
                            long confirmes = participantRepository.countByGroupeAndStatut(g, StatutParticipant.CONFIRME);
                            return g.getCapaciteMax() == 0 ? 0.0 : (confirmes * 100.0) / g.getCapaciteMax();
                        })
                        .average()
                        .orElse(0.0);
        tauxRemplissageMoyen = Math.round(tauxRemplissageMoyen * 100.0) / 100.0;

        long nouveauxUtilisateurs = utilisateurRepository.countByDateInscriptionAfter(debutMois);

        List<String> alertes = new ArrayList<>();
        for (Groupe g : groupesOuvertsActifs) {
            long confirmes = participantRepository.countByGroupeAndStatut(g, StatutParticipant.CONFIRME);
            int placesVersSeuil = g.getCapaciteMin() - (int) confirmes;
            if (placesVersSeuil > 0 && placesVersSeuil <= SEUIL_ALERTE_PLACES_RESTANTES) {
                alertes.add("Groupe \"" + g.getTitre() + "\" : " + confirmes + "/" + g.getCapaciteMin()
                        + " confirmés (" + placesVersSeuil + " places)");
            }
        }
        if (!brouillon.isEmpty()) {
            alertes.add(brouillon.size() + " demande" + (brouillon.size() > 1 ? "s" : "") + " en attente de devis");
        }
        // Alerte "Email non envoyé" volontairement absente ici — voir remarque ci-dessous.

        return new DashboardResponse(
                demandesEnAttenteValidation,
                groupesActifs.size(),
                groupesActifsFermes,
                groupesActifsOuverts,
                revenusDuMois,
                tauxRemplissageMoyen,
                nouveauxUtilisateurs,
                alertes
        );
    }
}