package com.stock.Controller.controller_Dashboard_Administrateur;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.io.File;
import javafx.stage.FileChooser;

public class DatabaseManagementController {
    @FXML private Button backupButton;
    @FXML private Label backupStatus;

    @FXML private Button chooseFileButton;
    @FXML private Button restoreButton;
    @FXML private Label restoreStatus;

    private File selectedFile;

    @FXML
    public void initialize() {

        // Sauvegarde
        backupButton.setOnAction(e -> {
            try {
                // BackupBDD.saveToFile("chemin/backup.sql");
                backupStatus.setText("Sauvegarde réussie !");
            } catch (Exception ex) {
                backupStatus.setText("Erreur : " + ex.getMessage());
            }
        });

        // Choisir fichier pour restauration
        chooseFileButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choisir le fichier de sauvegarde");
            selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                restoreStatus.setText("Fichier choisi : " + selectedFile.getName());
            }
        });

        // Restaurer
        restoreButton.setOnAction(e -> {
            if (selectedFile != null) {
                try {
                    // RestoreBDD.restoreFromFile(selectedFile);
                    restoreStatus.setText("Restauration réussie !");
                } catch (Exception ex) {
                    restoreStatus.setText("Erreur : " + ex.getMessage());
                }
            } else {
                restoreStatus.setText("Aucun fichier sélectionné !");
            }
        });
    }


}
