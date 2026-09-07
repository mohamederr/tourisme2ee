package com.example.tourisme2e.repository;

import com.example.tourisme2e.entity.ContactMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
    Page<ContactMessage> findByTraite(Boolean traite, Pageable pageable);
    List<ContactMessage> findByTraiteOrderByDateEnvoiDesc(Boolean traite);
}
