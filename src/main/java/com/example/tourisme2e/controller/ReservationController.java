package com.example.tourisme2e.controller;

import com.example.tourisme2e.dto.ConfirmerReservationRequest;
import com.example.tourisme2e.dto.ReservationResponse;
import com.example.tourisme2e.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> confirmer(@Valid @RequestBody ConfirmerReservationRequest request,
                                                         @RequestParam Long demandeDevisId) {
        return ResponseEntity.ok(reservationService.confirmer(demandeDevisId, request.getDisponibiliteId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getOne(id));
    }
}