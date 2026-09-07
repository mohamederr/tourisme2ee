package com.example.tourisme2e.dto;

import com.example.tourisme2e.entity.RoleAdmin;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminResponse {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private RoleAdmin roleAdmin;
    private Boolean actif;
}
