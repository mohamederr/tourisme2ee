package com.example.tourisme2e.repository;

import com.example.tourisme2e.entity.Groupe;
import com.example.tourisme2e.entity.Participant;
import com.example.tourisme2e.entity.StatutParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("""
            SELECT p FROM Participant p
            WHERE (:groupeId IS NULL OR p.groupe.id = :groupeId)
              AND (:statut IS NULL OR p.statut = :statut)
              AND (:debut IS NULL OR p.dateInscription >= :debut)
              AND (:fin IS NULL OR p.dateInscription <= :fin)
              AND (:q IS NULL OR LOWER(CONCAT(p.nom, ' ', p.prenom, ' ', p.email, ' ', COALESCE(p.telephone, ''), ' ', p.pays)) LIKE LOWER(CONCAT('%', :q, '%')))
            """)
    Page<Participant> rechercherAdmin(@Param("groupeId") Long groupeId,
                                      @Param("statut") StatutParticipant statut,
                                      @Param("debut") LocalDateTime debut,
                                      @Param("fin") LocalDateTime fin,
                                      @Param("q") String q,
                                      Pageable pageable);

    @Query("""
            SELECT p FROM Participant p
            WHERE (:groupeId IS NULL OR p.groupe.id = :groupeId)
              AND (:statut IS NULL OR p.statut = :statut)
              AND (:debut IS NULL OR p.dateInscription >= :debut)
              AND (:fin IS NULL OR p.dateInscription <= :fin)
              AND (:q IS NULL OR LOWER(CONCAT(p.nom, ' ', p.prenom, ' ', p.email, ' ', COALESCE(p.telephone, ''), ' ', p.pays)) LIKE LOWER(CONCAT('%', :q, '%')))
            """)
    List<Participant> rechercherAdminExport(@Param("groupeId") Long groupeId,
                                            @Param("statut") StatutParticipant statut,
                                            @Param("debut") LocalDateTime debut,
                                            @Param("fin") LocalDateTime fin,
                                            @Param("q") String q);
}
