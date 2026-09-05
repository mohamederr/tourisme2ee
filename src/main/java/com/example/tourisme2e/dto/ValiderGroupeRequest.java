package com.example.tourisme2e.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValiderGroupeRequest {

    @NotNull
    private boolean approuver; // true = APPROUVER, false = REFUSER

    private String commentaire;
}