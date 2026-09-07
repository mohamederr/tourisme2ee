package com.example.tourisme2e.service;

import com.example.tourisme2e.dto.OffreRequest;
import com.example.tourisme2e.dto.OffreResponse;
import com.example.tourisme2e.dto.PageResponse;
import com.example.tourisme2e.entity.*;
import com.example.tourisme2e.entity.Segment;
import com.example.tourisme2e.exception.ResourceNotFoundException;
import com.example.tourisme2e.mapper.OffreMapper;
import com.example.tourisme2e.repository.HotelCentreRepository;
import com.example.tourisme2e.repository.OffreRepository;
import com.example.tourisme2e.repository.SiteTouristiqueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OffreService {

    private final OffreRepository offreRepository;
    private final HotelCentreRepository hotelCentreRepository;
    private final SiteTouristiqueRepository siteTouristiqueRepository;
    private final OffreMapper offreMapper;

    @Transactional(readOnly = true)
    public PageResponse<OffreResponse> listerOffres(Segment segment, StatutOffre statut, Pageable pageable) {
        return listerOffres(segment, statut, null, pageable);
    }

    @Transactional(readOnly = true)
    public PageResponse<OffreResponse> listerOffres(Segment segment, StatutOffre statut, TypeGroupe typeGroupe, Pageable pageable) {
        Specification<Offre> spec = (root, query, cb) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();
            if (segment != null) {
                predicates.add(cb.equal(root.get("segment"), segment));
            }
            if (statut != null) {
                predicates.add(cb.equal(root.get("statut"), statut));
            }
            if (typeGroupe != null) {
                predicates.add(cb.equal(root.get("typeGroupe"), typeGroupe));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };

        var page = offreRepository.findAll(spec, pageable);
        return new PageResponse<>(page.map(offreMapper::toResponse));
    }

    @Transactional(readOnly = true)
    public OffreResponse getOffre(Long id) {
        Offre offre = offreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offre non trouvee avec l'id : " + id));
        return offreMapper.toResponse(offre);
    }

    @Transactional
    public OffreResponse creerOffre(OffreRequest request) {
        validerDonneesOffre(request);

        Offre offre = offreMapper.toEntity(request);
        appliquerHotel(offre, request.getHotelId());
        appliquerSites(offre, request.getSiteTouristiqueIds());

        Offre saved = offreRepository.save(offre);
        return offreMapper.toResponse(saved);
    }

    @Transactional
    public OffreResponse modifierOffre(Long id, OffreRequest request) {
        validerDonneesOffre(request);

        Offre offre = offreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offre non trouvee avec l'id : " + id));

        offreMapper.updateEntity(offre, request);
        appliquerHotel(offre, request.getHotelId());
        appliquerSites(offre, request.getSiteTouristiqueIds());

        Offre saved = offreRepository.save(offre);
        return offreMapper.toResponse(saved);
    }

    @Transactional
    public OffreResponse changerStatut(Long id, StatutOffre nouveauStatut) {
        if (nouveauStatut == null) {
            throw new IllegalArgumentException("Le nouveau statut ne peut pas être nul");
        }
        Offre offre = offreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offre non trouvee avec l'id : " + id));
        offre.setStatut(nouveauStatut);
        Offre saved = offreRepository.save(offre);
        return offreMapper.toResponse(saved);
    }

    @Transactional
    public void supprimerOffre(Long id) {
        if (!offreRepository.existsById(id)) {
            throw new ResourceNotFoundException("Offre non trouvee avec l'id : " + id);
        }
        offreRepository.deleteById(id);
    }

    private void validerDonneesOffre(OffreRequest request) {
        // Validation chronologique des dates
        if (request.getDateDebut() != null && request.getDateFin() != null) {
            if (request.getDateFin().isBefore(request.getDateDebut())) {
                throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
            }
            // Calcul automatique de la durée si non renseignée
            if (request.getDuree() == null || request.getDuree() <= 0) {
                long jours = ChronoUnit.DAYS.between(request.getDateDebut(), request.getDateFin()) + 1;
                request.setDuree((int) Math.max(1, jours));
            }
        }

        // Validation des capacités min 10 et max 20
        int min = request.getCapaciteMin() != null ? request.getCapaciteMin() : 10;
        int max = request.getCapaciteMax() != null ? request.getCapaciteMax() : 20;

        if (min < 10) {
            throw new IllegalArgumentException("La capacité minimale doit être d'au moins 10");
        }
        if (max > 20) {
            throw new IllegalArgumentException("La capacité maximale ne peut pas dépasser 20");
        }
        if (min > max) {
            throw new IllegalArgumentException("La capacité minimale (" + min + ") ne peut pas dépasser la capacité maximale (" + max + ")");
        }

        // Validation du niveau de confort si renseigné (2 à 5 étoiles)
        if (request.getNiveauConfort() != null) {
            if (request.getNiveauConfort() < 2 || request.getNiveauConfort() > 5) {
                throw new IllegalArgumentException("Le niveau de confort doit être compris entre 2 et 5 étoiles");
            }
        }
    }

    private void appliquerHotel(Offre offre, Long hotelId) {
        if (hotelId == null) {
            offre.setHotel(null);
            return;
        }
        HotelCentre hotel = hotelCentreRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel/Centre non trouve avec l'id : " + hotelId));
        offre.setHotel(hotel);
    }

    private void appliquerSites(Offre offre, List<Long> siteTouristiqueIds) {
        if (siteTouristiqueIds == null || siteTouristiqueIds.isEmpty()) {
            return;
        }
        List<SiteTouristique> sites = siteTouristiqueRepository.findAllById(siteTouristiqueIds);
        offre.setSites(sites);

        if (offre.getSitesTouristiques() == null || offre.getSitesTouristiques().isBlank()) {
            String noms = sites.stream()
                    .map(SiteTouristique::getNom)
                    .collect(Collectors.joining(", "));
            offre.setSitesTouristiques(noms);
        }
    }
}
