package com.example.tourisme2e.controller;

import com.example.tourisme2e.dto.ParticipantAdminResponse;
import com.example.tourisme2e.dto.ParticipantPublicResponse;
import com.example.tourisme2e.dto.RejoindreGroupeRequest;
import com.example.tourisme2e.service.ParticipantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}