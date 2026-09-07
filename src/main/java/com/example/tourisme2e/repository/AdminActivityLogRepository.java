package com.example.tourisme2e.repository;

import com.example.tourisme2e.entity.AdminActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminActivityLogRepository extends JpaRepository<AdminActivityLog, Long> {
}
