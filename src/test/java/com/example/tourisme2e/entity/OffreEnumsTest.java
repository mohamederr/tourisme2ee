package com.example.tourisme2e.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OffreEnumsTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("TypeGroupe : support de FERME, OUVERT, GROUPE_FERME, GROUPE_OUVERT")
    void testTypeGroupeJson() throws Exception {
        assertThat(objectMapper.readValue("\"GROUPE_FERME\"", TypeGroupe.class)).isEqualTo(TypeGroupe.FERME);
        assertThat(objectMapper.readValue("\"GROUPE_OUVERT\"", TypeGroupe.class)).isEqualTo(TypeGroupe.OUVERT);
        assertThat(objectMapper.readValue("\"FERME\"", TypeGroupe.class)).isEqualTo(TypeGroupe.FERME);
        assertThat(objectMapper.readValue("\"OUVERT\"", TypeGroupe.class)).isEqualTo(TypeGroupe.OUVERT);
        assertThat(objectMapper.readValue("\"Groupe Fermé\"", TypeGroupe.class)).isEqualTo(TypeGroupe.FERME);
        assertThat(objectMapper.readValue("\"Groupe Ouvert\"", TypeGroupe.class)).isEqualTo(TypeGroupe.OUVERT);
    }

    @Test
    @DisplayName("Pension : support de COMPLETE, DEMI_PENSION, PETIT_DEJEUNER avec tolérance")
    void testPensionJson() throws Exception {
        assertThat(objectMapper.readValue("\"COMPLETE\"", Pension.class)).isEqualTo(Pension.COMPLETE);
        assertThat(objectMapper.readValue("\"Complete\"", Pension.class)).isEqualTo(Pension.COMPLETE);
        assertThat(objectMapper.readValue("\"DEMI_PENSION\"", Pension.class)).isEqualTo(Pension.DEMI_PENSION);
        assertThat(objectMapper.readValue("\"Demi-pension\"", Pension.class)).isEqualTo(Pension.DEMI_PENSION);
        assertThat(objectMapper.readValue("\"PETIT_DEJEUNER\"", Pension.class)).isEqualTo(Pension.PETIT_DEJEUNER);
        assertThat(objectMapper.readValue("\"Petit-déjeuner\"", Pension.class)).isEqualTo(Pension.PETIT_DEJEUNER);
    }

    @Test
    @DisplayName("StatutOffre : support de BROUILLON, ACTIF, COMPLET, ARCHIVE")
    void testStatutOffreJson() throws Exception {
        assertThat(objectMapper.readValue("\"brouillon\"", StatutOffre.class)).isEqualTo(StatutOffre.BROUILLON);
        assertThat(objectMapper.readValue("\"ACTIF\"", StatutOffre.class)).isEqualTo(StatutOffre.ACTIF);
        assertThat(objectMapper.readValue("\"complet\"", StatutOffre.class)).isEqualTo(StatutOffre.COMPLET);
        assertThat(objectMapper.readValue("\"ARCHIVE\"", StatutOffre.class)).isEqualTo(StatutOffre.ARCHIVE);
    }
}
