package com.example.tourisme2e.service;

import com.example.tourisme2e.entity.Groupe;
import com.example.tourisme2e.entity.SiteTouristique;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

/**
 * Service de génération de devis PDF conforme au template officiel du §11 du cahier des charges.
 *
 * Template complet :
 * - En-tête : Logo Tourisme 2E, "DEVIS OFFICIEL", numéro et date d'émission
 * - Bloc Client : coordonnées du responsable
 * - Bloc Groupe : dates, participants, sites de l'Oriental, confort, pension
 * - Tableau détaillé des prestations
 * - Récapitulatif : Total HT, Réductions, TVA 20%, Total TTC
 * - Modalités de règlement : Acompte 10%, Solde 90% avec date limite
 * - Conditions légales : annulation, force majeure, assurance
 * - Encart signature et tampon administrateur
 */
@Service
public class DevisPdfService {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final BigDecimal TVA_TAUX = new BigDecimal("0.20");

    // Couleurs Tourisme 2E
    private static final Color COULEUR_PRINCIPALE = new Color(0, 102, 153);  // Bleu profond
    private static final Color COULEUR_SECONDAIRE = new Color(204, 102, 0);  // Orange doré
    private static final Color COULEUR_FOND_HEADER = new Color(0, 51, 102);
    private static final Color COULEUR_GRIS_CLAIR = new Color(245, 245, 245);
    private static final Color COULEUR_GRIS = new Color(180, 180, 180);

