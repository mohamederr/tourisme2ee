package com.example.tourisme2e.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AdminActivityLogResponse {
    private Long id;
    private Long adminId;
    private String adminEmail;
    private String action;
    private String details;
    private LocalDateTime dateAction;
}
