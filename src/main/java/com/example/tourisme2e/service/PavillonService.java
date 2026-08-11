package com.example.tourisme2e.service;

import com.example.tourisme2e.dto.PavillonRequest;
import com.example.tourisme2e.dto.PavillonResponse;
import com.example.tourisme2e.entity.Pavillon;
import com.example.tourisme2e.exception.ResourceNotFoundException;
import com.example.tourisme2e.repository.PavillonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PavillonService {

    private final PavillonRepository pavillonRepository;

    public List<PavillonResponse> lister() {
        return pavillonRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public PavillonResponse creer(PavillonRequest request) {
        Pavillon pavillon = new Pavillon();
        pavillon.setNom(request.getNom());
        pavillon.setCapaciteMin(request.getCapaciteMin());
        pavillon.setCapaciteMax(request.getCapaciteMax());
        pavillon.setStatut(request.getStatut());
        pavillonRepository.save(pavillon);
        return toResponse(pavillon);
    }

    private PavillonResponse toResponse(Pavillon p) {
        return new PavillonResponse(p.getId(), p.getNom(), p.getCapaciteMin(), p.getCapaciteMax(), p.getStatut());
    }
}