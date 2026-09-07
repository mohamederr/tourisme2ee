package com.example.tourisme2e.dto;

import com.example.tourisme2e.entity.RoleAdmin;
import com.example.tourisme2e.entity.TypeProfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminRequest {
    @NotBlank
    private String nom;
    @NotBlank
    private String prenom;
    @Email
    @NotBlank
    private String email;
    private String motDePasse;
    @NotNull
    private RoleAdmin roleAdmin;
    private TypeProfil typeProfil;
    private Boolean actif;
}
