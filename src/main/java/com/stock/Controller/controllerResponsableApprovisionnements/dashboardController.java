package com.stock.Controller.controllerResponsableApprovisionnements;

import com.stock.service.ProduitService;
import com.stock.service.CommandeService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.text.DecimalFormat;

public class dashboardController {

    @FXML
    private Label lblProduits;

    @FXML
    private Label lblFournisseurs;

    @FXML
    private Label lblCommandes;

    @FXML
    private Label lblStock;

    @FXML
    private Label lblRapports;

    @FXML
    private Button btnVoirDetails;

    @FXML
    private Label lblVoirTout;

    @FXML
    private Label lblVoirToutCommandes;

    @FXML
    private Label lblTotalProduits;

    @FXML
    private Label lblCommandesEnCours;

    @FXML
    private Label lblCommandesAttente;

    @FXML
    private Label lblAlertesRupture;

    @FXML
    private Label lblValeurStock;

    @FXML
    private VBox commandesContainer;

    @FXML
    private VBox alertesContainer;

    @FXML
    private javafx.scene.layout.HBox alertBanner;

    @FXML
    private Label lblAlertBannerTitle;

    @FXML
    private Label lblAlertBannerDetails;

    private ProduitService produitService;
    private CommandeService commandeService;

    @FXML
    public void initialize() {
        try {
            produitService = new ProduitService();
            commandeService = new CommandeService();
            loadDashboardData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Dashboard chargé !");

        if (lblProduits != null) {
            lblProduits.setOnMouseClicked(event -> goToProduits());
        }

        if (lblFournisseurs != null) {
            lblFournisseurs.setOnMouseClicked(event -> goToFournisseurs());
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

        if (btnVoirDetails != null) {
            btnVoirDetails.setOnAction(event -> goToStock());
        }

        if (lblVoirTout != null) {
            lblVoirTout.setOnMouseClicked(event -> goToStock());
        }

        if (lblVoirToutCommandes != null) {
            lblVoirToutCommandes.setOnMouseClicked(event -> goToCommandes());
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

    private void loadDashboardData() {
        try {
            int totalProduits = produitService.compterProduits();
            int commandesEnCours = commandeService.compterCommandesParStatut("VALIDEE");
            int commandesAttente = commandeService.compterCommandesParStatut("EN_ATTENTE");
            int alertesRupture = produitService.compterProduitsEnRupture();
            double valeurStock = produitService.calculerValeurStock();

            int totalCommandes = commandesEnCours + commandesAttente;
            
            lblTotalProduits.setText(String.valueOf(totalProduits));
            lblCommandesEnCours.setText(String.valueOf(totalCommandes));
            lblCommandesAttente.setText(commandesAttente + " en attente de livraison");
            lblAlertesRupture.setText(String.valueOf(alertesRupture));
            
            DecimalFormat df = new DecimalFormat("#,##0.00");
            lblValeurStock.setText(df.format(valeurStock) + " €");

            if (alertesRupture > 0) {
                alertBanner.setVisible(true);
                alertBanner.setManaged(true);
                lblAlertBannerTitle.setText(alertesRupture + " produit" + (alertesRupture > 1 ? "s" : "") + " en rupture de stock");
            }
            
            loadRecentCommandes();
            loadProduitsEnAlerte();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadRecentCommandes() {
        try {
            var commandes = commandeService.consulterTousLesBonsCommande();
            commandes.stream()
                .limit(5)
                .forEach(cmd -> {
                    HBox row = new HBox(20);
                    row.getChildren().addAll(
                        createLabel(cmd.getNumero(), 200),
                        createLabel(cmd.getFournisseur().getRaisonSociale(), 300),
                        createLabel(cmd.getStatut(), 120),
                        createLabel(String.format("%.2f €", cmd.getMontantTotal()), 100)
                    );
                    commandesContainer.getChildren().add(row);
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
    
    private void loadProduitsEnAlerte() {
        try {
            var produits = produitService.consulterTousProduits();
            produits.stream()
                .filter(p -> p.getQuantiteStock() < p.getSeuilMin())
                .limit(5)
                .forEach(p -> {
                    HBox row = new HBox(10);
                    row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                    row.setStyle("-fx-border-color:#f0f0f0; -fx-border-width:0 0 1 0; -fx-padding:8 0;");
                    
                    VBox info = new VBox();
                    Label nom = new Label(p.getDesignation());
                    nom.setStyle("-fx-font-weight:bold;");
                    Label stock = new Label("Stock actuel: " + p.getQuantiteStock() + " / Seuil: " + p.getSeuilMin());
                    stock.setStyle("-fx-text-fill:#777777; -fx-font-size:11;");
                    info.getChildren().addAll(nom, stock);
                    
                    javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
                    HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
                    
                    Button btn = new Button("Rupture");
                    btn.setStyle("-fx-background-color:#e74c3c; -fx-text-fill:white; -fx-background-radius:6;");
                    
                    row.getChildren().addAll(info, spacer, btn);
                    alertesContainer.getChildren().add(row);
                });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
