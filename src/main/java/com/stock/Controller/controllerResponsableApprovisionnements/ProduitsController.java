package com.stock.Controller.controllerResponsableApprovisionnements;

import com.stock.model.produit.Produit;
import com.stock.service.ProduitService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProduitsController {

    @FXML
    private Button btnDashboard;

    @FXML
    private javafx.scene.control.Label lblFournisseurs;

    @FXML
    private javafx.scene.control.Label lblCommandes;

    @FXML
    private javafx.scene.control.Label lblStock;

    @FXML
    private Button btnRapports;

    @FXML
    private Button btnAddProduct;

    @FXML
    private TextField searchField;

    @FXML
    private VBox productsContainer;

    private ProduitService produitService;

    @FXML
    public void initialize() {
        try {
            produitService = new ProduitService();
            loadProduits();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Page Produits chargée !");

        if (lblFournisseurs != null) {
            lblFournisseurs.setOnMouseClicked(event -> goToFournisseurs());
        }

        if (lblCommandes != null) {
            lblCommandes.setOnMouseClicked(event -> goToCommandes());
        }

        if (lblStock != null) {
            lblStock.setOnMouseClicked(event -> goToStock());
        }
        
        if (searchField != null) {
            searchField.textProperty().addListener((obs, oldVal, newVal) -> filterProduits(newVal));
        }
    }

    @FXML
    public void goToDashboard(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/DashResponsableApprovisionnements/dashboardResApprov.fxml"));
            Stage stage = (Stage) btnDashboard.getScene().getWindow();
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
    private void goToStock() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/DashResponsableApprovisionnements/gestionStock.fxml"));
            Stage stage = (Stage) lblStock.getScene().getWindow();
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goToRapports() {
        try {
            System.out.println("Tentative de navigation vers rapports...");
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/DashResponsableApprovisionnements/rapports.fxml"));
            Stage stage = (Stage) btnRapports.getScene().getWindow();
            stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
            System.out.println("Navigation vers rapports réussie !");
        } catch (Exception e) {
            System.out.println("Erreur lors de la navigation vers rapports: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void openAddProductPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DashResponsableApprovisionnements/ajouterProduit.fxml"));
            Parent root = loader.load();
            AjouterProduitController controller = loader.getController();
            controller.setOnProductAdded(() -> loadProduits());
            
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setTitle("Ajouter un produit");
            popup.setScene(new Scene(root, 750, 650));
            popup.setResizable(false);
            popup.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openEditProductPopup(Produit produit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DashResponsableApprovisionnements/modifierProduit.fxml"));
            Parent root = loader.load();
            ModifierProduitController controller = loader.getController();
            controller.setProduit(produit);
            controller.setOnProductUpdated(() -> loadProduits());
            
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setTitle("Modifier le produit");
            popup.setScene(new Scene(root, 750, 650));
            popup.setResizable(false);
            popup.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadProduits() {
        try {
            var produits = produitService.consulterTousProduits();
            productsContainer.getChildren().clear();
            
            produits.forEach(p -> {
                HBox row = new HBox(10);
                row.setStyle("-fx-padding:16; -fx-border-color: transparent transparent #f0f0f0 transparent; -fx-border-width:0 0 1 0;");
                
                String statut;
                String statutStyle;
                if (p.getQuantiteStock() < p.getSeuilMin()) {
                    statut = "Rupture";
                    statutStyle = "-fx-background-color:#e74c3c; -fx-text-fill:white; -fx-padding:4 8; -fx-background-radius:6; -fx-pref-width:120;";
                } else if (p.getQuantiteStock() > p.getSeuilMax()) {
                    statut = "Overflow";
                    statutStyle = "-fx-background-color:#f39c12; -fx-text-fill:white; -fx-padding:4 8; -fx-background-radius:6; -fx-pref-width:120;";
                } else {
                    statut = "En stock";
                    statutStyle = "-fx-background-color:#2e86de; -fx-text-fill:white; -fx-padding:4 8; -fx-background-radius:6; -fx-pref-width:120;";
                }
                
                Label lblStatut = new Label(statut);
                lblStatut.setStyle(statutStyle);
                
                HBox actions = new HBox(8);
                actions.setStyle("-fx-pref-width:100;");
                Button btnEdit = new Button("✏️");
                btnEdit.setStyle("-fx-cursor:hand;");
                btnEdit.setOnAction(e -> openEditProductPopup(p));
                Button btnDelete = new Button("🗑️");
                btnDelete.setStyle("-fx-cursor:hand;");
                btnDelete.setOnAction(e -> deleteProduit(p.getIdProduit()));
                actions.getChildren().addAll(btnEdit, btnDelete);
                
                row.getChildren().addAll(
                    createLabel(p.getReference(), 120),
                    createLabel(p.getDesignation(), 180),
                    createLabel(p.getCategorie(), 120),
                    createLabel(String.format("%.2f €", p.getPrixVente()), 100),
                    createLabel(p.getQuantiteStock() + " pcs", 80),
                    createLabel(String.valueOf(p.getSeuilMin()), 80),
                    lblStatut,
                    actions
                );
                
                productsContainer.getChildren().add(row);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private Label createLabel(String text, double width) {
        Label label = new Label(text);
        label.setPrefWidth(width);
        return label;
    }
    
    private void filterProduits(String searchText) {
        try {
            var produits = produitService.consulterTousProduits();
            productsContainer.getChildren().clear();
            
            produits.stream()
                .filter(p -> searchText == null || searchText.isEmpty() || 
                    p.getDesignation().toLowerCase().contains(searchText.toLowerCase()) ||
                    p.getReference().toLowerCase().contains(searchText.toLowerCase()))
                .forEach(p -> {
                    HBox row = new HBox(10);
                    row.setStyle("-fx-padding:16; -fx-border-color: transparent transparent #f0f0f0 transparent; -fx-border-width:0 0 1 0;");
                    
                    String statut;
                    String statutStyle;
                    if (p.getQuantiteStock() < p.getSeuilMin()) {
                        statut = "Rupture";
                        statutStyle = "-fx-background-color:#e74c3c; -fx-text-fill:white; -fx-padding:4 8; -fx-background-radius:6; -fx-pref-width:120;";
                    } else if (p.getQuantiteStock() > p.getSeuilMax()) {
                        statut = "Overflow";
                        statutStyle = "-fx-background-color:#f39c12; -fx-text-fill:white; -fx-padding:4 8; -fx-background-radius:6; -fx-pref-width:120;";
                    } else {
                        statut = "En stock";
                        statutStyle = "-fx-background-color:#2e86de; -fx-text-fill:white; -fx-padding:4 8; -fx-background-radius:6; -fx-pref-width:120;";
                    }
                    
                    Label lblStatut = new Label(statut);
                    lblStatut.setStyle(statutStyle);
                    
                    HBox actions = new HBox(8);
                    actions.setStyle("-fx-pref-width:100;");
                    Button btnEdit = new Button("✏️");
                    btnEdit.setStyle("-fx-cursor:hand;");
                    btnEdit.setOnAction(e -> openEditProductPopup(p));
                    Button btnDelete = new Button("🗑️");
                    btnDelete.setStyle("-fx-cursor:hand;");
                    btnDelete.setOnAction(e -> deleteProduit(p.getIdProduit()));
                    actions.getChildren().addAll(btnEdit, btnDelete);
                    
                    row.getChildren().addAll(
                        createLabel(p.getReference(), 120),
                        createLabel(p.getDesignation(), 180),
                        createLabel(p.getCategorie(), 120),
                        createLabel(String.format("%.2f €", p.getPrixVente()), 100),
                        createLabel(p.getQuantiteStock() + " pcs", 80),
                        createLabel(String.valueOf(p.getSeuilMin()), 80),
                        lblStatut,
                        actions
                    );
                    
                    productsContainer.getChildren().add(row);
                });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void deleteProduit(int idProduit) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer le produit");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce produit ?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                try {
                    produitService.supprimerProduit(idProduit);
                    loadProduits();
                } catch (java.sql.SQLIntegrityConstraintViolationException e) {
                    javafx.scene.control.Alert error = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                    error.setTitle("Erreur");
                    error.setHeaderText("Impossible de supprimer");
                    error.setContentText("Ce produit est utilisé dans des commandes et ne peut pas être supprimé.");
                    error.showAndWait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
