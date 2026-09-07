package com.example.tourisme2e.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Pension {
    COMPLETE,
    DEMI_PENSION,
    PETIT_DEJEUNER;

    @JsonCreator
    public static Pension fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        String normalized = value.trim().toUpperCase()
                .replace(" ", "_")
                .replace("-", "_")
                .replace("É", "E")
                .replace("È", "E")
                .replace("'", "_");
        for (Pension p : values()) {
            if (p.name().equals(normalized)) {
                return p;
            }
        }
        if (normalized.contains("COMPLETE")) {
            return COMPLETE;
        }
        if (normalized.contains("DEMI")) {
            return DEMI_PENSION;
        }
        if (normalized.contains("PETIT") || normalized.contains("DEJEUNER")) {
            return PETIT_DEJEUNER;
        }
        return Pension.valueOf(normalized);
    }
}