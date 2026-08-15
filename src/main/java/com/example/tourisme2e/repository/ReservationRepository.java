package com.example.tourisme2e.repository;


import com.example.tourisme2e.entity.Reservation;
import com.example.tourisme2e.entity.StatutReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("SELECT r FROM Reservation r " +
            "JOIN r.demandeDevis d " +
            "WHERE r.statut = :statut AND d.dateSouhaitee >= CURRENT_DATE " +
            "ORDER BY d.dateSouhaitee ASC")
    List<Reservation> findProchainsSejoursConfirmes(StatutReservation statut);
}