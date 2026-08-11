package com.example.tourisme2e.dto;

import com.example.tourisme2e.entity.StatutDevis;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangerStatutRequest {

    @NotNull
    private StatutDevis statut;
}