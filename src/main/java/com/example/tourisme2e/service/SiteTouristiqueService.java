package com.example.tourisme2e.service;

import com.example.tourisme2e.entity.SiteTouristique;
import com.example.tourisme2e.exception.ResourceNotFoundException;
import com.example.tourisme2e.repository.SiteTouristiqueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SiteTouristiqueService {

    private final SiteTouristiqueRepository siteTouristiqueRepository;

    @Transactional(readOnly = true)
    public List<SiteTouristique> lister() {
        return siteTouristiqueRepository.findAll();
    }

    @Transactional(readOnly = true)
    public SiteTouristique getOne(Long id) {
        return siteTouristiqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Site touristique non trouve : " + id));
    }

    @Transactional
    public SiteTouristique creer(SiteTouristique request) {
        request.setId(null);
        return siteTouristiqueRepository.save(request);
    }

    @Transactional
    public SiteTouristique modifier(Long id, SiteTouristique request) {
        if (!siteTouristiqueRepository.existsById(id)) {
            throw new ResourceNotFoundException("Site touristique non trouve : " + id);
        }
        request.setId(id);
        return siteTouristiqueRepository.save(request);
    }

    @Transactional
    public void supprimer(Long id) {
        if (!siteTouristiqueRepository.existsById(id)) {
            throw new ResourceNotFoundException("Site touristique non trouve : " + id);
        }
        siteTouristiqueRepository.deleteById(id);
    }
}
