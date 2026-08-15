package com.example.tourisme2e.service;

import com.example.tourisme2e.dto.DemandeDevisRequest;
import com.example.tourisme2e.dto.DemandeDevisResponse;
import com.example.tourisme2e.entity.*;
import com.example.tourisme2e.entity.Segment;  // ← BON import
import com.example.tourisme2e.exception.ResourceNotFoundException;
import com.example.tourisme2e.repository.DemandeDevisRepository;
import com.example.tourisme2e.repository.OffreRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemandeDevisService {

    private final DemandeDevisRepository demandeDevisRepository;
    private final OffreRepository offreRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DemandeDevisService(DemandeDevisRepository demandeDevisRepository, OffreRepository offreRepository) {
        this.demandeDevisRepository = demandeDevisRepository;
        this.offreRepository = offreRepository;
    }

    public DemandeDevisResponse creer(DemandeDevisRequest request, Utilisateur utilisateur) {
        Offre offre = offreRepository.findById(request.getOffreId())
                .orElseThrow(() -> new ResourceNotFoundException("Offre non trouvée avec l'id : " + request.getOffreId()));

        DemandeDevis demande = new DemandeDevis();
        demande.setUtilisateur(utilisateur);
        demande.setOffre(offre);
        demande.setDateSouhaitee(request.getDateSouhaitee());
        demande.setNbParticipants(request.getNbParticipants());
        demande.setMessage(request.getMessage());

        if (offre.getSegment() == Segment.SENIOR) {
            demande.setPaysOrigine(request.getPaysOrigine());
            demande.setBesoinsSpecifiques(request.getBesoinsSpecifiques());
        } else if (offre.getSegment() == Segment.MICE) {
            demande.setDureeJours(request.getDureeJours());
            demande.setEquipementRequis(serialiserEquipement(request.getEquipementRequis()));
        }

        demandeDevisRepository.save(demande);
        return toResponse(demande);
    }

    public Page<DemandeDevisResponse> listerToutes(StatutDevis statut, Pageable pageable) {
        Page<DemandeDevis> page = (statut != null)
                ? demandeDevisRepository.findByStatut(statut, pageable)
                : demandeDevisRepository.findAll(pageable);
        return page.map(this::toResponse);
    }

    public Page<DemandeDevisResponse> mesDemandes(Utilisateur utilisateur, Pageable pageable) {
        return demandeDevisRepository.findByUtilisateur(utilisateur, pageable)
                .map(this::toResponse);
    }

    public DemandeDevisResponse getOne(Long id) {
        DemandeDevis demande = demandeDevisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande non trouvée avec l'id : " + id));
        return toResponse(demande);
    }

    public DemandeDevisResponse changerStatut(Long id, StatutDevis nouveauStatut) {
        DemandeDevis demande = demandeDevisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande non trouvée avec l'id : " + id));
        demande.setStatut(nouveauStatut);
        demandeDevisRepository.save(demande);
        return toResponse(demande);
    }

    private String serialiserEquipement(List<String> equipement) {
        if (equipement == null) return null;
        try {
            return objectMapper.writeValueAsString(equipement);
        } catch (Exception e) {
            return null;
        }
    }

    private DemandeDevisResponse toResponse(DemandeDevis d) {
        return new DemandeDevisResponse(
                d.getId(),
                d.getUtilisateur().getId(),
                d.getOffre().getId(),
                d.getOffre().getTitre(),
                d.getDateSouhaitee(),
                d.getNbParticipants(),
                d.getBesoinsSpecifiques(),
                d.getMessage(),
                d.getStatut(),
                d.getDateCreation()
        );
    }
}