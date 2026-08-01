package com.example.tourisme2e.repository;


import com.example.tourisme2e.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}