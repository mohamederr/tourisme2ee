package com.example.tourisme2e.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmerReservationRequest {

    @NotNull
    private Long disponibiliteId;
}