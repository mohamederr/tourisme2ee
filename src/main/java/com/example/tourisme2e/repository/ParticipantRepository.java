package com.example.tourisme2e.repository;

import com.example.tourisme2e.entity.Groupe;
import com.example.tourisme2e.entity.Participant;
import com.example.tourisme2e.entity.StatutParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByGroupe(Groupe groupe);
    long countByGroupeAndStatut(Groupe groupe, StatutParticipant statut);
    @Query("SELECT COALESCE(SUM(p.montantAcompte), 0) FROM Participant p " +
            "WHERE p.statut = 'CONFIRME' AND p.dateInscription >= :debutMois")
    BigDecimal sommeAcomptesDuMois(@Param("debutMois") LocalDateTime debutMois);
}