package com.example.tourisme2e.controller;

import com.example.tourisme2e.dto.ChangerStatutRequest;
import com.example.tourisme2e.dto.DemandeDevisRequest;
import com.example.tourisme2e.dto.DemandeDevisResponse;
import com.example.tourisme2e.dto.PageResponse;
import com.example.tourisme2e.entity.StatutDevis;
import com.example.tourisme2e.entity.Utilisateur;
import com.example.tourisme2e.service.DemandeDevisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/demandes-devis")
@RequiredArgsConstructor
public class DemandeDevisController {

    private final DemandeDevisService demandeDevisService;

    @PostMapping
    public ResponseEntity<DemandeDevisResponse> creer(
            @Valid @RequestBody DemandeDevisRequest request,
            @AuthenticationPrincipal Utilisateur utilisateur
    ) {
        return ResponseEntity.ok(demandeDevisService.creer(request, utilisateur));
    }

    @GetMapping
    public ResponseEntity<PageResponse<DemandeDevisResponse>> listerToutes(
            @RequestParam(required = false) StatutDevis statut,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int taille
    ) {
        Pageable pageable = PageRequest.of(page, taille);
        return ResponseEntity.ok(new PageResponse<>(demandeDevisService.listerToutes(statut, pageable)));
    }

    @GetMapping("/mes-demandes")
    public ResponseEntity<PageResponse<DemandeDevisResponse>> mesDemandes(
            @AuthenticationPrincipal Utilisateur utilisateur,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int taille
    ) {
        Pageable pageable = PageRequest.of(page, taille);
        return ResponseEntity.ok(new PageResponse<>(demandeDevisService.mesDemandes(utilisateur, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DemandeDevisResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(demandeDevisService.getOne(id));
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<DemandeDevisResponse> changerStatut(
            @PathVariable Long id,
            @Valid @RequestBody ChangerStatutRequest request
    ) {
        return ResponseEntity.ok(demandeDevisService.changerStatut(id, request.getStatut()));
    }
}