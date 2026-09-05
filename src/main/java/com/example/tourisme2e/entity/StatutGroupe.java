package com.example.tourisme2e.entity;

public enum StatutGroupe {
    BROUILLON,
    EN_ATTENTE_VALIDATION, // pour un groupe ouvert créé par un client, avant approbation admin
    ACTIF,
    EN_FORMATION,          // spécifique groupe ouvert : encore sous 10 participants
    COMPLET,
    ARCHIVE
}