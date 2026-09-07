package com.example.tourisme2e.dto;

import com.example.tourisme2e.entity.Sexe;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaiementAcompteRequest {

    @NotBlank(message = "Le mode de paiement est obligatoire")
    private String modePaiement; // STRIPE, CARTE_BANCAIRE, ORANGE_MONEY, VIREMENT

    private String transactionId;

    private String numeroCarteMasque;

    private BigDecimal montant;
}
