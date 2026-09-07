package com.example.tourisme2e.mapper;

import com.example.tourisme2e.dto.OffreRequest;
import com.example.tourisme2e.dto.OffreResponse;
import com.example.tourisme2e.dto.SiteTouristiqueSummaryDto;
import com.example.tourisme2e.entity.HotelCentre;
import com.example.tourisme2e.entity.Offre;
import com.example.tourisme2e.entity.StatutOffre;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OffreMapper {

    public Offre toEntity(OffreRequest request) {
        Offre offre = new Offre();
        updateEntity(offre, request);
        return offre;
    }

    public void updateEntity(Offre offre, OffreRequest request) {
        offre.setTitre(request.getTitre());

        // Descriptions
        offre.setDescriptionCourte(request.getDescriptionCourte());
        offre.setDescriptionLongue(request.getDescriptionLongue());
        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            offre.setDescription(request.getDescription());
        } else if (request.getDescriptionCourte() != null) {
            offre.setDescription(request.getDescriptionCourte());
        }

        offre.setSegment(request.getSegment());
        offre.setTypeGroupe(request.getTypeGroupe());
        offre.setDateDebut(request.getDateDebut());
        offre.setDateFin(request.getDateFin());

        // Capacité
        offre.setCapaciteMin(request.getCapaciteMin() != null ? request.getCapaciteMin() : 10);
        offre.setCapaciteMax(request.getCapaciteMax() != null ? request.getCapaciteMax() : 20);

        // Prix
        if (request.getPrixBase() != null) {
            offre.setPrixBase(request.getPrixBase());
            if (request.getPrixIndicatif() == null) {
                offre.setPrixIndicatif(request.getPrixBase());
            } else {
                offre.setPrixIndicatif(request.getPrixIndicatif());
            }
        } else if (request.getPrixIndicatif() != null) {
            offre.setPrixIndicatif(request.getPrixIndicatif());
            offre.setPrixBase(request.getPrixIndicatif());
        }

        offre.setDuree(request.getDuree());
        offre.setPhotos(request.getPhotos());

        if (request.getSitesTouristiques() != null) {
            offre.setSitesTouristiques(request.getSitesTouristiques());
        }

        offre.setActivitesIncluses(request.getActivitesIncluses());
        offre.setNiveauConfort(request.getNiveauConfort());
        offre.setPension(request.getPension());
        offre.setServicesAdditionnels(request.getServicesAdditionnels());

        if (request.getStatut() != null) {
            offre.setStatut(request.getStatut());
        } else if (offre.getStatut() == null) {
            offre.setStatut(StatutOffre.BROUILLON);
        }
    }

    public OffreResponse toResponse(Offre offre) {
        HotelCentre hotel = offre.getHotel();

        List<Long> siteIds = new ArrayList<>();
        List<SiteTouristiqueSummaryDto> siteDtos = new ArrayList<>();

        if (offre.getSites() != null && !offre.getSites().isEmpty()) {
            siteIds = offre.getSites().stream()
                    .map(s -> s.getId())
                    .collect(Collectors.toList());

            siteDtos = offre.getSites().stream()
                    .map(s -> new SiteTouristiqueSummaryDto(s.getId(), s.getNom(), s.getCategorie()))
                    .collect(Collectors.toList());
        }

        String sitesStr = offre.getSitesTouristiques();
        if ((sitesStr == null || sitesStr.isBlank()) && !siteDtos.isEmpty()) {
            sitesStr = siteDtos.stream()
                    .map(SiteTouristiqueSummaryDto::getNom)
                    .collect(Collectors.joining(", "));
        }

        return new OffreResponse(
                offre.getId(),
                offre.getTitre(),
                offre.getDescription(),
                offre.getDescriptionCourte(),
                offre.getDescriptionLongue(),
                offre.getSegment(),
                offre.getTypeGroupe(),
                offre.getDateDebut(),
                offre.getDateFin(),
                offre.getCapaciteMin(),
                offre.getCapaciteMax(),
                offre.getPrixIndicatif(),
                offre.getDuree(),
                offre.getPhotos(),
                sitesStr,
                siteIds,
                siteDtos,
                offre.getActivitesIncluses(),
                hotel != null ? hotel.getId() : null,
                hotel != null ? hotel.getNom() : null,
                offre.getNiveauConfort(),
                offre.getPension(),
                offre.getPrixBase(),
                offre.getServicesAdditionnels(),
                offre.getStatut(),
                offre.getDateCreation()
        );
    }
}
