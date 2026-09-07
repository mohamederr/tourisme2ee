package com.example.tourisme2e.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum StatutOffre {
    BROUILLON,
    ACTIF,
    COMPLET,
    ARCHIVE,
    INACTIF;

    @JsonCreator
    public static StatutOffre fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        String normalized = value.trim().toUpperCase();
        for (StatutOffre s : values()) {
            if (s.name().equals(normalized)) {
                return s;
            }
        }
        return StatutOffre.valueOf(normalized);
    }
}
