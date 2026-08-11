package com.example.tourisme2e.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DisponibiliteRequest {

    @NotNull
    private Long offreId;

    @NotNull
    private Long pavillonId;

    @NotNull
    private LocalDate dateDebut;

    @NotNull
    private LocalDate dateFin;

    @Positive
    @NotNull
    private Integer placesTotales;
}