package com.example.tourisme2e.repository;

import com.example.tourisme2e.entity.NewsletterAbonnement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NewsletterAbonnementRepository extends JpaRepository<NewsletterAbonnement, Long> {
    Optional<NewsletterAbonnement> findByEmail(String email);
    boolean existsByEmail(String email);
}
