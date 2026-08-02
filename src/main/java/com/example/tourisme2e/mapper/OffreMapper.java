package com.example.tourisme2e.mapper;

import com.example.tourisme2e.dto.OffreRequest;
import com.example.tourisme2e.dto.OffreResponse;
import com.example.tourisme2e.entity.Offre;
import org.springframework.stereotype.Component;

@Component
public class OffreMapper {

    public Offre toEntity(OffreRequest request) {
        Offre offre = new Offre();
        offre.setTitre(request.getTitre());
        offre.setDescription(request.getDescription());
        offre.setSegment(request.getSegment());
        offre.setPrixIndicatif(request.getPrixIndicatif());
        offre.setDuree(request.getDuree());
        offre.setPhotos(request.getPhotos());
        offre.setStatut(request.getStatut());
        return offre;
    }

    public void updateEntity(Offre offre, OffreRequest request) {
        offre.setTitre(request.getTitre());
        offre.setDescription(request.getDescription());
        offre.setSegment(request.getSegment());
        offre.setPrixIndicatif(request.getPrixIndicatif());
        offre.setDuree(request.getDuree());
        offre.setPhotos(request.getPhotos());
        offre.setStatut(request.getStatut());
    }

    public OffreResponse toResponse(Offre offre) {
        return new OffreResponse(
                offre.getId(),
                offre.getTitre(),
                offre.getDescription(),
                offre.getSegment(),
                offre.getPrixIndicatif(),
                offre.getDuree(),
                offre.getPhotos(),
                offre.getStatut(),
                offre.getDateCreation()
        );
    }
}