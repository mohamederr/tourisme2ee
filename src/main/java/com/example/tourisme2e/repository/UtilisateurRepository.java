package com.example.tourisme2e.repository;


import com.example.tourisme2e.entity.Utilisateur;
import com.example.tourisme2e.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByEmail(String email);
    List<Utilisateur> findByRole(Role role);
    boolean existsByEmail(String email);
    long countByDateInscriptionAfter(java.time.LocalDateTime date);
}
