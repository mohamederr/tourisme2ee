package com.example.tourisme2e.service;

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

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private static final int SEUIL_CONFIRMATION_GROUPE = 10;

    private final ParticipantRepository participantRepository;
    private final GroupeRepository groupeRepository;

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

        participantRepository.save(participant);
        return toAdminResponse(participant);
    }

    @Transactional
    public ParticipantAdminResponse confirmerParticipant(Long participantId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new ResourceNotFoundException("Participant non trouvé avec l'id : " + participantId));

        participant.setStatut(StatutParticipant.CONFIRME);
        participantRepository.save(participant);

        // Vérifie si le groupe atteint le seuil de confirmation (§8 ETAPE 5)
        Groupe groupe = participant.getGroupe();
        long nbConfirmes = participantRepository.countByGroupeAndStatut(groupe, StatutParticipant.CONFIRME);

        if (nbConfirmes >= SEUIL_CONFIRMATION_GROUPE && groupe.getStatut() == StatutGroupe.EN_FORMATION) {
            groupe.setStatut(StatutGroupe.ACTIF); // "COMPLET" au sens du seuil minimum atteint, reste ouvert jusqu'au max
        }
        if (nbConfirmes >= groupe.getCapaciteMax()) {
            groupe.setStatut(StatutGroupe.COMPLET); // capacité maximale atteinte, plus aucune place
        }
        groupeRepository.save(groupe);

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
        StringBuilder csv = new StringBuilder("Nom,Email,Offre,Statut,Age,Telephone,Pays,Date,Montant acompte,Notes\n");
        participantRepository.rechercherAdminExport(groupeId, statut, debut, fin, q).forEach(p -> csv.append(csv(p.getNom() + " " + p.getPrenom())).append(',')
                .append(csv(p.getEmail())).append(',')
                .append(csv(p.getGroupe().getTitre())).append(',')
                .append(csv(p.getStatut().name())).append(',')
                .append(p.getAge()).append(',')
                .append(csv(p.getTelephone())).append(',')
                .append(csv(p.getPays())).append(',')
                .append(p.getDateInscription()).append(',')
                .append(p.getMontantAcompte() != null ? p.getMontantAcompte() : "").append(',')
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
