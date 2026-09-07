package com.example.tourisme2e.service;

import com.example.tourisme2e.entity.Groupe;
import com.example.tourisme2e.entity.Utilisateur;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service de notification par email pour Tourisme 2E.
 *
 * En production, ce service doit être connecté à un serveur SMTP via
 * spring-boot-starter-mail (JavaMailSender). En développement ou lorsque
 * SMTP n'est pas configuré, toutes les notifications sont journalisées dans
 * les logs pour un débogage transparent sans erreur.
 *
 * Pour activer l'envoi réel, ajouter dans application.properties :
 *   spring.mail.host=smtp.example.com
 *   spring.mail.port=587
 *   spring.mail.username=...
 *   spring.mail.password=...
 */
@Service
@RequiredArgsConstructor
public class EmailNotificationService {

    private static final Logger log = LoggerFactory.getLogger(EmailNotificationService.class);

    /**
     * Notifie l'admin qu'une nouvelle demande de groupe fermé a été créée.
     * §3 Étape 5 du cahier des charges.
     */
    public void notifierNouvelleDemandeGroupeFerme(Groupe groupe, Utilisateur responsable) {
        log.info("[EMAIL] → Admin | NOUVELLE DEMANDE GROUPE FERMÉ | Groupe: '{}' | Responsable: {} {} <{}>  | Participants: {} | Dates: {} → {}",
                groupe.getTitre(),
                responsable.getPrenom(),
                responsable.getNom(),
                responsable.getEmail(),
                groupe.getCapaciteMin(),
                groupe.getDateDebut(),
                groupe.getDateFin()
        );
        // TODO : Implémenter avec JavaMailSender pour envoi réel en production
    }

    /**
     * Envoie le devis PDF par email au responsable du groupe.
     * §11 Étape 5 du cahier des charges.
     */
    public void envoyerDevisParEmail(Groupe groupe) {
        String emailResponsable = groupe.getCreateur().getEmail();
        log.info("[EMAIL] → {} | DEVIS ENVOYÉ | Devis N° {} | Groupe: '{}' | Total TTC: {} MAD | Acompte 10%: {} MAD | Solde 90% avant: {}",
                emailResponsable,
                groupe.getNumeroDevis(),
                groupe.getTitre(),
                groupe.getMontantTotalDevis(),
                groupe.getAcompteDevis(),
                groupe.getDateLimiteSolde()
        );
        // TODO : Implémenter avec JavaMailSender en attachant le PDF généré par DevisPdfService
    }

    /**
     * Notifie l'admin que le responsable du groupe a confirmé/validé son devis.
     */
    public void notifierDevisConfirmeParClient(Groupe groupe) {
        log.info("[EMAIL] → Admin | DEVIS CONFIRMÉ PAR CLIENT | Devis N° {} | Groupe: '{}' | Responsable: {} <{}>",
                groupe.getNumeroDevis(),
                groupe.getTitre(),
                groupe.getCreateur().getPrenom() + " " + groupe.getCreateur().getNom(),
                groupe.getCreateur().getEmail()
        );
        // TODO : Implémenter avec JavaMailSender
    }

    /**
     * Envoie un email de confirmation d'inscription au participant.
     * §8 Étape 3 du cahier des charges.
     */
    public void confirmerInscriptionParticipant(String emailParticipant, String prenomParticipant,
                                                String titreGroupe, java.math.BigDecimal montantAcompte) {
        log.info("[EMAIL] → {} | CONFIRMATION INSCRIPTION | Groupe: '{}' | Acompte versé: {} MAD | Profil visible: en attente de confirmation admin",
                emailParticipant,
                titreGroupe,
                montantAcompte
        );
        // TODO : Implémenter avec JavaMailSender
    }

    /**
     * Notifie tous les participants confirmés que le groupe a atteint le seuil de 10 personnes.
     * §8 Étape 5 du cahier des charges.
     */
    public void notifierGroupeComplet(Groupe groupe, java.util.List<String> emailsParticipants) {
        log.info("[EMAIL] → {} participants | GROUPE ATTEINT SEUIL 10 PERSONNES | Groupe: '{}' | Statut: ACTIF | Réductions automatiques appliquées",
                emailsParticipants.size(),
                groupe.getTitre()
        );
        // TODO : Implémenter avec JavaMailSender — envoyer le devis à chaque participant
    }

    /**
     * Notifie l'admin qu'un participant a payé son acompte.
     */
    public void notifierPaiementAcompte(String emailParticipant, String titreGroupe,
                                        java.math.BigDecimal montant, String modePaiement) {
        log.info("[EMAIL] → Admin | ACOMPTE REÇU | Participant: {} | Groupe: '{}' | Montant: {} MAD | Mode: {}",
                emailParticipant, titreGroupe, montant, modePaiement
        );
        // TODO : Implémenter avec JavaMailSender
    }
}
