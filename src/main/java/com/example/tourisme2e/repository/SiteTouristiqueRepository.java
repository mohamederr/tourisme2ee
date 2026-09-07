package com.example.tourisme2e.repository;

import com.example.tourisme2e.entity.CategorieSite;
import com.example.tourisme2e.entity.SiteTouristique;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SiteTouristiqueRepository extends JpaRepository<SiteTouristique, Long> {
    List<SiteTouristique> findByCategorie(CategorieSite categorie);
}
