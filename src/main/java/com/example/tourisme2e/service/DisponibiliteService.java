package com.example.tourisme2e.service;

import com.example.tourisme2e.dto.DisponibiliteRequest;
import com.example.tourisme2e.dto.DisponibiliteResponse;
import com.example.tourisme2e.entity.Disponibilite;
import com.example.tourisme2e.entity.Offre;
import com.example.tourisme2e.entity.Pavillon;
import com.example.tourisme2e.exception.PlacesInsuffisantesException;
import com.example.tourisme2e.exception.ResourceNotFoundException;
import com.example.tourisme2e.repository.DisponibiliteRepository;
import com.example.tourisme2e.repository.OffreRepository;
import com.example.tourisme2e.repository.PavillonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DisponibiliteService {

    private final DisponibiliteRepository disponibiliteRepository;
    private final OffreRepository offreRepository;
    private final PavillonRepository pavillonRepository;

    @Transactional(readOnly = true)  // ← AJOUTÉ
    public List<DisponibiliteResponse> rechercher(Long offreId, LocalDate dateDebut, LocalDate dateFin) {
        return disponibiliteRepository.rechercherCreneaux(offreId, dateDebut, dateFin).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional  // ← AJOUTÉ aussi (bonne pratique pour toute écriture)
    public DisponibiliteResponse creer(DisponibiliteRequest request) {
        Offre offre = offreRepository.findById(request.getOffreId())
                .orElseThrow(() -> new ResourceNotFoundException("Offre non trouvée avec l'id : " + request.getOffreId()));
        Pavillon pavillon = pavillonRepository.findById(request.getPavillonId())
                .orElseThrow(() -> new ResourceNotFoundException("Pavillon non trouvé avec l'id : " + request.getPavillonId()));

        Disponibilite disponibilite = new Disponibilite();
        disponibilite.setOffre(offre);
        disponibilite.setPavillon(pavillon);
        disponibilite.setDateDebut(request.getDateDebut());
        disponibilite.setDateFin(request.getDateFin());
        disponibilite.setPlacesTotales(request.getPlacesTotales());
        disponibilite.setPlacesRestantes(request.getPlacesTotales());

        disponibiliteRepository.save(disponibilite);
        return toResponse(disponibilite);
    }

    @Transactional
    public void reserverPlaces(Long disponibiliteId, int nbParticipants) {
        Disponibilite disponibilite = disponibiliteRepository.findById(disponibiliteId)
                .orElseThrow(() -> new ResourceNotFoundException("Disponibilité non trouvée avec l'id : " + disponibiliteId));

        if (disponibilite.getPlacesRestantes() < nbParticipants) {
            throw new PlacesInsuffisantesException(
                    "Places insuffisantes : " + disponibilite.getPlacesRestantes() + " restantes, " + nbParticipants + " demandées"
            );
        }

        disponibilite.setPlacesRestantes(disponibilite.getPlacesRestantes() - nbParticipants);
        disponibiliteRepository.save(disponibilite);
    }

    private DisponibiliteResponse toResponse(Disponibilite d) {
        return new DisponibiliteResponse(
                d.getId(),
                d.getOffre().getId(),
                d.getPavillon().getId(),
                d.getPavillon().getNom(),
                d.getDateDebut(),
                d.getDateFin(),
                d.getPlacesTotales(),
                d.getPlacesRestantes()
        );
    }
}