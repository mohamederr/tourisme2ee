package com.example.tourisme2e.service;

import com.example.tourisme2e.dto.OffreRequest;
import com.example.tourisme2e.dto.OffreResponse;
import com.example.tourisme2e.entity.*;
import com.example.tourisme2e.mapper.OffreMapper;
import com.example.tourisme2e.repository.HotelCentreRepository;
import com.example.tourisme2e.repository.OffreRepository;
import com.example.tourisme2e.repository.SiteTouristiqueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OffreServiceTest {

    @Mock
    private OffreRepository offreRepository;

    @Mock
    private HotelCentreRepository hotelCentreRepository;

    @Mock
    private SiteTouristiqueRepository siteTouristiqueRepository;

    @Spy
    private OffreMapper offreMapper;

    @InjectMocks
    private OffreService offreService;

    private HotelCentre hotel;
    private SiteTouristique site1;
    private SiteTouristique site2;

    @BeforeEach
    void setUp() {
        hotel = new HotelCentre();
        hotel.setId(1L);
        hotel.setNom("Atlas Resort & Spa");

        site1 = new SiteTouristique();
        site1.setId(10L);
        site1.setNom("Jardin Majorelle");
        site1.setCategorie(CategorieSite.VILLE);

        site2 = new SiteTouristique();
        site2.setId(20L);
        site2.setNom("Palais Bahia");
        site2.setCategorie(CategorieSite.MONTAGNE);
    }

    @Test
    @DisplayName("Admin peut créer une offre complète avec tous les champs requis")
    void testCreerOffreCompleteSucces() {
        OffreRequest request = new OffreRequest();
        request.setTitre("Séjour Merveilles de Marrakech");
        request.setDescriptionCourte("Un court séjour inoubliable");
        request.setDescriptionLongue("Description détaillée du programme jour par jour à Marrakech...");
        request.setTypeGroupe(TypeGroupe.OUVERT);
        request.setDateDebut(LocalDate.now().plusDays(10));
        request.setDateFin(LocalDate.now().plusDays(17));
        request.setCapaciteMin(10);
        request.setCapaciteMax(20);
        request.setSiteTouristiqueIds(List.of(10L, 20L));
        request.setActivitesIncluses("Visite guidée, Promenade en calèche, Dîner spectacle");
        request.setHotelId(1L);
        request.setNiveauConfort(4);
        request.setPension(Pension.COMPLETE);
        request.setPrixBase(new BigDecimal("4200.00"));
        request.setServicesAdditionnels("Transfert aéroport VIP, Guide polyglotte");
        request.setStatut(StatutOffre.ACTIF);

        when(hotelCentreRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(siteTouristiqueRepository.findAllById(List.of(10L, 20L))).thenReturn(List.of(site1, site2));
        when(offreRepository.save(any(Offre.class))).thenAnswer(invocation -> {
            Offre o = invocation.getArgument(0);
            o.setId(100L);
            return o;
        });

        OffreResponse response = offreService.creerOffre(request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(100L);
        assertThat(response.getTitre()).isEqualTo("Séjour Merveilles de Marrakech");
        assertThat(response.getDescriptionCourte()).isEqualTo("Un court séjour inoubliable");
        assertThat(response.getDescriptionLongue()).startsWith("Description détaillée");
        assertThat(response.getTypeGroupe()).isEqualTo(TypeGroupe.OUVERT);
        assertThat(response.getCapaciteMin()).isEqualTo(10);
        assertThat(response.getCapaciteMax()).isEqualTo(20);
        assertThat(response.getHotelId()).isEqualTo(1L);
        assertThat(response.getHotelNom()).isEqualTo("Atlas Resort & Spa");
        assertThat(response.getNiveauConfort()).isEqualTo(4);
        assertThat(response.getPension()).isEqualTo(Pension.COMPLETE);
        assertThat(response.getPrixBase()).isEqualByComparingTo("4200.00");
        assertThat(response.getStatut()).isEqualTo(StatutOffre.ACTIF);
        assertThat(response.getSiteTouristiqueIds()).containsExactlyInAnyOrder(10L, 20L);
        assertThat(response.getSites()).hasSize(2);
        assertThat(response.getSitesTouristiques()).contains("Jardin Majorelle", "Palais Bahia");
        assertThat(response.getDuree()).isEqualTo(8); // du jour 10 au jour 17 inclus = 8 jours

        verify(offreRepository).save(any(Offre.class));
    }

    @Test
    @DisplayName("Validation : Capacité minimale inférieure à 10 rejetée")
    void testValidationCapaciteMinInvalide() {
        OffreRequest request = new OffreRequest();
        request.setTitre("Circuit Test");
        request.setCapaciteMin(8); // < 10
        request.setCapaciteMax(20);

        assertThatThrownBy(() -> offreService.creerOffre(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La capacité minimale doit être d'au moins 10");

        verify(offreRepository, never()).save(any());
    }

    @Test
    @DisplayName("Validation : Capacité maximale supérieure à 20 rejetée")
    void testValidationCapaciteMaxInvalide() {
        OffreRequest request = new OffreRequest();
        request.setTitre("Circuit Test");
        request.setCapaciteMin(10);
        request.setCapaciteMax(25); // > 20

        assertThatThrownBy(() -> offreService.creerOffre(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La capacité maximale ne peut pas dépasser 20");

        verify(offreRepository, never()).save(any());
    }

    @Test
    @DisplayName("Validation : Capacité min supérieure à capacité max rejetée")
    void testValidationCapaciteMinSuperieureMax() {
        OffreRequest request = new OffreRequest();
        request.setTitre("Circuit Test");
        request.setCapaciteMin(18);
        request.setCapaciteMax(12);

        assertThatThrownBy(() -> offreService.creerOffre(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ne peut pas dépasser la capacité maximale");

        verify(offreRepository, never()).save(any());
    }

    @Test
    @DisplayName("Validation : Date fin antérieure à date début rejetée")
    void testValidationDateFinAnterieure() {
        OffreRequest request = new OffreRequest();
        request.setTitre("Circuit Test");
        request.setDateDebut(LocalDate.now().plusDays(10));
        request.setDateFin(LocalDate.now().plusDays(5)); // fin avant début

        assertThatThrownBy(() -> offreService.creerOffre(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("La date de fin ne peut pas être antérieure à la date de début");

        verify(offreRepository, never()).save(any());
    }

    @Test
    @DisplayName("Validation : Niveau de confort invalide rejeté")
    void testValidationNiveauConfortInvalide() {
        OffreRequest request = new OffreRequest();
        request.setTitre("Circuit Test");
        request.setNiveauConfort(6); // > 5

        assertThatThrownBy(() -> offreService.creerOffre(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Le niveau de confort doit être compris entre 2 et 5 étoiles");

        verify(offreRepository, never()).save(any());
    }

    @Test
    @DisplayName("Modification du statut de l'offre")
    void testChangerStatut() {
        Offre offre = new Offre();
        offre.setId(50L);
        offre.setTitre("Offre en préparation");
        offre.setStatut(StatutOffre.BROUILLON);

        when(offreRepository.findById(50L)).thenReturn(Optional.of(offre));
        when(offreRepository.save(any(Offre.class))).thenReturn(offre);

        OffreResponse response = offreService.changerStatut(50L, StatutOffre.ACTIF);

        assertThat(response.getStatut()).isEqualTo(StatutOffre.ACTIF);
        assertThat(offre.getStatut()).isEqualTo(StatutOffre.ACTIF);
        verify(offreRepository).save(offre);
    }
}
