package com.stock.util;

import com.stock.model.document.BonLivraison;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class PDFExporter {
    
    public static void exporterBonLivraison(BonLivraison bon, String cheminFichier) {
        try {
            PdfWriter writer = new PdfWriter(cheminFichier + ".pdf");
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            
            PdfFont font = PdfFontFactory.createFont();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            // En-tête
            document.add(new Paragraph("SYSTÈME DE GESTION DE STOCK")
                    .setFont(font).setFontSize(18).setTextAlignment(TextAlignment.CENTER).setBold());
            
            document.add(new Paragraph("BON DE LIVRAISON N° " + bon.getNumero())
                    .setFont(font).setFontSize(16).setTextAlignment(TextAlignment.CENTER).setBold());
            
            document.add(new Paragraph("\n"));
            
            // Informations
            document.add(new Paragraph("Date de livraison: " + bon.getDateLivraison().format(formatter)));
            document.add(new Paragraph("Client: " + bon.getClientNom()));
            document.add(new Paragraph("Adresse: " + (bon.getAdresseLivraison() != null ? bon.getAdresseLivraison() : "Non spécifiée")));
            document.add(new Paragraph("Statut: " + bon.getStatut()));
            
            document.add(new Paragraph("\n"));
            
            // Tableau + c'est  dans  cette partie  que je dois récuperre les produits passé  dans le bin de commande
            Table table = new Table(3);
            table.addHeaderCell(new Cell().add(new Paragraph("Produit").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Quantité").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Observations").setBold()));
            
            table.addCell("Produits divers");
            table.addCell("-");
            table.addCell(bon.getObservations() != null ? bon.getObservations() : "Aucune");
            
            document.add(table);
            
            document.add(new Paragraph("\n\n"));
            document.add(new Paragraph("Signature du client: _________________________"));
            document.add(new Paragraph("Date de réception: _________________________"));
            
            document.close();
            System.out.println("PDF généré: " + cheminFichier + ".pdf");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}