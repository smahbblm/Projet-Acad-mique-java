package com.stock.Controller.controllerResponsableApprovisionnements;

import com.stock.model.produit.Produit;
import com.stock.service.ProduitService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GestionStockController {

    @FXML
    private Label lblDashboard;

    @FXML
    private Label lblProduits;

    @FXML
    private Label lblFournisseurs;

    @FXML
    private Label lblCommandes;

    @FXML
    private Label lblRapports;

    @FXML
    private TextField searchField;

    @FXML
    private VBox stockContainer;

    @FXML
    private Button btnMouvements;

    @FXML
    private ComboBox<String> cmbStatut;

    private ProduitService produitService;

    @FXML
    public void initialize() {
        try {
            produitService = new ProduitService();
            loadStock();
            setupStatutFilter();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (lblDashboard != null) {
            lblDashboard.setOnMouseClicked(event -> goToDashboard());
        }

        if (lblProduits != null) {
            lblProduits.setOnMouseClicked(event -> goToProduits());
        }

        if (lblFournisseurs != null) {
            lblFournisseurs.setOnMouseClicked(event -> goToFournisseurs());
        }

        if (lblCommandes != null) {
            lblCommandes.setOnMouseClicked(event -> goToCommandes());
        }

        if (lblRapports != null) {
            lblRapports.setOnMouseClicked(event -> goToRapports());
        }
        
        if (searchField != null) {
            searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        }
    }

    private void setupStatutFilter() {
        cmbStatut.getItems().addAll("Tous les statuts", "En stock", "Rupture", "Overflow");
        cmbStatut.setValue("Tous les statuts");
        cmbStatut.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
    }
    
    private void loadStock() {
        try {
            var produits = produitService.consulterTousProduits();
            stockContainer.getChildren().clear();
            
            produits.forEach(p -> {
                HBox row = new HBox(10);
                row.setStyle("-fx-padding:16; -fx-border-color: transparent transparent #f0f0f0 transparent; -fx-border-width:0 0 1 0;");
                
                double pourcentage = (double) p.getQuantiteStock() / p.getSeuilMax() * 100;
                String statut;
                String statutStyle;
                String progressColor;
                
                if (p.getQuantiteStock() < p.getSeuilMin()) {
                    statut = "Rupture";
                    statutStyle = "-fx-background-color:#e74c3c; -fx-text-fill:white; -fx-padding:4 8; -fx-background-radius:6; -fx-pref-width:100;";
                    progressColor = "red";
                } else if (p.getQuantiteStock() > p.getSeuilMax()) {
                    statut = "Overflow";
                    statutStyle = "-fx-background-color:#f39c12; -fx-text-fill:white; -fx-padding:4 8; -fx-background-radius:6; -fx-pref-width:100;";
                    progressColor = "orange";
                } else {
                    statut = "En stock";
                    statutStyle = "-fx-background-color:#2e86de; -fx-text-fill:white; -fx-padding:4 8; -fx-background-radius:6; -fx-pref-width:100;";
                    progressColor = "green";
                }
                
                Label lblStatut = new Label(statut);
                lblStatut.setStyle(statutStyle);
                
                ProgressBar progress = new ProgressBar(Math.min(pourcentage / 100, 1.0));
                progress.setPrefWidth(100);
                progress.setStyle("-fx-accent: " + progressColor + ";");
                
                HBox progressBox = new HBox(5);
                progressBox.setStyle("-fx-pref-width:150;");
                progressBox.getChildren().addAll(progress, new Label(String.format("%.0f%%", pourcentage)));
                
                row.getChildren().addAll(
                    createLabel(p.getReference(), 110),
                    createLabel(p.getDesignation(), 150),
                    createLabel(p.getCategorie(), 100),
                    createLabel(p.getQuantiteStock() + " pcs", 100),
                    createLabel(p.getSeuilMin() + " pcs", 110),
                    lblStatut,
                    progressBox
                );
                
                stockContainer.getChildren().add(row);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void applyFilters() {
        try {
            var produits = produitService.consulterTousProduits();
            stockContainer.getChildren().clear();
            
            String searchText = searchField.getText();
            String statutFilter = cmbStatut.getValue();
            
            produits.stream()
                .filter(p -> searchText == null || searchText.isEmpty() ||
                    p.getReference().toLowerCase().contains(searchText.toLowerCase()) ||
                    p.getDesignation().toLowerCase().contains(searchText.toLowerCase()))
                .filter(p -> {
                    if (statutFilter == null || statutFilter.equals("Tous les statuts")) return true;
                    String statut = getStatut(p);
                    return statut.equals(statutFilter);
                })
                .forEach(p -> {
                    HBox row = new HBox(10);
                    row.setStyle("-fx-padding:16; -fx-border-color: transparent transparent #f0f0f0 transparent; -fx-border-width:0 0 1 0;");
                    
                    double pourcentage = (double) p.getQuantiteStock() / p.getSeuilMax() * 100;
                    String statut = getStatut(p);
                    String statutStyle = getStatutStyle(statut);
                    String progressColor = getProgressColor(statut);
                    
                    Label lblStatut = new Label(statut);
                    lblStatut.setStyle(statutStyle);
                    
                    ProgressBar progress = new ProgressBar(Math.min(pourcentage / 100, 1.0));
                    progress.setPrefWidth(100);
                    progress.setStyle("-fx-accent: " + progressColor + ";");
                    
                    HBox progressBox = new HBox(5);
                    progressBox.setStyle("-fx-pref-width:150;");
                    progressBox.getChildren().addAll(progress, new Label(String.format("%.0f%%", pourcentage)));
                    
                    row.getChildren().addAll(
                        createLabel(p.getReference(), 110),
                        createLabel(p.getDesignation(), 150),
                        createLabel(p.getCategorie(), 100),
                        createLabel(p.getQuantiteStock() + " pcs", 100),
                        createLabel(p.getSeuilMin() + " pcs", 110),
                        lblStatut,
                        progressBox
                    );
                    
                    stockContainer.getChildren().add(row);
                });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getStatut(Produit p) {
        if (p.getQuantiteStock() < p.getSeuilMin()) {
            return "Rupture";
        } else if (p.getQuantiteStock() > p.getSeuilMax()) {
            return "Overflow";
        } else {
            return "En stock";
        }
    }

    private String getStatutStyle(String statut) {
        switch (statut) {
            case "Rupture":
                return "-fx-background-color:#e74c3c; -fx-text-fill:white; -fx-padding:4 8; -fx-background-radius:6; -fx-pref-width:100;";
            case "Overflow":
                return "-fx-background-color:#f39c12; -fx-text-fill:white; -fx-padding:4 8; -fx-background-radius:6; -fx-pref-width:100;";
            default:
                return "-fx-background-color:#2e86de; -fx-text-fill:white; -fx-padding:4 8; -fx-background-radius:6; -fx-pref-width:100;";
        }
    }

    private String getProgressColor(String statut) {
        switch (statut) {
            case "Rupture":
                return "red";
            case "Overflow":
                return "orange";
            default:
                return "green";
        }
    }
    
    private Label createLabel(String text, double width) {
        Label label = new Label(text);
        label.setPrefWidth(width);
        return label;
    }

    @FXML
    private void goToDashboard() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/DashResponsableApprovisionnements/dashboardResApprov.fxml"));
            Stage stage = (Stage) lblDashboard.getScene().getWindow();
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToProduits() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/DashResponsableApprovisionnements/produits.fxml"));
            Stage stage = (Stage) lblProduits.getScene().getWindow();
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToFournisseurs() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/DashResponsableApprovisionnements/fournisseurs.fxml"));
            Stage stage = (Stage) lblFournisseurs.getScene().getWindow();
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToCommandes() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/DashResponsableApprovisionnements/bonCommandes.fxml"));
            Stage stage = (Stage) lblCommandes.getScene().getWindow();
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToRapports() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/DashResponsableApprovisionnements/rapports.fxml"));
            Stage stage = (Stage) lblRapports.getScene().getWindow();
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goToMouvements() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/DashResponsableApprovisionnements/mouvementsStock.fxml"));
            Stage stage = (Stage) btnMouvements.getScene().getWindow();
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