    public byte[] generer(Groupe groupe) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 40, 40, 40, 40);

        try {
            PdfWriter writer = PdfWriter.getInstance(document, output);
            document.open();

            // ══════════════════════════════════════════════════════════════
            // EN-TÊTE OFFICIEL
            // ══════════════════════════════════════════════════════════════
            ajouterEnTete(document, groupe);

            document.add(new Paragraph(" "));

            // ══════════════════════════════════════════════════════════════
            // BLOC CLIENT ET INFORMATIONS DU GROUPE
            // ══════════════════════════════════════════════════════════════
            ajouterBlocClientEtGroupe(document, groupe);

            document.add(new Paragraph(" "));

            // ══════════════════════════════════════════════════════════════
            // TABLEAU DÉTAILLÉ DES PRESTATIONS
            // ══════════════════════════════════════════════════════════════
            ajouterTableauPrestations(document, groupe);

            document.add(new Paragraph(" "));

            // ══════════════════════════════════════════════════════════════
            // RÉCAPITULATIF FINANCIER (HT / RÉDUCTIONS / TVA / TTC)
            // ══════════════════════════════════════════════════════════════
            ajouterRecapitulatifFinancier(document, groupe);

            document.add(new Paragraph(" "));

            // ══════════════════════════════════════════════════════════════
            // MODALITÉS DE RÈGLEMENT
            // ══════════════════════════════════════════════════════════════
            ajouterModalitesReglement(document, groupe);

            document.add(new Paragraph(" "));

            // ══════════════════════════════════════════════════════════════
            // CONDITIONS LÉGALES
            // ══════════════════════════════════════════════════════════════
            ajouterConditionsLegales(document);

            document.add(new Paragraph(" "));

            // ══════════════════════════════════════════════════════════════
            // ENCART SIGNATURE ET TAMPON
            // ══════════════════════════════════════════════════════════════
            ajouterSignature(document);

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF devis", e);
        } finally {
            document.close();
        }

        return output.toByteArray();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // EN-TÊTE
    // ─────────────────────────────────────────────────────────────────────────
    private void ajouterEnTete(Document document, Groupe groupe) throws Exception {
        // Bandeau coloré avec nom de l'entreprise
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{60, 40});

        // Colonne gauche : nom et coordonnées
        Phrase nomSociete = new Phrase();
        nomSociete.add(new Chunk("Tourisme 2E\n",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Font.BOLD, Color.WHITE)));
        nomSociete.add(new Chunk("Business & Évasion\n",
                FontFactory.getFont(FontFactory.HELVETICA, 11, Font.ITALIC, new Color(200, 220, 255))));
        nomSociete.add(new Chunk("Oujda, Oriental, Maroc\n",
                FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL, new Color(200, 220, 255))));
        nomSociete.add(new Chunk("contact@tourisme2e.ma | +212 600 000 000",
                FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL, new Color(200, 220, 255))));

        PdfPCell cellGauche = new PdfPCell(nomSociete);
        cellGauche.setBackgroundColor(COULEUR_FOND_HEADER);
        cellGauche.setBorder(Rectangle.NO_BORDER);
        cellGauche.setPadding(15);
        headerTable.addCell(cellGauche);

        // Colonne droite : numéro et date du devis
        String numeroDevis = groupe.getNumeroDevis() != null ? groupe.getNumeroDevis() : "DEV-" + groupe.getId();
        String dateDevis = groupe.getDateDevis() != null ? groupe.getDateDevis().format(DATE_FMT) : LocalDate.now().format(DATE_FMT);

        Phrase infosDevis = new Phrase();
        infosDevis.add(new Chunk("DEVIS OFFICIEL\n",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Font.BOLD, COULEUR_SECONDAIRE)));
        infosDevis.add(new Chunk("N° " + numeroDevis + "\n",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Font.BOLD, Color.WHITE)));
        infosDevis.add(new Chunk("Émis le : " + dateDevis,
                FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL, new Color(200, 220, 255))));

        PdfPCell cellDroite = new PdfPCell(infosDevis);
        cellDroite.setBackgroundColor(COULEUR_FOND_HEADER);
        cellDroite.setBorder(Rectangle.NO_BORDER);
        cellDroite.setPadding(15);
        cellDroite.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headerTable.addCell(cellDroite);

        document.add(headerTable);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // BLOC CLIENT ET INFOS GROUPE
    // ─────────────────────────────────────────────────────────────────────────
    private void ajouterBlocClientEtGroupe(Document document, Groupe groupe) throws Exception {
        PdfPTable blocsTable = new PdfPTable(2);
        blocsTable.setWidthPercentage(100);
        blocsTable.setWidths(new float[]{50, 50});
        blocsTable.setSpacingBefore(10);

        // ── Bloc Client (à gauche) ──
        PdfPTable tableClient = new PdfPTable(1);
        tableClient.setWidthPercentage(100);

        PdfPCell titreClient = new PdfPCell(new Phrase("CLIENT / RESPONSABLE",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Font.BOLD, Color.WHITE)));
        titreClient.setBackgroundColor(COULEUR_PRINCIPALE);
        titreClient.setPadding(5);
        titreClient.setBorder(Rectangle.NO_BORDER);
        tableClient.addCell(titreClient);

        String nomResponsable = "";
        String emailResponsable = "";
        if (groupe.getCreateur() != null) {
            nomResponsable = groupe.getCreateur().getPrenom() + " " + groupe.getCreateur().getNom();
            emailResponsable = groupe.getCreateur().getEmail();
            String organisation = groupe.getCreateur().getOrganisation();
            if (organisation != null && !organisation.isBlank()) {
                nomResponsable = organisation + "\nRep. : " + nomResponsable;
            }
        }

        String infoClient = nomResponsable + "\n" + emailResponsable;
        PdfPCell cellClient = new PdfPCell(new Phrase(infoClient,
                FontFactory.getFont(FontFactory.HELVETICA, 9)));
        cellClient.setPadding(8);
        cellClient.setBackgroundColor(COULEUR_GRIS_CLAIR);
        cellClient.setBorder(Rectangle.BOX);
        tableClient.addCell(cellClient);

        PdfPCell wrapperClient = new PdfPCell(tableClient);
        wrapperClient.setBorder(Rectangle.NO_BORDER);
        wrapperClient.setPaddingRight(5);
        blocsTable.addCell(wrapperClient);

        // ── Bloc Groupe (à droite) ──
        PdfPTable tableGroupe = new PdfPTable(1);
        tableGroupe.setWidthPercentage(100);

        PdfPCell titreGroupe = new PdfPCell(new Phrase("INFORMATIONS DU SÉJOUR",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Font.BOLD, Color.WHITE)));
        titreGroupe.setBackgroundColor(COULEUR_PRINCIPALE);
        titreGroupe.setPadding(5);
        titreGroupe.setBorder(Rectangle.NO_BORDER);
        tableGroupe.addCell(titreGroupe);

        String typeGroupe = groupe.getTypeGroupeFerme() != null ? groupe.getTypeGroupeFerme().name() : groupe.getTypeGroupe().name();
        String dateDebut = groupe.getDateDebut() != null ? groupe.getDateDebut().format(DATE_FMT) : "-";
        String dateFin = groupe.getDateFin() != null ? groupe.getDateFin().format(DATE_FMT) : "-";
        String nbParticipants = groupe.getCapaciteMin() + " participant(s)";
        String confort = groupe.getNiveauConfort() != null ? groupe.getNiveauConfort() + " étoiles" : "Non précisé";
        String pension = groupe.getPension() != null ? groupe.getPension().name().replace("_", " ") : "Non précisée";

        String sites = "";
        if (groupe.getSites() != null && !groupe.getSites().isEmpty()) {
            sites = groupe.getSites().stream()
                    .map(SiteTouristique::getNom)
                    .collect(Collectors.joining(", "));
        }

        String infoGroupe = "Titre : " + groupe.getTitre() + "\n" +
                "Type : " + typeGroupe + "\n" +
                "Dates : " + dateDebut + " → " + dateFin + "\n" +
                "Participants : " + nbParticipants + "\n" +
                "Confort : " + confort + "\n" +
                "Pension : " + pension + "\n" +
                (sites.isBlank() ? "" : "Sites : " + sites);

        PdfPCell cellGroupe = new PdfPCell(new Phrase(infoGroupe,
                FontFactory.getFont(FontFactory.HELVETICA, 9)));
        cellGroupe.setPadding(8);
        cellGroupe.setBackgroundColor(COULEUR_GRIS_CLAIR);
        cellGroupe.setBorder(Rectangle.BOX);
        tableGroupe.addCell(cellGroupe);

        PdfPCell wrapperGroupe = new PdfPCell(tableGroupe);
        wrapperGroupe.setBorder(Rectangle.NO_BORDER);
        wrapperGroupe.setPaddingLeft(5);
        blocsTable.addCell(wrapperGroupe);

        document.add(blocsTable);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TABLEAU DÉTAILLÉ DES PRESTATIONS
    // ─────────────────────────────────────────────────────────────────────────
    private void ajouterTableauPrestations(Document document, Groupe groupe) throws Exception {
        // Titre de section
        Paragraph titrePrestations = new Paragraph("DÉTAIL DES PRESTATIONS",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Font.BOLD, COULEUR_PRINCIPALE));
        titrePrestations.setSpacingBefore(5);
        document.add(titrePrestations);

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{60, 20, 20});
        table.setSpacingBefore(5);

        // En-têtes du tableau
        ajouterCelluleHeader(table, "PRESTATION", Element.ALIGN_LEFT);
        ajouterCelluleHeader(table, "DESCRIPTION", Element.ALIGN_CENTER);
        ajouterCelluleHeader(table, "MONTANT (MAD)", Element.ALIGN_RIGHT);

        // Lignes des prestations
        ajouterLignePrestationTable(table, "Hébergement", "Hôtel/Centre d'estivage", groupe.getMontantHebergement(), false);
        ajouterLignePrestationTable(table, "Restauration", "Cuisine marocaine authentique", groupe.getMontantRestauration(), true);
        ajouterLignePrestationTable(table, "Transport", "Minibus climatisés", groupe.getMontantTransport(), false);
        ajouterLignePrestationTable(table, "Services inclus", "Guides experts, Infirmier 24h, Animation folklore", groupe.getMontantServices(), true);

        document.add(table);
    }

    private void ajouterCelluleHeader(PdfPTable table, String texte, int alignement) {
        PdfPCell cell = new PdfPCell(new Phrase(texte,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Font.BOLD, Color.WHITE)));
        cell.setBackgroundColor(COULEUR_PRINCIPALE);
        cell.setPadding(6);
        cell.setHorizontalAlignment(alignement);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
    }

    private void ajouterLignePrestationTable(PdfPTable table, String prestation, String description,
                                              BigDecimal montant, boolean gris) {
        Color fond = gris ? COULEUR_GRIS_CLAIR : Color.WHITE;
        String montantStr = montant != null ? montant.setScale(2, RoundingMode.HALF_UP) + " MAD" : "0,00 MAD";

        PdfPCell c1 = new PdfPCell(new Phrase(prestation, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9)));
        c1.setBackgroundColor(fond); c1.setPadding(5); c1.setBorderColor(COULEUR_GRIS);
        table.addCell(c1);

        PdfPCell c2 = new PdfPCell(new Phrase(description, FontFactory.getFont(FontFactory.HELVETICA, 8)));
        c2.setBackgroundColor(fond); c2.setPadding(5); c2.setBorderColor(COULEUR_GRIS);
        table.addCell(c2);

        PdfPCell c3 = new PdfPCell(new Phrase(montantStr, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9)));
        c3.setBackgroundColor(fond); c3.setPadding(5); c3.setBorderColor(COULEUR_GRIS);
        c3.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c3);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // RÉCAPITULATIF FINANCIER
    // ─────────────────────────────────────────────────────────────────────────
    private void ajouterRecapitulatifFinancier(Document document, Groupe groupe) throws Exception {
        Paragraph titreRecap = new Paragraph("RÉCAPITULATIF FINANCIER",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Font.BOLD, COULEUR_PRINCIPALE));
        titreRecap.setSpacingBefore(5);
        document.add(titreRecap);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(60);
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.setWidths(new float[]{65, 35});
        table.setSpacingBefore(5);

        // Calcul du HT (total avant TVA = totalTTC / 1.20)
        BigDecimal totalTTC = valeur(groupe.getMontantTotalDevis());
        BigDecimal totalHT = totalTTC.divide(new BigDecimal("1.20"), 2, RoundingMode.HALF_UP);
        BigDecimal reductions = valeur(groupe.getMontantReductions());
        BigDecimal montantTVA = totalTTC.subtract(totalHT);

        ajouterLigneRecap(table, "Total HT", formatMAD(totalHT.add(reductions)), false, false);
        ajouterLigneRecap(table, "Réductions appliquées", "- " + formatMAD(reductions), false, false);
        ajouterLigneRecap(table, "Base HT après réductions", formatMAD(totalHT), false, true);
        ajouterLigneRecap(table, "TVA (20%)", formatMAD(montantTVA), false, false);
        ajouterLigneRecap(table, "TOTAL TTC", formatMAD(totalTTC), true, false);

        document.add(table);
    }

    private void ajouterLigneRecap(PdfPTable table, String libelle, String montant, boolean total, boolean sousTitre) {
        Font fontLibelle = total
                ? FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Font.BOLD, Color.WHITE)
                : (sousTitre ? FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Font.BOLD, COULEUR_PRINCIPALE)
                             : FontFactory.getFont(FontFactory.HELVETICA, 9));

        Font fontMontant = total
                ? FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Font.BOLD, Color.WHITE)
                : FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);

        Color fond = total ? COULEUR_PRINCIPALE : (sousTitre ? COULEUR_GRIS_CLAIR : Color.WHITE);

        PdfPCell c1 = new PdfPCell(new Phrase(libelle, fontLibelle));
        c1.setBackgroundColor(fond); c1.setPadding(5); c1.setBorderColor(COULEUR_GRIS);
        table.addCell(c1);

        PdfPCell c2 = new PdfPCell(new Phrase(montant, fontMontant));
        c2.setBackgroundColor(fond); c2.setPadding(5); c2.setBorderColor(COULEUR_GRIS);
        c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c2);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // MODALITÉS DE RÈGLEMENT
    // ─────────────────────────────────────────────────────────────────────────
    private void ajouterModalitesReglement(Document document, Groupe groupe) throws Exception {
        Paragraph titreReglement = new Paragraph("MODALITÉS DE RÈGLEMENT",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Font.BOLD, COULEUR_PRINCIPALE));
        titreReglement.setSpacingBefore(5);
        document.add(titreReglement);

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{40, 30, 30});
        table.setSpacingBefore(5);

        ajouterCelluleHeader(table, "ÉCHÉANCE", Element.ALIGN_LEFT);
        ajouterCelluleHeader(table, "MONTANT (MAD)", Element.ALIGN_RIGHT);
        ajouterCelluleHeader(table, "STATUT", Element.ALIGN_CENTER);

        boolean acompteRegle = Boolean.TRUE.equals(groupe.getAcompteRegle());
        String statut1 = acompteRegle ? "✓ REÇU" : "À VERSER";
        String dateLimite = groupe.getDateLimiteSolde() != null
                ? "Avant le " + groupe.getDateLimiteSolde().format(DATE_FMT)
                : "15 jours avant le départ";

        ajouterLignePaiement(table, "Acompte 10% (à la réservation)", formatMAD(valeur(groupe.getAcompteDevis())), statut1, acompteRegle);
        ajouterLignePaiement(table, "Solde 90% (" + dateLimite + ")", formatMAD(valeur(groupe.getSoldeDevis())), "EN ATTENTE", false);

        document.add(table);

        Paragraph noteReglement = new Paragraph(
                "Modes de paiement acceptés : Virement bancaire, Carte bancaire (Stripe), Orange Money Maroc.",
                FontFactory.getFont(FontFactory.HELVETICA, 8, Font.ITALIC, Color.GRAY));
        noteReglement.setSpacingBefore(3);
        document.add(noteReglement);
    }

    private void ajouterLignePaiement(PdfPTable table, String libelle, String montant, String statut, boolean regle) {
        PdfPCell c1 = new PdfPCell(new Phrase(libelle, FontFactory.getFont(FontFactory.HELVETICA, 9)));
        c1.setPadding(6); c1.setBorderColor(COULEUR_GRIS); c1.setBackgroundColor(COULEUR_GRIS_CLAIR);
        table.addCell(c1);

        PdfPCell c2 = new PdfPCell(new Phrase(montant, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9)));
        c2.setPadding(6); c2.setBorderColor(COULEUR_GRIS); c2.setBackgroundColor(COULEUR_GRIS_CLAIR);
        c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c2);

        Color couleurStatut = regle ? new Color(0, 128, 0) : COULEUR_SECONDAIRE;
        PdfPCell c3 = new PdfPCell(new Phrase(statut,
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Font.BOLD, couleurStatut)));
        c3.setPadding(6); c3.setBorderColor(COULEUR_GRIS); c3.setBackgroundColor(COULEUR_GRIS_CLAIR);
        c3.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c3);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // CONDITIONS LÉGALES
    // ─────────────────────────────────────────────────────────────────────────
    private void ajouterConditionsLegales(Document document) throws Exception {
        Paragraph titre = new Paragraph("CONDITIONS GÉNÉRALES",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Font.BOLD, COULEUR_PRINCIPALE));
        titre.setSpacingBefore(5);
        document.add(titre);

        String conditions =
                "ANNULATION : Toute annulation doit être notifiée par écrit. Annulation > 45 jours : remboursement intégral de l'acompte. " +
                "Annulation entre 30 et 45 jours : retenue de 30% de l'acompte. " +
                "Annulation < 30 jours : acompte non remboursable.\n\n" +
                "FORCE MAJEURE : Tourisme 2E décline toute responsabilité en cas d'événements indépendants de sa volonté " +
                "(catastrophes naturelles, épidémies, grèves, restrictions gouvernementales). " +
                "Dans ce cas, un avoir ou un report sera proposé au client.\n\n" +
                "ASSURANCE : Il est vivement recommandé à chaque participant de souscrire une assurance voyage " +
                "couvrant les frais médicaux, l'annulation et le rapatriement.\n\n" +
                "RESPONSABILITÉ : Tourisme 2E est immatriculée au Registre de Commerce d'Oujda. " +
                "La prestation inclut un infirmier disponible 24h/24 et des guides diplômés de la région Oriental.";

        Paragraph conditionsText = new Paragraph(conditions,
                FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL, Color.DARK_GRAY));
        conditionsText.setSpacingBefore(3);
        document.add(conditionsText);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SIGNATURE ET TAMPON
    // ─────────────────────────────────────────────────────────────────────────
    private void ajouterSignature(Document document) throws Exception {
        PdfPTable sigTable = new PdfPTable(2);
        sigTable.setWidthPercentage(100);
        sigTable.setWidths(new float[]{50, 50});
        sigTable.setSpacingBefore(15);

        // Signature client (gauche)
        PdfPCell cellClient = new PdfPCell();
        cellClient.setBorder(Rectangle.BOX);
        cellClient.setPadding(10);
        cellClient.setMinimumHeight(70);
        cellClient.addElement(new Phrase("Bon pour accord – Signature du responsable :",
                FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD)));
        cellClient.addElement(new Phrase("\n\n(Mention « Lu et approuvé »)",
                FontFactory.getFont(FontFactory.HELVETICA, 7, Font.ITALIC, Color.GRAY)));
        sigTable.addCell(cellClient);

        // Tampon agence (droite)
        PdfPCell cellAgence = new PdfPCell();
        cellAgence.setBorder(Rectangle.BOX);
        cellAgence.setPadding(10);
        cellAgence.setMinimumHeight(70);
        cellAgence.setBackgroundColor(COULEUR_GRIS_CLAIR);
        cellAgence.addElement(new Phrase("Signature et cachet Tourisme 2E :",
                FontFactory.getFont(FontFactory.HELVETICA, 8, Font.BOLD, COULEUR_PRINCIPALE)));
        cellAgence.addElement(new Phrase("\n\nTourisme 2E – Business & Évasion\nOujda, Maroc",
                FontFactory.getFont(FontFactory.HELVETICA, 8, Font.ITALIC, Color.GRAY)));
        sigTable.addCell(cellAgence);

        document.add(sigTable);

        Paragraph footer = new Paragraph(
                "Tourisme 2E SARL – RC Oujda – ICE : 000000000000000 – Tél. : +212 600 000 000 – contact@tourisme2e.ma – www.tourisme2e.ma",
                FontFactory.getFont(FontFactory.HELVETICA, 7, Font.NORMAL, Color.GRAY));
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(10);
        document.add(footer);
    }

    private String montant(BigDecimal valeur) {
        return (valeur != null ? valeur.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO) + " MAD";
    }

    private String formatMAD(BigDecimal valeur) {
        return String.format("%.2f MAD", valeur != null ? valeur : BigDecimal.ZERO);
    }

    private BigDecimal valeur(BigDecimal v) {
        return v != null ? v : BigDecimal.ZERO;
    }
}
