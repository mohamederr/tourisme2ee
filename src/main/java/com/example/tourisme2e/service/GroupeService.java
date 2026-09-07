package com.example.tourisme2e.service;

import com.example.tourisme2e.dto.*;
import com.example.tourisme2e.entity.*;
import com.example.tourisme2e.exception.ConflitEtatException;
import com.example.tourisme2e.exception.ResourceNotFoundException;
import com.example.tourisme2e.repository.GroupeRepository;
import com.example.tourisme2e.repository.HotelCentreRepository;
import com.example.tourisme2e.repository.ParticipantRepository;
import com.example.tourisme2e.repository.SiteTouristiqueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class GroupeService {

    private static final int CAPACITE_MIN_OUVERT = 10;
    private static final int CAPACITE_MAX_OUVERT = 20;
    private static final BigDecimal TVA = new BigDecimal("0.20");

    private final GroupeRepository groupeRepository;
    private final HotelCentreRepository hotelCentreRepository;
    private final ParticipantRepository participantRepository;
    private final SiteTouristiqueRepository siteTouristiqueRepository;
    private final DevisPdfService devisPdfService;
    private final ReductionService reductionService;
    private final EmailNotificationService emailNotificationService;

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
        groupe.setPrixBase(BigDecimal.ZERO); // fixé par l'admin lors du devis

        if (request.getHotelId() != null) {
            HotelCentre hotel = hotelCentreRepository.findById(request.getHotelId())
                    .orElseThrow(() -> new ResourceNotFoundException("Hôtel non trouvé avec l'id : " + request.getHotelId()));
            groupe.setHotel(hotel);
        }

        // Association des sites touristiques multi-select
        if (request.getSiteTouristiqueIds() != null && !request.getSiteTouristiqueIds().isEmpty()) {
            List<SiteTouristique> sites = siteTouristiqueRepository.findAllById(request.getSiteTouristiqueIds());
            groupe.setSites(sites);
        }

        groupeRepository.save(groupe);

        // Notification admin : nouvelle demande de groupe fermé
        emailNotificationService.notifierNouvelleDemandeGroupeFerme(groupe, utilisateur);

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
        groupe.setPrixBase(BigDecimal.ZERO); // fixé par l'admin à la validation

        // Association des sites touristiques multi-select
        if (request.getSiteTouristiqueIds() != null && !request.getSiteTouristiqueIds().isEmpty()) {
            List<SiteTouristique> sites = siteTouristiqueRepository.findAllById(request.getSiteTouristiqueIds());
            groupe.setSites(sites);
        }

        groupeRepository.save(groupe);

        // Enregistrement automatique du créateur comme 1er participant (§4 Scénario A)
        if (request.getAgeCréateur() != null && request.getSexeCréateur() != null) {
            Participant createur = new Participant();
            createur.setGroupe(groupe);
            createur.setNom(utilisateur.getNom());
            createur.setPrenom(utilisateur.getPrenom());
            createur.setEmail(utilisateur.getEmail());
            createur.setAge(request.getAgeCréateur());
            createur.setSexe(request.getSexeCréateur());
            createur.setPays(request.getPaysCréateur() != null ? request.getPaysCréateur() : "Maroc");
            createur.setStatut(StatutParticipant.EN_ATTENTE);
            participantRepository.save(createur);
        }

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

    /**
     * Génère un devis complet pour un groupe fermé.
     * Calcule automatiquement les réductions (volume + délai), la TVA (20%) et les montants HT/TTC.
     * Conforme au §11 du cahier des charges.
     */
    @Transactional
    public GroupeResponse genererDevis(Long id, GenererDevisRequest request) {
        Groupe groupe = groupeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Groupe non trouve avec l'id : " + id));

        BigDecimal hebergement = valeur(request.getMontantHebergement());
        BigDecimal restauration = valeur(request.getMontantRestauration());
        BigDecimal transport = valeur(request.getMontantTransport());
        BigDecimal services = valeur(request.getMontantServices());

        BigDecimal totalHT = hebergement.add(restauration).add(transport).add(services);

        // Calcul automatique des réductions selon §5 du cahier des charges
        BigDecimal reductions = valeur(request.getMontantReductions());
        if (reductions.compareTo(BigDecimal.ZERO) == 0 && groupe.getPrixBase().compareTo(BigDecimal.ZERO) > 0) {
            int nbParticipants = groupe.getCapaciteMin();
            LocalDate dateReservation = LocalDate.now();
            ReductionResponse reduction = reductionService.calculer(
                    groupe.getPrixBase(), nbParticipants, dateReservation, groupe.getDateDebut());
            reductions = reduction.getEconomieTotale();
        }

        BigDecimal totalApresReduction = totalHT.subtract(reductions);
        if (totalApresReduction.signum() < 0) {
            totalApresReduction = BigDecimal.ZERO;
        }

        // TVA 20%
        BigDecimal montantTVA = totalApresReduction.multiply(TVA).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalTTC = totalApresReduction.add(montantTVA).setScale(2, RoundingMode.HALF_UP);

        // Numéro de devis officiel unique
        String numeroDevis = genererNumeroDevis();

        // Date limite du solde (15 jours avant le départ)
        LocalDate dateLimiteSolde = groupe.getDateDebut().minusDays(15);

        groupe.setMontantHebergement(hebergement);
        groupe.setMontantRestauration(restauration);
        groupe.setMontantTransport(transport);
        groupe.setMontantServices(services);
        groupe.setMontantReductions(reductions);
        groupe.setMontantTotalDevis(totalTTC);
        groupe.setAcompteDevis(totalTTC.multiply(new BigDecimal("0.10")).setScale(2, RoundingMode.HALF_UP));
        groupe.setSoldeDevis(totalTTC.multiply(new BigDecimal("0.90")).setScale(2, RoundingMode.HALF_UP));
        groupe.setDevisPdfUrl(request.getDevisPdfUrl() != null ? request.getDevisPdfUrl() : "/api/v1/groupes/" + id + "/devis.pdf");
        groupe.setNumeroDevis(numeroDevis);
        groupe.setDateDevis(LocalDate.now());
        groupe.setDateLimiteSolde(dateLimiteSolde);
        groupe.setStatut(StatutGroupe.DEVIS_ENVOYE);
        groupeRepository.save(groupe);

        // Notification email au responsable du groupe
        emailNotificationService.envoyerDevisParEmail(groupe);

        return toResponse(groupe);
    }

    /**
     * Permet au responsable/client de confirmer et valider son devis reçu.
     */
    @Transactional
    public GroupeResponse confirmerDevisParClient(Long id) {
        Groupe groupe = groupeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Groupe non trouve avec l'id : " + id));

        if (groupe.getStatut() != StatutGroupe.DEVIS_ENVOYE) {
            throw new ConflitEtatException("Le devis ne peut être confirmé que lorsqu'il est à l'état DEVIS_ENVOYE. Statut actuel : " + groupe.getStatut());
        }

        groupe.setStatut(StatutGroupe.ACTIF);
        groupeRepository.save(groupe);

        // Notification admin : devis confirmé par le client
        emailNotificationService.notifierDevisConfirmeParClient(groupe);

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
        // Places restantes avant d'atteindre le seuil de 10 (validation du groupe)
        int placesVersSeuilValidation = Math.max(0, g.getCapaciteMin() - (int) confirmes);

        List<SiteTouristiqueSummaryDto> sitesDto = g.getSites().stream()
                .map(s -> new SiteTouristiqueSummaryDto(s.getId(), s.getNom(), s.getCategorie()))
                .toList();

        return new GroupeResponse(
                g.getId(), g.getTitre(), g.getDescriptionCourte(), g.getTypeGroupe(), g.getStatut(),
                g.getDateDebut(), g.getDateFin(), g.getCapaciteMin(), g.getCapaciteMax(),
                confirmes, placesRestantes, placesVersSeuilValidation, g.getPrixBase(),
                g.getMessage(), g.getCommentaireValidation(),
                g.getMontantHebergement(), g.getMontantRestauration(), g.getMontantTransport(),
                g.getMontantServices(), g.getMontantReductions(), g.getMontantTotalDevis(),
                g.getAcompteDevis(), g.getSoldeDevis(), g.getDevisPdfUrl(),
                g.getNumeroDevis(), g.getDateDevis(), g.getDateLimiteSolde(), g.getAcompteRegle(),
                sitesDto
        );
    }

    private BigDecimal valeur(BigDecimal montant) {
        return montant != null ? montant : BigDecimal.ZERO;
    }

    /**
     * Génère un numéro de devis officiel unique au format DEV-YYYY-XXXXX.
     */
    private String genererNumeroDevis() {
        String annee = String.valueOf(LocalDate.now().getYear());
        long count = groupeRepository.count() + 1;
        return "DEV-" + annee + "-" + String.format("%05d", count);
    }
}
