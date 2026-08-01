package com.example.tourisme2e.repository;


import com.example.tourisme2e.entity.Disponibilite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DisponibiliteRepository extends JpaRepository<Disponibilite, Long> {

    @Query("SELECT d FROM Disponibilite d WHERE d.offre.id = :offreId " +
            "AND d.dateDebut <= :dateFin AND d.dateFin >= :dateDebut")
    List<Disponibilite> rechercherCreneaux(
            @Param("offreId") Long offreId,
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin
    );
}
