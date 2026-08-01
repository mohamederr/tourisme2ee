package com.example.tourisme2e.repository;


import com.example.tourisme2e.entity.DemandeDevis;
import com.example.tourisme2e.entity.StatutDevis;
import com.example.tourisme2e.entity.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemandeDevisRepository extends JpaRepository<DemandeDevis, Long> {
    Page<DemandeDevis> findByUtilisateur(Utilisateur utilisateur, Pageable pageable);
    Page<DemandeDevis> findByStatut(StatutDevis statut, Pageable pageable);
}