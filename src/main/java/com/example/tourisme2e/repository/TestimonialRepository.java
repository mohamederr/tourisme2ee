package com.example.tourisme2e.repository;

import com.example.tourisme2e.entity.StatutModeration;
import com.example.tourisme2e.entity.Testimonial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestimonialRepository extends JpaRepository<Testimonial, Long> {
    List<Testimonial> findByStatutAndAfficherAccueil(StatutModeration statut, Boolean afficherAccueil);
}
