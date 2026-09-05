package com.example.tourisme2e.repository;

import com.example.tourisme2e.entity.Groupe;
import com.example.tourisme2e.entity.Participant;
import com.example.tourisme2e.entity.StatutParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByGroupe(Groupe groupe);
    long countByGroupeAndStatut(Groupe groupe, StatutParticipant statut);
}