package com.example.tourisme2e.service;

import com.example.tourisme2e.entity.Groupe;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

@Service
public class DevisPdfService {

    public byte[] generer(Groupe groupe) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        document.open();

        document.add(new Paragraph("Tourisme 2E - Devis groupe"));
        document.add(new Paragraph("Groupe : " + groupe.getTitre()));
        document.add(new Paragraph("Type : " + groupe.getTypeGroupe()));
        document.add(new Paragraph("Periode : " + groupe.getDateDebut() + " au " + groupe.getDateFin()));
        document.add(new Paragraph("Capacite : " + groupe.getCapaciteMin() + " - " + groupe.getCapaciteMax() + " personnes"));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(2);
        table.addCell("Hebergement");
        table.addCell(montant(groupe.getMontantHebergement()));
        table.addCell("Restauration");
        table.addCell(montant(groupe.getMontantRestauration()));
        table.addCell("Transport");
        table.addCell(montant(groupe.getMontantTransport()));
        table.addCell("Services");
        table.addCell(montant(groupe.getMontantServices()));
        table.addCell("Reductions");
        table.addCell(montant(groupe.getMontantReductions()));
        table.addCell("Total");
        table.addCell(montant(groupe.getMontantTotalDevis()));
        table.addCell("Acompte 10%");
        table.addCell(montant(groupe.getAcompteDevis()));
        table.addCell("Solde 90%");
        table.addCell(montant(groupe.getSoldeDevis()));
        document.add(table);

        document.close();
        return output.toByteArray();
    }

    private String montant(BigDecimal valeur) {
        return (valeur != null ? valeur : BigDecimal.ZERO) + " MAD";
    }
}
