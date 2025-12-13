package com.stock.Controller.Dashboard_Administrateur;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;

public class GenerateReportController {

    @FXML private ComboBox<String> reportType;
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;
    @FXML private ComboBox<String> formatBox;
    @FXML private Button generateButton;
    @FXML private Label statusLabel;

    @FXML
    public void initialize() {

        // Valeurs statiques par défaut
        reportType.getItems().addAll(
                "Rapport des mouvements",
                "Entrées",
                "Sorties",
                "Inventaire",
                "Par utilisateur"
        );

        formatBox.getItems().addAll(
                "PDF",
                "Excel (XLSX)",
                "CSV"
        );

        // Dates statiques par défaut
        startDate.setValue(LocalDate.now().minusDays(7));
        endDate.setValue(LocalDate.now());

        // Action statique → pas de traitement réel
        generateButton.setOnAction(e -> generateReport());
    }

    private void generateReport() {

        // Récupération des valeurs
        String type = reportType.getValue();
        LocalDate start = startDate.getValue();
        LocalDate end = endDate.getValue();
        String format = formatBox.getValue();

        // Vérifications simples
        if (type == null || type.isEmpty()) {
            setStatus("⚠️ Veuillez choisir un type de rapport.", "red");
            return;
        }

        if (start == null || end == null) {
            setStatus("⚠️ Veuillez sélectionner la période.", "red");
            return;
        }

        if (end.isBefore(start)) {
            setStatus("❌ La date fin ne peut pas être avant la date début.", "red");
            return;
        }

        if (format == null || format.isEmpty()) {
            setStatus("⚠️ Veuillez choisir un format de fichier.", "red");
            return;
        }

        // -----------------------------
        // VERSION 100% STATIQUE
        // No export, no database, no file
        // -----------------------------
        System.out.println("=== RAPPORT STATIQUE ===");
        System.out.println("Type (statique) : " + type);
        System.out.println("Période : " + start + " → " + end);
        System.out.println("Format : " + format);
        System.out.println("========================");

        // Message statique
        setStatus("✔️ Rapport généré (simulation statique).", "green");
    }

    private void setStatus(String text, String color) {
        statusLabel.setText(text);
        statusLabel.setStyle("-fx-text-fill:" + color + "; -fx-font-weight:bold;");
    }
}
