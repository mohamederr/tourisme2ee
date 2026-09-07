package com.example.tourisme2e.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ContactMessageResponse {
    private Long id;
    private String nom;
    private String email;
    private String telephone;
    private String sujet;
    private String message;
    private Boolean traite;
    private LocalDateTime dateEnvoi;
}
