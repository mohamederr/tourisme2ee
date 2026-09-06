package com.example.tourisme2e.mapper;

import com.example.tourisme2e.dto.OffreRequest;
import com.example.tourisme2e.dto.OffreResponse;
import com.example.tourisme2e.entity.HotelCentre;
import com.example.tourisme2e.entity.Offre;
import org.springframework.stereotype.Component;

@Component
public class OffreMapper {

    public Offre toEntity(OffreRequest request) {
        Offre offre = new Offre();
        updateEntity(offre, request);
        return offre;
    }

    public void updateEntity(Offre offre, OffreRequest request) {
        offre.setTitre(request.getTitre());
        offre.setDescription(request.getDescription());
        offre.setDescriptionCourte(request.getDescriptionCourte());
        offre.setDescriptionLongue(request.getDescriptionLongue());
        offre.setSegment(request.getSegment());
        offre.setTypeGroupe(request.getTypeGroupe());
        offre.setDateDebut(request.getDateDebut());
        offre.setDateFin(request.getDateFin());
        offre.setCapaciteMin(request.getCapaciteMin());
        offre.setCapaciteMax(request.getCapaciteMax());
        offre.setPrixIndicatif(request.getPrixIndicatif());
        offre.setDuree(request.getDuree());
        offre.setPhotos(request.getPhotos());
        offre.setSitesTouristiques(request.getSitesTouristiques());
        offre.setActivitesIncluses(request.getActivitesIncluses());
        offre.setNiveauConfort(request.getNiveauConfort());
        offre.setPension(request.getPension());
        offre.setPrixBase(request.getPrixBase());
        offre.setServicesAdditionnels(request.getServicesAdditionnels());
        offre.setStatut(request.getStatut());
    }

    public OffreResponse toResponse(Offre offre) {
        HotelCentre hotel = offre.getHotel();
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
                offre.getSitesTouristiques(),
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
