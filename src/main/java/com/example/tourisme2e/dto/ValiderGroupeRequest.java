package com.example.tourisme2e.dto;

import com.example.tourisme2e.entity.ActionValidationGroupe;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ValiderGroupeRequest {

    @NotNull
    private ActionValidationGroupe action;

    private String commentaire;

    private BigDecimal prixBase;
}
