package com.example.tourisme2e.service;

import com.example.tourisme2e.dto.AuthResponse;
import com.example.tourisme2e.dto.LoginRequest;
import com.example.tourisme2e.dto.RegisterRequest;
import com.example.tourisme2e.entity.Role;
import com.example.tourisme2e.entity.Utilisateur;
import com.example.tourisme2e.repository.UtilisateurRepository;
import com.example.tourisme2e.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Un compte existe déjà avec cet email");
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(request.getNom());
        utilisateur.setPrenom(request.getPrenom());
        utilisateur.setEmail(request.getEmail());
        utilisateur.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        utilisateur.setTypeProfil(request.getTypeProfil());
        utilisateur.setOrganisation(request.getOrganisation());
        utilisateur.setPays(request.getPays());
        utilisateur.setRole(Role.CLIENT); // jamais laissé au choix du client, toujours CLIENT par défaut

        utilisateurRepository.save(utilisateur);

        String token = jwtService.generateToken(utilisateur);
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getMotDePasse())
        );

        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        String token = jwtService.generateToken(utilisateur);
        return new AuthResponse(token);
    }
}
