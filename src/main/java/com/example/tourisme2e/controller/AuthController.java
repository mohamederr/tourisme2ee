package com.example.tourisme2e.controller;

import com.example.tourisme2e.dto.AuthResponse;
import com.example.tourisme2e.dto.LoginRequest;
import com.example.tourisme2e.dto.RegisterRequest;
import com.example.tourisme2e.dto.UtilisateurResponse;
import com.example.tourisme2e.entity.Utilisateur;
import com.example.tourisme2e.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UtilisateurResponse> me(@AuthenticationPrincipal Utilisateur utilisateur) {
        UtilisateurResponse response = new UtilisateurResponse(
                utilisateur.getId(),
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                utilisateur.getEmail(),
                utilisateur.getRole(),
                utilisateur.getTypeProfil(),
                utilisateur.getOrganisation()
        );
        return ResponseEntity.ok(response);
    }
}