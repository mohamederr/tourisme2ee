package com.example.tourisme2e.dto;

import com.example.tourisme2e.entity.StatutReservation;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReservationResponse {
    private Long id;
    private Long demandeDevisId;
    private LocalDateTime dateConfirmation;
    private StatutReservation statut;
}