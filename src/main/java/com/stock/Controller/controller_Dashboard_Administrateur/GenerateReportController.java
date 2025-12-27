package com.stock.Controller.controller_Dashboard_Administrateur;

import com.stock.model.systeme.Rapport;
import com.stock.service.RapportService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.io.FileWriter;
import java.io.IOException;

public class GenerateReportController {

    @FXML private ComboBox<String> reportType;
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;
    @FXML private Button generateButton;
    @FXML private Label statusLabel;

    private final RapportService rapportService = new RapportService();
    private final Map<String, String> typeMapping = new HashMap<>();

    @FXML
    public void initialize() {
        // Définir les types de rapports
        typeMapping.put("Rapport des mouvements", "ANALYSE_GLOBAL");
        typeMapping.put("Entrées / Sorties", "MOUVEMENTS");
        typeMapping.put("Produits", "PRODUITS");
        typeMapping.put("Utilisateurs", "UTILISATEURS");

        reportType.getItems().addAll(typeMapping.keySet());

        startDate.setValue(LocalDate.now().minusDays(7));
        endDate.setValue(LocalDate.now());

        generateButton.setOnAction(e -> generateReport());
    }

    private void generateReport() {
        String uiType = reportType.getValue();
        LocalDate start = startDate.getValue();
        LocalDate end = endDate.getValue();

        if (uiType == null || start == null || end == null) {
            setStatus("Veuillez remplir tous les champs.", "red");
            return;
        }

        if (end.isBefore(start)) {
            setStatus("Date fin invalide.", "red");
            return;
        }

        try {
            String techType = typeMapping.get(uiType);
            Rapport rapport = rapportService.genererRapport(techType, start, end);

            // Génération automatique d'un fichier TXT
            String fileName = "Rapport_" + techType + "_" + LocalDate.now() + ".txt";
            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write(rapport.getContenu());
            }

            setStatus("Rapport TXT généré : " + fileName, "green");

        } catch (Exception e) {
            e.printStackTrace();
            setStatus("Erreur génération rapport.", "red");
        }
    }

    private void setStatus(String text, String color) {
        statusLabel.setText(text);
        statusLabel.setStyle("-fx-text-fill:" + color + "; -fx-font-weight:bold;");
    }
}
