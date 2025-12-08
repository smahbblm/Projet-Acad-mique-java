package com.stock.Controller.controllerResponsableApprovisionnements;

import com.stock.model.partenaire.Fournisseur;
import com.stock.service.FournisseurService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FournisseursController {

    @FXML
    private Label lblDashboard;

    @FXML
    private Label lblProduits;

    @FXML
    private Label lblCommandes;

    @FXML
    private Label lblStock;

    @FXML
    private Label lblRapports;

    @FXML
    private Button btnAddSupplier;

    @FXML
    private TextField searchField;

    @FXML
    private VBox suppliersContainer;

    private FournisseurService fournisseurService;

    @FXML
    public void initialize() {
        try {
            fournisseurService = new FournisseurService();
            loadFournisseurs();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Page Fournisseurs chargée !");

        if (lblDashboard != null) {
            lblDashboard.setOnMouseClicked(event -> goToDashboard());
        }

        if (lblProduits != null) {
            lblProduits.setOnMouseClicked(event -> goToProduits());
        }

        if (lblCommandes != null) {
            lblCommandes.setOnMouseClicked(event -> goToCommandes());
        }

        if (lblStock != null) {
            lblStock.setOnMouseClicked(event -> goToStock());
        }

        if (lblRapports != null) {
            lblRapports.setOnMouseClicked(event -> goToRapports());
        }
        
        if (searchField != null) {
            searchField.textProperty().addListener((obs, oldVal, newVal) -> filterFournisseurs(newVal));
        }
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
    public void openAddSupplierPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DashResponsableApprovisionnements/ajouterFournisseur.fxml"));
            Parent root = loader.load();
            AjouterFournisseurController controller = loader.getController();
            controller.setOnFournisseurAdded(() -> loadFournisseurs());
            
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setTitle("Ajouter un fournisseur");
            popup.setScene(new Scene(root, 700, 630));
            popup.setResizable(false);
            popup.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openEditSupplierPopup(Fournisseur fournisseur) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DashResponsableApprovisionnements/modifierFournisseur.fxml"));
            Parent root = loader.load();
            ModifierFournisseurController controller = loader.getController();
            controller.setFournisseur(fournisseur);
            controller.setOnFournisseurUpdated(() -> loadFournisseurs());
            
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setTitle("Modifier le fournisseur");
            popup.setScene(new Scene(root, 700, 630));
            popup.setResizable(false);
            popup.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadFournisseurs() {
        try {
            var fournisseurs = fournisseurService.consulterTousFournisseurs();
            suppliersContainer.getChildren().clear();
            
            fournisseurs.forEach(f -> {
                HBox row = new HBox(10);
                row.setStyle("-fx-padding:16; -fx-border-color: transparent transparent #f0f0f0 transparent; -fx-border-width:0 0 1 0;");
                
                VBox coordonnees = new VBox(4);
                coordonnees.setStyle("-fx-pref-width:200;");
                if (f.getEmail() != null && !f.getEmail().isEmpty()) {
                    Label lblEmail = new Label("✉ " + f.getEmail());
                    lblEmail.setStyle("-fx-font-size:11;");
                    coordonnees.getChildren().add(lblEmail);
                }
                if (f.getTelephone() != null && !f.getTelephone().isEmpty()) {
                    Label lblTel = new Label("📞 " + f.getTelephone());
                    lblTel.setStyle("-fx-font-size:11;");
                    coordonnees.getChildren().add(lblTel);
                }
                
                HBox actions = new HBox(8);
                actions.setStyle("-fx-pref-width:100;");
                Button btnEdit = new Button("✏️");
                btnEdit.setStyle("-fx-cursor:hand;");
                btnEdit.setOnAction(e -> openEditSupplierPopup(f));
                Button btnDelete = new Button("🗑️");
                btnDelete.setStyle("-fx-cursor:hand;");
                btnDelete.setOnAction(e -> deleteFournisseur(f.getIdFournisseur()));
                actions.getChildren().addAll(btnEdit, btnDelete);
                
                row.getChildren().addAll(
                    createLabel(f.getRaisonSociale(), 180),
                    createLabel(f.getContact() != null ? f.getContact() : "", 150),
                    coordonnees,
                    createLabel(f.getAdresse() != null ? f.getAdresse() : "", 280),
                    actions
                );
                
                suppliersContainer.getChildren().add(row);
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
    
    private void filterFournisseurs(String searchText) {
        try {
            var fournisseurs = fournisseurService.consulterTousFournisseurs();
            suppliersContainer.getChildren().clear();
            
            fournisseurs.stream()
                .filter(f -> searchText == null || searchText.isEmpty() || 
                    f.getRaisonSociale().toLowerCase().contains(searchText.toLowerCase()) ||
                    (f.getContact() != null && f.getContact().toLowerCase().contains(searchText.toLowerCase())))
                .forEach(f -> {
                    HBox row = new HBox(10);
                    row.setStyle("-fx-padding:16; -fx-border-color: transparent transparent #f0f0f0 transparent; -fx-border-width:0 0 1 0;");
                    
                    VBox coordonnees = new VBox(4);
                    coordonnees.setStyle("-fx-pref-width:200;");
                    if (f.getEmail() != null && !f.getEmail().isEmpty()) {
                        Label lblEmail = new Label("✉ " + f.getEmail());
                        lblEmail.setStyle("-fx-font-size:11;");
                        coordonnees.getChildren().add(lblEmail);
                    }
                    if (f.getTelephone() != null && !f.getTelephone().isEmpty()) {
                        Label lblTel = new Label("📞 " + f.getTelephone());
                        lblTel.setStyle("-fx-font-size:11;");
                        coordonnees.getChildren().add(lblTel);
                    }
                    
                    HBox actions = new HBox(8);
                    actions.setStyle("-fx-pref-width:100;");
                    Button btnEdit = new Button("✏️");
                    btnEdit.setStyle("-fx-cursor:hand;");
                    btnEdit.setOnAction(e -> openEditSupplierPopup(f));
                    Button btnDelete = new Button("🗑️");
                    btnDelete.setStyle("-fx-cursor:hand;");
                    btnDelete.setOnAction(e -> deleteFournisseur(f.getIdFournisseur()));
                    actions.getChildren().addAll(btnEdit, btnDelete);
                    
                    row.getChildren().addAll(
                        createLabel(f.getRaisonSociale(), 180),
                        createLabel(f.getContact() != null ? f.getContact() : "", 150),
                        coordonnees,
                        createLabel(f.getAdresse() != null ? f.getAdresse() : "", 280),
                        actions
                    );
                    
                    suppliersContainer.getChildren().add(row);
                });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void deleteFournisseur(int idFournisseur) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer le fournisseur");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce fournisseur ?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    fournisseurService.supprimerFournisseur(idFournisseur);
                    loadFournisseurs();
                } catch (Exception e) {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Erreur");
                    error.setHeaderText("Impossible de supprimer");
                    error.setContentText("Erreur: " + e.getMessage());
                    error.showAndWait();
                }
            }
        });
    }
}
