package com.example.tourisme2e.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class GenererDevisRequest {
    private BigDecimal montantHebergement;
    private BigDecimal montantRestauration;
    private BigDecimal montantTransport;
    private BigDecimal montantServices;
    private BigDecimal montantReductions;
    private String devisPdfUrl;
}
