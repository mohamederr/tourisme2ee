package com.example.tourisme2e.controller;

import com.example.tourisme2e.dto.DisponibiliteRequest;
import com.example.tourisme2e.dto.DisponibiliteResponse;
import com.example.tourisme2e.service.DisponibiliteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/disponibilites")
@RequiredArgsConstructor
public class DisponibiliteController {

    private final DisponibiliteService disponibiliteService;

    @GetMapping
    public ResponseEntity<List<DisponibiliteResponse>> rechercher(
            @RequestParam Long offreId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin
    ) {
        return ResponseEntity.ok(disponibiliteService.rechercher(offreId, dateDebut, dateFin));
    }

    @PostMapping
    public ResponseEntity<DisponibiliteResponse> creer(@Valid @RequestBody DisponibiliteRequest request) {
        return ResponseEntity.ok(disponibiliteService.creer(request));
    }
}
