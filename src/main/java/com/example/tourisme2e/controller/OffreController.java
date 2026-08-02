package com.example.tourisme2e.controller;

import com.example.tourisme2e.dto.OffreRequest;
import com.example.tourisme2e.dto.OffreResponse;
import com.example.tourisme2e.dto.PageResponse;
import com.example.tourisme2e.entity.Segment;
import com.example.tourisme2e.entity.StatutOffre;
import com.example.tourisme2e.service.OffreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/offres")
@RequiredArgsConstructor
public class OffreController {

    private final OffreService offreService;

    @GetMapping
    public ResponseEntity<PageResponse<OffreResponse>> lister(
            @RequestParam(required = false) Segment segment,
            @RequestParam(required = false) StatutOffre statut,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int taille,
            @RequestParam(defaultValue = "dateCreation,desc") String tri
    ) {
        String[] triParts = tri.split(",");
        Sort sort = Sort.by(
                triParts.length > 1 && triParts[1].equalsIgnoreCase("asc")
                        ? Sort.Direction.ASC : Sort.Direction.DESC,
                triParts[0]
        );
        Pageable pageable = PageRequest.of(page, taille, sort);

        return ResponseEntity.ok(offreService.listerOffres(segment, statut, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OffreResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(offreService.getOffre(id));
    }

    @PostMapping
    public ResponseEntity<OffreResponse> creer(@Valid @RequestBody OffreRequest request) {
        return ResponseEntity.ok(offreService.creerOffre(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OffreResponse> modifier(@PathVariable Long id, @Valid @RequestBody OffreRequest request) {
        return ResponseEntity.ok(offreService.modifierOffre(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        offreService.supprimerOffre(id);
        return ResponseEntity.noContent().build();
    }
}