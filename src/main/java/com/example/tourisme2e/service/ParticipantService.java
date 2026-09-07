package com.example.tourisme2e.service;

import com.example.tourisme2e.dto.PaiementAcompteRequest;
import com.example.tourisme2e.dto.ParticipantAdminResponse;
import com.example.tourisme2e.dto.ParticipantPublicResponse;
import com.example.tourisme2e.dto.RejoindreGroupeRequest;
import com.example.tourisme2e.entity.*;
import com.example.tourisme2e.exception.GroupeCompletException;
import com.example.tourisme2e.exception.ResourceNotFoundException;
import com.example.tourisme2e.repository.GroupeRepository;
import com.example.tourisme2e.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private static final int SEUIL_CONFIRMATION_GROUPE = 10;

    private final ParticipantRepository participantRepository;
    private final GroupeRepository groupeRepository;
    private final EmailNotificationService emailNotificationService;

    @Transactional
    public ParticipantAdminResponse rejoindreGroupe(Long groupeId, RejoindreGroupeRequest request) {
        Groupe groupe = groupeRepository.findById(groupeId)
                .orElseThrow(() -> new ResourceNotFoundException("Groupe non trouvé avec l'id : " + groupeId));

        if (groupe.getTypeGroupe() != TypeGroupe.OUVERT) {
            throw new IllegalArgumentException("Seuls les groupes ouverts acceptent de nouveaux participants");
        }
        if (groupe.getStatut() == StatutGroupe.COMPLET || groupe.getStatut() == StatutGroupe.ARCHIVE) {
            throw new GroupeCompletException("Ce groupe n'accepte plus de nouveaux participants");
        }

        long nbActuels = participantRepository.findByGroupe(groupe).stream()
                .filter(p -> p.getStatut() != StatutParticipant.REFUSE)
                .count();

        if (nbActuels >= groupe.getCapaciteMax()) {
            throw new GroupeCompletException("Ce groupe a atteint sa capacité maximale de " + groupe.getCapaciteMax() + " participants");
        }

        Participant participant = new Participant();
        participant.setGroupe(groupe);
        participant.setNom(request.getNom());
        participant.setPrenom(request.getPrenom());
        participant.setEmail(request.getEmail());
        participant.setTelephone(request.getTelephone());
        participant.setAge(request.getAge());
        participant.setSexe(request.getSexe());
        participant.setPays(request.getPays());
        participant.setMessage(request.getMessage());
        participant.setStatut(StatutParticipant.EN_ATTENTE);

        // Calcul automatique de l'acompte de 10% (§8 Étape 3 du cahier des charges)
        if (groupe.getPrixBase() != null && groupe.getPrixBase().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal acompte = groupe.getPrixBase()
                    .multiply(new BigDecimal("0.10"))
                    .setScale(2, RoundingMode.HALF_UP);
            participant.setMontantAcompte(acompte);
        }

        participantRepository.save(participant);

        // Email de confirmation d'inscription au participant (§8 Étape 3)
        emailNotificationService.confirmerInscriptionParticipant(
                participant.getEmail(),
                participant.getPrenom(),
                groupe.getTitre(),
                participant.getMontantAcompte()
        );

        return toAdminResponse(participant);
    }

    /**
     * Enregistre le paiement de l'acompte (10%) d'un participant.
     */
    @Transactional
    public ParticipantAdminResponse payerAcompte(Long participantId, PaiementAcompteRequest request) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new ResourceNotFoundException("Participant non trouvé avec l'id : " + participantId));

        participant.setModePaiement(request.getModePaiement());
        participant.setReferencePaiement(request.getTransactionId() != null
                ? request.getTransactionId()
                : "REF-" + System.currentTimeMillis());
        participant.setDatePaiement(LocalDateTime.now());

        // Mise à jour du montant si fourni
        if (request.getMontant() != null) {
            participant.setMontantAcompte(request.getMontant());
        }

        participantRepository.save(participant);

        emailNotificationService.notifierPaiementAcompte(
                participant.getEmail(),
                participant.getGroupe().getTitre(),
                participant.getMontantAcompte(),
                participant.getModePaiement()
        );

        return toAdminResponse(participant);
    }

    @Transactional
    public ParticipantAdminResponse confirmerParticipant(Long participantId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new ResourceNotFoundException("Participant non trouvé avec l'id : " + participantId));

        participant.setStatut(StatutParticipant.CONFIRME);
        participantRepository.save(participant);

        // Vérifie si le groupe atteint le seuil de confirmation (§8 Étape 5)
        Groupe groupe = participant.getGroupe();
        long nbConfirmes = participantRepository.countByGroupeAndStatut(groupe, StatutParticipant.CONFIRME);

        if (nbConfirmes >= SEUIL_CONFIRMATION_GROUPE && groupe.getStatut() == StatutGroupe.EN_FORMATION) {
            // Seuil de 10 participants atteint : le groupe devient ACTIF et les réductions s'appliquent automatiquement
            groupe.setStatut(StatutGroupe.ACTIF);
            groupeRepository.save(groupe);

            // Notification à tous les participants que le groupe est validé (§8 Étape 5)
            List<String> emails = participantRepository.findByGroupe(groupe).stream()
                    .filter(p -> p.getStatut() == StatutParticipant.CONFIRME)
                    .map(Participant::getEmail)
                    .collect(Collectors.toList());
            emailNotificationService.notifierGroupeComplet(groupe, emails);
        }

        if (nbConfirmes >= groupe.getCapaciteMax()) {
            groupe.setStatut(StatutGroupe.COMPLET); // capacité maximale de 20 atteinte, plus aucune place
            groupeRepository.save(groupe);
        }

        return toAdminResponse(participant);
    }

    @Transactional
    public ParticipantAdminResponse refuserParticipant(Long participantId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new ResourceNotFoundException("Participant non trouvé avec l'id : " + participantId));
        participant.setStatut(StatutParticipant.REFUSE);
        participantRepository.save(participant);
        return toAdminResponse(participant);
    }

    @Transactional(readOnly = true)
    public List<ParticipantPublicResponse> listerProfilsPublics(Long groupeId) {
        Groupe groupe = groupeRepository.findById(groupeId)
                .orElseThrow(() -> new ResourceNotFoundException("Groupe non trouvé avec l'id : " + groupeId));

        return participantRepository.findByGroupe(groupe).stream()
                .filter(p -> p.getStatut() == StatutParticipant.CONFIRME)
                .map(p -> new ParticipantPublicResponse(p.getId(), p.getProfilPublic()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ParticipantAdminResponse> listerTousAdmin(Long groupeId) {
        Groupe groupe = groupeRepository.findById(groupeId)
                .orElseThrow(() -> new ResourceNotFoundException("Groupe non trouvé avec l'id : " + groupeId));
        return participantRepository.findByGroupe(groupe).stream()
                .map(this::toAdminResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<ParticipantAdminResponse> rechercherAdmin(Long groupeId, StatutParticipant statut,
                                                          LocalDateTime debut, LocalDateTime fin,
                                                          String q, Pageable pageable) {
        return participantRepository.rechercherAdmin(groupeId, statut, debut, fin, q, pageable)
                .map(this::toAdminResponse);
    }

    @Transactional
    public void supprimerParticipant(Long participantId) {
        if (!participantRepository.existsById(participantId)) {
            throw new ResourceNotFoundException("Participant non trouve avec l'id : " + participantId);
        }
        participantRepository.deleteById(participantId);
    }

    @Transactional(readOnly = true)
    public String exporterCsv(Long groupeId, StatutParticipant statut, LocalDateTime debut, LocalDateTime fin, String q) {
        StringBuilder csv = new StringBuilder("Nom,Email,Offre,Statut,Age,Telephone,Pays,Date,Montant acompte,Mode paiement,Reference paiement,Notes\n");
        participantRepository.rechercherAdminExport(groupeId, statut, debut, fin, q).forEach(p ->
                csv.append(csv(p.getNom() + " " + p.getPrenom())).append(',')
                        .append(csv(p.getEmail())).append(',')
                        .append(csv(p.getGroupe().getTitre())).append(',')
                        .append(csv(p.getStatut().name())).append(',')
                        .append(p.getAge()).append(',')
                        .append(csv(p.getTelephone())).append(',')
                        .append(csv(p.getPays())).append(',')
                        .append(p.getDateInscription()).append(',')
                        .append(p.getMontantAcompte() != null ? p.getMontantAcompte() : "").append(',')
                        .append(csv(p.getModePaiement())).append(',')
                        .append(csv(p.getReferencePaiement())).append(',')
                        .append(csv(p.getMessage())).append('\n'));
        return csv.toString();
    }

    private ParticipantAdminResponse toAdminResponse(Participant p) {
        return new ParticipantAdminResponse(
                p.getId(), p.getGroupe().getId(), p.getNom(), p.getPrenom(), p.getEmail(),
                p.getTelephone(), p.getAge(), p.getSexe(), p.getPays(), p.getMessage(),
                p.getStatut(), p.getMontantAcompte(), p.getDateInscription()
        );
    }

    private String csv(String value) {
        if (value == null) {
            return "";
        }
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
