package com.example.tourisme2e.dto;

import com.example.tourisme2e.entity.Role;
import com.example.tourisme2e.entity.TypeProfil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UtilisateurResponse {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private Role role;
    private TypeProfil typeProfil;
    private String organisation;
}