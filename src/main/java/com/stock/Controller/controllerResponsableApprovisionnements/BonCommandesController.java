package com.stock.Controller.controllerResponsableApprovisionnements;

import com.stock.model.document.BonCommande;
import com.stock.service.CommandeService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.time.format.DateTimeFormatter;

public class BonCommandesController {

    @FXML
    private Label lblDashboard;

    @FXML
    private Label lblProduits;

    @FXML
    private Label lblFournisseurs;

    @FXML
    private Label lblStock;

    @FXML
    private Label lblRapports;

    @FXML
    private Button btnAddOrder;

    @FXML
    private TextField searchField;

    @FXML
    private VBox ordersContainer;

    private CommandeService commandeService;

    @FXML
    public void initialize() {
        try {
            commandeService = new CommandeService();
            loadCommandes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Page Commandes chargée !");

        if (lblDashboard != null) {
            lblDashboard.setOnMouseClicked(event -> goToDashboard());
        }

        if (lblProduits != null) {
            lblProduits.setOnMouseClicked(event -> goToProduits());
        }

        if (lblFournisseurs != null) {
            lblFournisseurs.setOnMouseClicked(event -> goToFournisseurs());
        }

        if (lblStock != null) {
            lblStock.setOnMouseClicked(event -> goToStock());
        }

        if (lblRapports != null) {
            lblRapports.setOnMouseClicked(event -> goToRapports());
        }
        
        if (searchField != null) {
            searchField.textProperty().addListener((obs, oldVal, newVal) -> filterCommandes(newVal));
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
    public void openAddOrderPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DashResponsableApprovisionnements/ajouterCommande.fxml"));
            Parent root = loader.load();
            AjouterCommandeController controller = loader.getController();
            controller.setOnCommandeAdded(() -> loadCommandes());
            
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setTitle("Créer un bon de commande");
            popup.setScene(new Scene(root, 800, 750));
            popup.setResizable(false);
            popup.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openEditOrderPopup(BonCommande commande) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DashResponsableApprovisionnements/modifierCommande.fxml"));
            Parent root = loader.load();
            ModifierCommandeController controller = loader.getController();
            controller.setCommande(commande);
            controller.setOnCommandeUpdated(() -> loadCommandes());
            
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setTitle("Modifier le bon de commande");
            popup.setScene(new Scene(root, 800, 750));
            popup.setResizable(false);
            popup.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadCommandes() {
        try {
            var commandes = commandeService.consulterTousLesBonsCommande();
            ordersContainer.getChildren().clear();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            commandes.forEach(c -> {
                HBox row = new HBox(10);
                row.setStyle("-fx-padding:16; -fx-border-color: transparent transparent #f0f0f0 transparent; -fx-border-width:0 0 1 0;");
                
                String statutStyle;
                switch (c.getStatut()) {
                    case "VALIDEE":
                    case "TRANSMISE":
                        statutStyle = "-fx-background-color:#e8e8e8; -fx-padding:4 8; -fx-background-radius:6; -fx-pref-width:100;";
                        break;
                    case "RECU":
                        statutStyle = "-fx-background-color:#2e86de; -fx-text-fill:white; -fx-padding:4 8; -fx-background-radius:6; -fx-pref-width:100;";
                        break;
                    case "ANNULEE":
                        statutStyle = "-fx-background-color:#e74c3c; -fx-text-fill:white; -fx-padding:4 8; -fx-background-radius:6; -fx-pref-width:100;";
                        break;
                    default:
                        statutStyle = "-fx-background-color:#fff3cd; -fx-padding:4 8; -fx-background-radius:6; -fx-pref-width:100;";
                }
                
                Label lblStatut = new Label(c.getStatut());
                lblStatut.setStyle(statutStyle);
                
                HBox actions = new HBox(8);
                actions.setStyle("-fx-pref-width:120;");
                Button btnEdit = new Button("✏️");
                btnEdit.setStyle("-fx-cursor:hand;");
                btnEdit.setOnAction(e -> openEditOrderPopup(c));
                Button btnDelete = new Button("🗑️");
                btnDelete.setStyle("-fx-cursor:hand;");
                btnDelete.setOnAction(e -> deleteCommande(c.getIdBonCommande()));
                actions.getChildren().addAll(btnEdit, btnDelete);
                
                int nbArticles = c.getLignes() != null ? c.getLignes().size() : 0;
                
                row.getChildren().addAll(
                    createLabel(c.getNumero(), 120),
                    createLabel(c.getFournisseur() != null ? c.getFournisseur().getRaisonSociale() : "", 140),
                    createLabel("📅 " + c.getDateCommande().format(formatter), 100),
                    createLabel(c.getDateLivraisonPrevue() != null ? c.getDateLivraisonPrevue().format(formatter) : "", 120),
                    createLabel(String.valueOf(nbArticles), 70),
                    createLabel(String.format("%.2f €", c.getMontantTotal()), 100),
                    lblStatut,
                    actions
                );
                
                ordersContainer.getChildren().add(row);
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
    
    private void filterCommandes(String searchText) {
        try {
            var commandes = commandeService.consulterTousLesBonsCommande();
            ordersContainer.getChildren().clear();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            commandes.stream()
                .filter(c -> searchText == null || searchText.isEmpty() || 
                    c.getNumero().toLowerCase().contains(searchText.toLowerCase()) ||
                    (c.getFournisseur() != null && c.getFournisseur().getRaisonSociale().toLowerCase().contains(searchText.toLowerCase())))
                .forEach(c -> {
                    HBox row = new HBox(10);
                    row.setStyle("-fx-padding:16; -fx-border-color: transparent transparent #f0f0f0 transparent; -fx-border-width:0 0 1 0;");
                    
                    String statutStyle;
                    switch (c.getStatut()) {
                        case "VALIDEE":
                        case "TRANSMISE":
                            statutStyle = "-fx-background-color:#e8e8e8; -fx-padding:4 8; -fx-background-radius:6; -fx-pref-width:100;";
                            break;
                        case "RECU":
                            statutStyle = "-fx-background-color:#2e86de; -fx-text-fill:white; -fx-padding:4 8; -fx-background-radius:6; -fx-pref-width:100;";
                            break;
                        case "ANNULEE":
                            statutStyle = "-fx-background-color:#e74c3c; -fx-text-fill:white; -fx-padding:4 8; -fx-background-radius:6; -fx-pref-width:100;";
                            break;
                        default:
                            statutStyle = "-fx-background-color:#fff3cd; -fx-padding:4 8; -fx-background-radius:6; -fx-pref-width:100;";
                    }
                    
                    Label lblStatut = new Label(c.getStatut());
                    lblStatut.setStyle(statutStyle);
                    
                    HBox actions = new HBox(8);
                    actions.setStyle("-fx-pref-width:120;");
                    Button btnEdit = new Button("✏️");
                    btnEdit.setStyle("-fx-cursor:hand;");
                    btnEdit.setOnAction(e -> openEditOrderPopup(c));
                    Button btnDelete = new Button("🗑️");
                    btnDelete.setStyle("-fx-cursor:hand;");
                    btnDelete.setOnAction(e -> deleteCommande(c.getIdBonCommande()));
                    actions.getChildren().addAll(btnEdit, btnDelete);
                    
                    int nbArticles = c.getLignes() != null ? c.getLignes().size() : 0;
                    
                    row.getChildren().addAll(
                        createLabel(c.getNumero(), 120),
                        createLabel(c.getFournisseur() != null ? c.getFournisseur().getRaisonSociale() : "", 140),
                        createLabel("📅 " + c.getDateCommande().format(formatter), 100),
                        createLabel(c.getDateLivraisonPrevue() != null ? c.getDateLivraisonPrevue().format(formatter) : "", 120),
                        createLabel(String.valueOf(nbArticles), 70),
                        createLabel(String.format("%.2f €", c.getMontantTotal()), 100),
                        lblStatut,
                        actions
                    );
                    
                    ordersContainer.getChildren().add(row);
                });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void deleteCommande(int idCommande) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer la commande");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette commande ?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    commandeService.supprimerBonCommande(idCommande);
                    loadCommandes();
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
