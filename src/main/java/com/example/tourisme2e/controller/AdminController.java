package com.example.tourisme2e.controller;

import com.example.tourisme2e.dto.AdminRequest;
import com.example.tourisme2e.dto.AdminResponse;
import com.example.tourisme2e.dto.AdminActivityLogResponse;
import com.example.tourisme2e.dto.ChangerMotDePasseAdminRequest;
import com.example.tourisme2e.entity.Utilisateur;
import com.example.tourisme2e.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public List<AdminResponse> lister() {
        return adminService.listerAdmins();
    }

    @PostMapping
    public AdminResponse creer(@Valid @RequestBody AdminRequest request, @AuthenticationPrincipal Utilisateur acteur) {
        return adminService.creerAdmin(request, acteur);
    }

    @PutMapping("/{id}")
    public AdminResponse modifier(@PathVariable Long id, @Valid @RequestBody AdminRequest request,
                                  @AuthenticationPrincipal Utilisateur acteur) {
        return adminService.modifierAdmin(id, request, acteur);
    }

    @PatchMapping("/{id}/mot-de-passe")
    public AdminResponse changerMotDePasse(@PathVariable Long id,
                                           @Valid @RequestBody ChangerMotDePasseAdminRequest request,
                                           @AuthenticationPrincipal Utilisateur acteur) {
        return adminService.changerMotDePasse(id, request.getMotDePasse(), acteur);
    }

    @PatchMapping("/{id}/activation")
    public AdminResponse changerActivation(@PathVariable Long id, @RequestParam boolean actif,
                                           @AuthenticationPrincipal Utilisateur acteur) {
        return adminService.changerActivation(id, actif, acteur);
    }

    @GetMapping("/logs")
    public List<AdminActivityLogResponse> logs() {
        return adminService.listerLogs();
    }
}
