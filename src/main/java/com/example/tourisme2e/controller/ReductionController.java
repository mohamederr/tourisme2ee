package com.example.tourisme2e.controller;

import com.example.tourisme2e.dto.ReductionResponse;
import com.example.tourisme2e.service.ReductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/reductions")
@RequiredArgsConstructor
public class ReductionController {

    private final ReductionService reductionService;

    @GetMapping("/calculer")
    public ResponseEntity<ReductionResponse> calculer(
            @RequestParam BigDecimal prixBase,
            @RequestParam int nbParticipants,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateReservation,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebutSejour
    ) {
        return ResponseEntity.ok(reductionService.calculer(prixBase, nbParticipants, dateReservation, dateDebutSejour));
    }
}