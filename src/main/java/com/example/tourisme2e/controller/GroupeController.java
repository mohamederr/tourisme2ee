package com.example.tourisme2e.controller;

import com.example.tourisme2e.dto.*;
import com.example.tourisme2e.entity.Utilisateur;
import com.example.tourisme2e.service.GroupeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groupes")
@RequiredArgsConstructor
public class GroupeController {

    private final GroupeService groupeService;

    @PostMapping("/fermes")
    public ResponseEntity<GroupeResponse> creerFerme(
            @Valid @RequestBody CreerGroupeFermeRequest request,
            @AuthenticationPrincipal Utilisateur utilisateur
    ) {
        return ResponseEntity.ok(groupeService.creerGroupeFerme(request, utilisateur));
    }

    @PostMapping("/ouverts")
    public ResponseEntity<GroupeResponse> creerOuvert(
            @Valid @RequestBody CreerGroupeOuvertRequest request,
            @AuthenticationPrincipal Utilisateur utilisateur
    ) {
        return ResponseEntity.ok(groupeService.creerGroupeOuvert(request, utilisateur));
    }

    @GetMapping("/ouverts")
    public ResponseEntity<PageResponse<GroupeResponse>> listerOuvertsPublics(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int taille
    ) {
        Pageable pageable = PageRequest.of(page, taille);
        return ResponseEntity.ok(new PageResponse<>(groupeService.listerGroupesOuvertsPublics(pageable)));
    }

    @GetMapping
    public ResponseEntity<PageResponse<GroupeResponse>> listerTous(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int taille
    ) {
        Pageable pageable = PageRequest.of(page, taille);
        return ResponseEntity.ok(new PageResponse<>(groupeService.listerTous(pageable)));
    }

    @GetMapping("/demandes-validation")
    public ResponseEntity<PageResponse<GroupeResponse>> listerDemandesValidation(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int taille
    ) {
        Pageable pageable = PageRequest.of(page, taille);
        return ResponseEntity.ok(new PageResponse<>(groupeService.listerDemandesValidation(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupeResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(groupeService.getGroupe(id));
    }

    @PatchMapping("/{id}/valider")
    public ResponseEntity<GroupeResponse> valider(@PathVariable Long id, @Valid @RequestBody ValiderGroupeRequest request) {
        return ResponseEntity.ok(groupeService.validerGroupe(id, request));
    }

    @PostMapping("/{id}/devis/generer")
    public ResponseEntity<GroupeResponse> genererDevis(@PathVariable Long id, @Valid @RequestBody GenererDevisRequest request) {
        return ResponseEntity.ok(groupeService.genererDevis(id, request));
    }

    @GetMapping("/{id}/devis.pdf")
    public ResponseEntity<byte[]> telechargerDevis(@PathVariable Long id) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=devis-groupe-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(groupeService.telechargerDevis(id));
    }
}
