package com.example.tourisme2e.controller;

import com.example.tourisme2e.dto.ParticipantAdminResponse;
import com.example.tourisme2e.dto.PageResponse;
import com.example.tourisme2e.dto.ParticipantPublicResponse;
import com.example.tourisme2e.dto.RejoindreGroupeRequest;
import com.example.tourisme2e.entity.StatutParticipant;
import com.example.tourisme2e.service.ParticipantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantService participantService;

    @PostMapping("/groupes/{groupeId}/participants")
    public ResponseEntity<ParticipantAdminResponse> rejoindre(
            @PathVariable Long groupeId,
            @Valid @RequestBody RejoindreGroupeRequest request
    ) {
        return ResponseEntity.ok(participantService.rejoindreGroupe(groupeId, request));
    }

    @GetMapping("/groupes/{groupeId}/participants")
    public ResponseEntity<List<ParticipantPublicResponse>> listerPublics(@PathVariable Long groupeId) {
        return ResponseEntity.ok(participantService.listerProfilsPublics(groupeId));
    }

    @GetMapping("/groupes/{groupeId}/participants/admin")
    public ResponseEntity<List<ParticipantAdminResponse>> listerAdmin(@PathVariable Long groupeId) {
        return ResponseEntity.ok(participantService.listerTousAdmin(groupeId));
    }

    @PatchMapping("/participants/{id}/confirmer")
    public ResponseEntity<ParticipantAdminResponse> confirmer(@PathVariable Long id) {
        return ResponseEntity.ok(participantService.confirmerParticipant(id));
    }

    @PatchMapping("/participants/{id}/refuser")
    public ResponseEntity<ParticipantAdminResponse> refuser(@PathVariable Long id) {
        return ResponseEntity.ok(participantService.refuserParticipant(id));
    }

    @GetMapping("/participants/admin")
    public ResponseEntity<PageResponse<ParticipantAdminResponse>> rechercherAdmin(
            @RequestParam(required = false) Long groupeId,
            @RequestParam(required = false) StatutParticipant statut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int taille
    ) {
        Pageable pageable = PageRequest.of(page, taille);
        return ResponseEntity.ok(new PageResponse<>(participantService.rechercherAdmin(groupeId, statut, debut, fin, q, pageable)));
    }

    @GetMapping("/participants/admin/export.csv")
    public ResponseEntity<String> exporterCsv(
            @RequestParam(required = false) Long groupeId,
            @RequestParam(required = false) StatutParticipant statut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin,
            @RequestParam(required = false) String q
    ) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=inscriptions.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(participantService.exporterCsv(groupeId, statut, debut, fin, q));
    }

    @DeleteMapping("/participants/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        participantService.supprimerParticipant(id);
        return ResponseEntity.noContent().build();
    }
}
