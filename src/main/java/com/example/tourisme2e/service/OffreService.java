package com.example.tourisme2e.service;

import com.example.tourisme2e.dto.OffreRequest;
import com.example.tourisme2e.dto.OffreResponse;
import com.example.tourisme2e.dto.PageResponse;
import com.example.tourisme2e.entity.Offre;
import com.example.tourisme2e.entity.Segment;
import com.example.tourisme2e.entity.StatutOffre;
import com.example.tourisme2e.exception.ResourceNotFoundException;
import com.example.tourisme2e.mapper.OffreMapper;
import com.example.tourisme2e.repository.HotelCentreRepository;
import com.example.tourisme2e.repository.OffreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OffreService {

    private final OffreRepository offreRepository;
    private final HotelCentreRepository hotelCentreRepository;
    private final OffreMapper offreMapper;

    @Transactional(readOnly = true)
    public PageResponse<OffreResponse> listerOffres(Segment segment, StatutOffre statut, Pageable pageable) {
        var page = (segment != null && statut != null)
                ? offreRepository.findBySegmentAndStatut(segment, statut, pageable)
                : (segment != null)
                ? offreRepository.findBySegment(segment, pageable)
                : (statut != null)
                ? offreRepository.findByStatut(statut, pageable)
                : offreRepository.findAll(pageable);

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
        Offre offre = offreMapper.toEntity(request);
        appliquerHotel(offre, request.getHotelId());
        offreRepository.save(offre);
        return offreMapper.toResponse(offre);
    }

    @Transactional
    public OffreResponse modifierOffre(Long id, OffreRequest request) {
        Offre offre = offreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offre non trouvee avec l'id : " + id));
        offreMapper.updateEntity(offre, request);
        appliquerHotel(offre, request.getHotelId());
        offreRepository.save(offre);
        return offreMapper.toResponse(offre);
    }

    @Transactional
    public void supprimerOffre(Long id) {
        if (!offreRepository.existsById(id)) {
            throw new ResourceNotFoundException("Offre non trouvee avec l'id : " + id);
        }
        offreRepository.deleteById(id);
    }

    private void appliquerHotel(Offre offre, Long hotelId) {
        if (hotelId == null) {
            offre.setHotel(null);
            return;
        }
        offre.setHotel(hotelCentreRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel/Centre non trouve avec l'id : " + hotelId)));
    }
}
