package com.example.tourisme2e.repository;

import com.example.tourisme2e.entity.Offre;
import com.example.tourisme2e.entity.Segment;
import com.example.tourisme2e.entity.StatutOffre;
import com.example.tourisme2e.entity.TypeGroupe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OffreRepository extends JpaRepository<Offre, Long>, JpaSpecificationExecutor<Offre> {
    Page<Offre> findBySegment(Segment segment, Pageable pageable);
    Page<Offre> findByStatut(StatutOffre statut, Pageable pageable);
    Page<Offre> findBySegmentAndStatut(Segment segment, StatutOffre statut, Pageable pageable);
    Page<Offre> findByTypeGroupe(TypeGroupe typeGroupe, Pageable pageable);
    Page<Offre> findByTypeGroupeAndStatut(TypeGroupe typeGroupe, StatutOffre statut, Pageable pageable);
}
