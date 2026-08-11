package com.example.tourisme2e.dto;

import com.example.tourisme2e.entity.StatutPavillon;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PavillonRequest {

    @NotBlank
    private String nom;

    @Positive
    private Integer capaciteMin;

    @Positive
    private Integer capaciteMax;

    @NotNull
    private StatutPavillon statut;
}
