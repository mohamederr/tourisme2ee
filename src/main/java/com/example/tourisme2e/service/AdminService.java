package com.example.tourisme2e.service;

import com.example.tourisme2e.dto.AdminRequest;
import com.example.tourisme2e.dto.AdminResponse;
import com.example.tourisme2e.dto.AdminActivityLogResponse;
import com.example.tourisme2e.entity.AdminActivityLog;
import com.example.tourisme2e.entity.Role;
import com.example.tourisme2e.entity.TypeProfil;
import com.example.tourisme2e.entity.Utilisateur;
import com.example.tourisme2e.exception.ResourceNotFoundException;
import com.example.tourisme2e.repository.AdminActivityLogRepository;
import com.example.tourisme2e.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UtilisateurRepository utilisateurRepository;
    private final AdminActivityLogRepository logRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<AdminResponse> listerAdmins() {
        return utilisateurRepository.findByRole(Role.ADMIN).stream().map(this::toResponse).toList();
    }

    @Transactional
    public AdminResponse creerAdmin(AdminRequest request, Utilisateur acteur) {
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Un compte existe deja avec cet email");
        }
        if (request.getMotDePasse() == null || request.getMotDePasse().isBlank()) {
            throw new IllegalArgumentException("Le mot de passe est obligatoire pour creer un admin");
        }
        Utilisateur admin = new Utilisateur();
        appliquer(admin, request);
        admin.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        admin.setRole(Role.ADMIN);
        utilisateurRepository.save(admin);
        log(acteur, "ADMIN_CREE", "Admin cree : " + admin.getEmail());
        return toResponse(admin);
    }

    @Transactional
    public AdminResponse modifierAdmin(Long id, AdminRequest request, Utilisateur acteur) {
        Utilisateur admin = getAdmin(id);
        appliquer(admin, request);
        utilisateurRepository.save(admin);
        log(acteur, "ADMIN_MODIFIE", "Admin modifie : " + admin.getEmail());
        return toResponse(admin);
    }

    @Transactional
    public AdminResponse changerMotDePasse(Long id, String motDePasse, Utilisateur acteur) {
        Utilisateur admin = getAdmin(id);
        admin.setMotDePasse(passwordEncoder.encode(motDePasse));
        utilisateurRepository.save(admin);
        log(acteur, "ADMIN_MOT_DE_PASSE", "Mot de passe admin modifie : " + admin.getEmail());
        return toResponse(admin);
    }

    @Transactional
    public AdminResponse changerActivation(Long id, boolean actif, Utilisateur acteur) {
        Utilisateur admin = getAdmin(id);
        admin.setActif(actif);
        utilisateurRepository.save(admin);
        log(acteur, actif ? "ADMIN_ACTIVE" : "ADMIN_SUSPENDU", admin.getEmail());
        return toResponse(admin);
    }

    @Transactional(readOnly = true)
    public List<AdminActivityLogResponse> listerLogs() {
        return logRepository.findAll().stream().map(this::toLogResponse).toList();
    }

    private Utilisateur getAdmin(Long id) {
        Utilisateur admin = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin non trouve : " + id));
        if (admin.getRole() != Role.ADMIN) {
            throw new ResourceNotFoundException("Admin non trouve : " + id);
        }
        return admin;
    }

    private void appliquer(Utilisateur admin, AdminRequest request) {
        admin.setNom(request.getNom());
        admin.setPrenom(request.getPrenom());
        admin.setEmail(request.getEmail());
        admin.setRoleAdmin(request.getRoleAdmin());
        admin.setTypeProfil(request.getTypeProfil() != null ? request.getTypeProfil() : TypeProfil.PARTICULIER);
        admin.setActif(request.getActif() == null || request.getActif());
    }

    private AdminResponse toResponse(Utilisateur u) {
        return new AdminResponse(u.getId(), u.getNom(), u.getPrenom(), u.getEmail(), u.getRoleAdmin(), u.getActif());
    }

    private AdminActivityLogResponse toLogResponse(AdminActivityLog log) {
        Utilisateur admin = log.getAdmin();
        return new AdminActivityLogResponse(
                log.getId(),
                admin != null ? admin.getId() : null,
                admin != null ? admin.getEmail() : null,
                log.getAction(),
                log.getDetails(),
                log.getDateAction()
        );
    }

    private void log(Utilisateur acteur, String action, String details) {
        AdminActivityLog log = new AdminActivityLog();
        log.setAdmin(acteur);
        log.setAction(action);
        log.setDetails(details);
        logRepository.save(log);
    }
}
