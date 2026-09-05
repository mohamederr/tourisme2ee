package com.example.tourisme2e.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ParticipantPublicResponse {
    private Long id;
    private String profilPublic; // ex: "M.L. | 52 ans | Femme"
}