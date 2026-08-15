package com.example.tourisme2e.service;

import com.example.tourisme2e.dto.DashboardResponse;
import com.example.tourisme2e.dto.SejourConfirmeResponse;
import com.example.tourisme2e.entity.Disponibilite;
import com.example.tourisme2e.entity.Reservation;
import com.example.tourisme2e.entity.Segment;
import com.example.tourisme2e.entity.StatutDevis;
import com.example.tourisme2e.entity.StatutReservation;
import com.example.tourisme2e.repository.DemandeDevisRepository;
import com.example.tourisme2e.repository.DisponibiliteRepository;
import com.example.tourisme2e.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private static final String NOM_PAVILLON_PRINCIPAL = "Pavillon 1";
    private static final int LIMITE_PROCHAINS_SEJOURS = 10;

    private final DemandeDevisRepository demandeDevisRepository;
    private final DisponibiliteRepository disponibiliteRepository;
    private final ReservationRepository reservationRepository;

    @Transactional(readOnly = true)  // ← AJOUTÉ
    public DashboardResponse getDashboard() {
        long devisSenior = demandeDevisRepository.countByStatutAndOffreSegment(StatutDevis.EN_ATTENTE, Segment.SENIOR);
        long devisMice = demandeDevisRepository.countByStatutAndOffreSegment(StatutDevis.EN_ATTENTE, Segment.MICE);

        Double tauxRemplissage = calculerTauxRemplissagePavillon1();

        List<SejourConfirmeResponse> prochainsSejours = reservationRepository
                .findProchainsSejoursConfirmes(StatutReservation.CONFIRMEE)
                .stream()
                .limit(LIMITE_PROCHAINS_SEJOURS)
                .map(this::toSejourResponse)
                .toList();

        return new DashboardResponse(devisSenior, devisMice, tauxRemplissage, prochainsSejours);
    }

    private Double calculerTauxRemplissagePavillon1() {
        List<Disponibilite> disponibilites = disponibiliteRepository.findByPavillonNom(NOM_PAVILLON_PRINCIPAL);

        if (disponibilites.isEmpty()) {
            return null;
        }

        int totalPlaces = disponibilites.stream().mapToInt(Disponibilite::getPlacesTotales).sum();
        int placesOccupees = disponibilites.stream()
                .mapToInt(d -> d.getPlacesTotales() - d.getPlacesRestantes())
                .sum();

        if (totalPlaces == 0) {
            return 0.0;
        }

        return Math.round((placesOccupees * 10000.0 / totalPlaces)) / 100.0;
    }

    private SejourConfirmeResponse toSejourResponse(Reservation r) {
        var demande = r.getDemandeDevis();
        return new SejourConfirmeResponse(
                r.getId(),
                demande.getOffre().getTitre(),
                null,
                demande.getDateSouhaitee(),
                null,
                demande.getNbParticipants()
        );
    }
}