package com.example.tourisme2e.service;

import com.example.tourisme2e.dto.*;
import com.example.tourisme2e.entity.*;
import com.example.tourisme2e.exception.ResourceNotFoundException;
import com.example.tourisme2e.repository.GroupeRepository;
import com.example.tourisme2e.repository.HotelCentreRepository;
import com.example.tourisme2e.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupeService {

    private static final int CAPACITE_MIN_OUVERT = 10;
    private static final int CAPACITE_MAX_OUVERT = 20;

    private final GroupeRepository groupeRepository;
    private final HotelCentreRepository hotelCentreRepository;
    private final ParticipantRepository participantRepository;
    private final DevisPdfService devisPdfService;

    @Transactional
    public GroupeResponse creerGroupeFerme(CreerGroupeFermeRequest request, Utilisateur utilisateur) {
        Groupe groupe = new Groupe();
        groupe.setTitre(request.getTitre());
        groupe.setDescriptionCourte(request.getDescriptionCourte());
        groupe.setDescriptionLongue(request.getDescriptionLongue());
        groupe.setTypeGroupe(TypeGroupe.FERME);
        groupe.setTypeGroupeFerme(request.getTypeGroupeFerme());
        groupe.setDateDebut(request.getDateDebut());
        groupe.setDateFin(request.getDateFin());
        groupe.setCapaciteMin(request.getNbParticipants());
        groupe.setCapaciteMax(request.getNbParticipants());
        groupe.setCategorieSitePrincipale(request.getCategorieSitePrincipale());
        groupe.setActivitesIncluses(request.getActivitesIncluses());
        groupe.setNiveauConfort(request.getNiveauConfort());
        groupe.setPension(request.getPension());
        groupe.setServicesAdditionnels(request.getServicesAdditionnels());
        groupe.setMessage(request.getDemandesSpeciales());
        groupe.setCreateur(utilisateur);
        groupe.setStatut(StatutGroupe.BROUILLON); // en attente de devis admin
        groupe.setPrixBase(java.math.BigDecimal.ZERO); // fixé par l'admin lors du devis

        if (request.getHotelId() != null) {
            HotelCentre hotel = hotelCentreRepository.findById(request.getHotelId())
                    .orElseThrow(() -> new ResourceNotFoundException("Hôtel non trouvé avec l'id : " + request.getHotelId()));
            groupe.setHotel(hotel);
        }

        groupeRepository.save(groupe);
        return toResponse(groupe);
    }

    @Transactional
    public GroupeResponse creerGroupeOuvert(CreerGroupeOuvertRequest request, Utilisateur utilisateur) {
        Groupe groupe = new Groupe();
        groupe.setTitre(request.getTitre());
        groupe.setTypeGroupe(TypeGroupe.OUVERT);
        groupe.setDateDebut(request.getDateDebut());
        groupe.setDateFin(request.getDateFin());
        groupe.setCapaciteMin(CAPACITE_MIN_OUVERT);
        groupe.setCapaciteMax(CAPACITE_MAX_OUVERT);
        groupe.setCategorieSitePrincipale(request.getCategorieSitePrincipale());
        groupe.setActivitesIncluses(request.getActivitesIncluses());
        groupe.setNiveauConfort(request.getNiveauConfort());
        groupe.setMessage(request.getMessage());
        groupe.setCreateur(utilisateur);
        groupe.setStatut(StatutGroupe.EN_ATTENTE_VALIDATION); // doit être approuvé par l'admin avant d'être public
        groupe.setPrixBase(java.math.BigDecimal.ZERO); // fixé par l'admin à la validation

        groupeRepository.save(groupe);
        return toResponse(groupe);
    }

    @Transactional(readOnly = true)
    public Page<GroupeResponse> listerGroupesOuvertsPublics(Pageable pageable) {
        return groupeRepository
                .findByTypeGroupeAndStatutIn(TypeGroupe.OUVERT, List.of(StatutGroupe.EN_FORMATION, StatutGroupe.ACTIF), pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<GroupeResponse> listerTous(Pageable pageable) {
        return groupeRepository.findAll(pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<GroupeResponse> listerDemandesValidation(Pageable pageable) {
        return groupeRepository.findByTypeGroupeAndStatutIn(
                TypeGroupe.OUVERT,
                List.of(StatutGroupe.EN_ATTENTE_VALIDATION, StatutGroupe.CORRECTIONS_DEMANDEES),
                pageable
        ).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public GroupeResponse getGroupe(Long id) {
        Groupe groupe = groupeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Groupe non trouvé avec l'id : " + id));
        return toResponse(groupe);
    }

    @Transactional
    public GroupeResponse validerGroupe(Long id, ValiderGroupeRequest request) {
        Groupe groupe = groupeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Groupe non trouvé avec l'id : " + id));

        groupe.setCommentaireValidation(request.getCommentaire());
        if (request.getAction() == ActionValidationGroupe.APPROUVER) {
            groupe.setStatut(StatutGroupe.EN_FORMATION);
            if (request.getPrixBase() != null) {
                groupe.setPrixBase(request.getPrixBase());
            }
        } else if (request.getAction() == ActionValidationGroupe.DEMANDER_CORRECTIONS) {
            groupe.setStatut(StatutGroupe.CORRECTIONS_DEMANDEES);
        } else if (request.getAction() == ActionValidationGroupe.REFUSER) {
            groupe.setStatut(StatutGroupe.REFUSE);
        }
        groupeRepository.save(groupe);
        return toResponse(groupe);
    }

    @Transactional
    public GroupeResponse genererDevis(Long id, GenererDevisRequest request) {
        Groupe groupe = groupeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Groupe non trouve avec l'id : " + id));

        BigDecimal hebergement = valeur(request.getMontantHebergement());
        BigDecimal restauration = valeur(request.getMontantRestauration());
        BigDecimal transport = valeur(request.getMontantTransport());
        BigDecimal services = valeur(request.getMontantServices());
        BigDecimal reductions = valeur(request.getMontantReductions());
        BigDecimal total = hebergement.add(restauration).add(transport).add(services).subtract(reductions);
        if (total.signum() < 0) {
            total = BigDecimal.ZERO;
        }

        groupe.setMontantHebergement(hebergement);
        groupe.setMontantRestauration(restauration);
        groupe.setMontantTransport(transport);
        groupe.setMontantServices(services);
        groupe.setMontantReductions(reductions);
        groupe.setMontantTotalDevis(total);
        groupe.setAcompteDevis(total.multiply(new BigDecimal("0.10")));
        groupe.setSoldeDevis(total.multiply(new BigDecimal("0.90")));
        groupe.setDevisPdfUrl(request.getDevisPdfUrl() != null ? request.getDevisPdfUrl() : "/api/v1/groupes/" + id + "/devis.pdf");
        groupe.setStatut(StatutGroupe.DEVIS_ENVOYE);
        groupeRepository.save(groupe);
        return toResponse(groupe);
    }

    @Transactional(readOnly = true)
    public byte[] telechargerDevis(Long id) {
        Groupe groupe = groupeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Groupe non trouve avec l'id : " + id));
        return devisPdfService.generer(groupe);
    }

    @Transactional(readOnly = true)
    private GroupeResponse toResponse(Groupe g) {
        long confirmes = participantRepository.countByGroupeAndStatut(g, StatutParticipant.CONFIRME);
        int placesRestantes = g.getCapaciteMax() - (int) confirmes;
        return new GroupeResponse(
                g.getId(), g.getTitre(), g.getDescriptionCourte(), g.getTypeGroupe(), g.getStatut(),
                g.getDateDebut(), g.getDateFin(), g.getCapaciteMin(), g.getCapaciteMax(),
                confirmes, placesRestantes, g.getPrixBase(), g.getMessage(), g.getCommentaireValidation(),
                g.getMontantHebergement(), g.getMontantRestauration(), g.getMontantTransport(),
                g.getMontantServices(), g.getMontantReductions(), g.getMontantTotalDevis(),
                g.getAcompteDevis(), g.getSoldeDevis(), g.getDevisPdfUrl()
        );
    }

    private BigDecimal valeur(BigDecimal montant) {
        return montant != null ? montant : BigDecimal.ZERO;
    }
}
