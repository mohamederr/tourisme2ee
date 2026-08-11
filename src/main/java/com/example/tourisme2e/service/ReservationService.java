package com.example.tourisme2e.service;

import com.example.tourisme2e.dto.ReservationResponse;
import com.example.tourisme2e.entity.*;
import com.example.tourisme2e.exception.ConflitEtatException;
import com.example.tourisme2e.exception.ResourceNotFoundException;
import com.example.tourisme2e.repository.DemandeDevisRepository;
import com.example.tourisme2e.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final DemandeDevisRepository demandeDevisRepository;
    private final DisponibiliteService disponibiliteService;

    @Transactional
    public ReservationResponse confirmer(Long demandeDevisId, Long disponibiliteId) {
        DemandeDevis demande = demandeDevisRepository.findById(demandeDevisId)
                .orElseThrow(() -> new ResourceNotFoundException("Demande non trouvée avec l'id : " + demandeDevisId));

        if (demande.getStatut() == StatutDevis.CONFIRME) {
            throw new ConflitEtatException("Cette demande a déjà été confirmée");
        }
        if (demande.getStatut() == StatutDevis.REFUSE) {
            throw new ConflitEtatException("Impossible de confirmer une demande déjà refusée");
        }

        // Décrémente les places de manière sûre (protégé par @Version côté Disponibilite)
        disponibiliteService.reserverPlaces(disponibiliteId, demande.getNbParticipants());

        Reservation reservation = new Reservation();
        reservation.setDemandeDevis(demande);
        reservation.setDateConfirmation(java.time.LocalDateTime.now());
        reservation.setStatut(StatutReservation.CONFIRMEE);
        reservationRepository.save(reservation);

        demande.setStatut(StatutDevis.CONFIRME);
        demandeDevisRepository.save(demande);

        return toResponse(reservation);
    }

    public ReservationResponse getOne(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Réservation non trouvée avec l'id : " + id));
        return toResponse(reservation);
    }

    private ReservationResponse toResponse(Reservation r) {
        return new ReservationResponse(
                r.getId(),
                r.getDemandeDevis().getId(),
                r.getDateConfirmation(),
                r.getStatut()
        );
    }
}