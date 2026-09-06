package com.example.tourisme2e.repository;

import com.example.tourisme2e.entity.Groupe;
import com.example.tourisme2e.entity.StatutGroupe;
import com.example.tourisme2e.entity.TypeGroupe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupeRepository extends JpaRepository<Groupe, Long> {
    Page<Groupe> findByTypeGroupeAndStatutIn(TypeGroupe typeGroupe, java.util.List<StatutGroupe> statuts, Pageable pageable);
    Page<Groupe> findByTypeGroupe(TypeGroupe typeGroupe, Pageable pageable);
    long countByStatut(StatutGroupe statut);
    List<Groupe> findByStatutIn(List<StatutGroupe> statuts);

    List<Groupe> findByStatut(StatutGroupe statutGroupe);
}