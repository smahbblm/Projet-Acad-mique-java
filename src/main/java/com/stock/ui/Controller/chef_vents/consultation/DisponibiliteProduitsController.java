package com.stock.ui.Controller.chef_vents.consultation;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

public class DisponibiliteProduitsController {

    @FXML private TableView<?> tableProduits;
    @FXML private ComboBox<String> filtreCategorie;
    @FXML private ComboBox<String> filtreStatutStock;
    @FXML private ComboBox<String> filtreFournisseur;
    @FXML private TextField rechercheField;
    @FXML private Text totalProduitsNumber;
    @FXML private Text stockBasNumber;
    @FXML private Text stockDisponibleNumber;
    @FXML private Text stockEpuiseNumber;
    @FXML private Button btnExporter;
    @FXML private Button btnActualiser;
    @FXML private Button btnAlertes;

    public void initialize() {
        System.out.println("je suis la fonction d'initialisation de la classe DisponibiliteProduitsController");
    }

    @FXML
    public void exporterStock() {
        System.out.println("Export du stock");
    }

    @FXML
    public void actualiserStock() {
        System.out.println("Actualisation du stock");
    }

    @FXML
    public void voirAlertes() {
        System.out.println("Affichage des alertes de stock");
    }
}