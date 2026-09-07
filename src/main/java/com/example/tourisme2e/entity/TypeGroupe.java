package com.example.tourisme2e.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TypeGroupe {
    FERME,
    OUVERT;

    @JsonCreator
    public static TypeGroupe fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        String normalized = value.trim().toUpperCase()
                .replace(" ", "_")
                .replace("-", "_")
                .replace("É", "E")
                .replace("È", "E");
        if (normalized.contains("FERME")) {
            return FERME;
        }
        if (normalized.contains("OUVERT")) {
            return OUVERT;
        }
        return TypeGroupe.valueOf(normalized);
    }
}