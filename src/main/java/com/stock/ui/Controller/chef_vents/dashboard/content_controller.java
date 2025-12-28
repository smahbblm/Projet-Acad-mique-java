package com.stock.ui.Controller.chef_vents.dashboard;

import com.stock.dao.implementation.FactureDAO;
import com.stock.dao.implementation.ClientDAO;
import com.stock.dao.implementation.ProduitDAO;
import com.stock.model.document.Facture;
import com.stock.model.produit.Produit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.chart.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class content_controller implements Initializable {

    @FXML private Button aujourdhuit, semaine, mois, annee;
    @FXML private Label facturesJour, chiffreAffaires, clientsActifs, panierMoyen;
    @FXML private Label badgeNotifications;
    @FXML private Label userNameLabel, userRoleLabel, userInitialsLabel;
    @FXML private PieChart diagrammeCategories;
    @FXML private LineChart<String, Number> evolutionCA;
    @FXML private BarChart<String, Number> topProduits;
    @FXML private VBox transactionsContainer;
    
    private String periodeSelectionnee = "semaine";
    private FactureDAO factureDAO;
    private ClientDAO clientDAO;
    private ProduitDAO produitDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            factureDAO = new FactureDAO();
            clientDAO = new ClientDAO();
            produitDAO = new ProduitDAO();
            
            chargerInfoUtilisateur();
            chargerStatistiques();
            chargerGraphiques();
            chargerTransactions();
            chargerNotifications();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void chargerInfoUtilisateur() {
        try {
            com.stock.util.SessionManager sessionManager = com.stock.util.SessionManager.getInstance();
            Object userObj = sessionManager.getCurrentUser();
            
            if (userObj != null && userObj instanceof com.stock.model.utilisateur.Utilisateur) {
                com.stock.model.utilisateur.Utilisateur user = (com.stock.model.utilisateur.Utilisateur) userObj;
                
                // Nom complet
                if (userNameLabel != null) {
                    userNameLabel.setText(user.getNom() + " " + user.getPrenom());
                }
                
                // Rôle
                if (userRoleLabel != null) {
                    userRoleLabel.setText(user.getRole());
                }
                
                // Initiales
                if (userInitialsLabel != null) {
                    String initials = "";
                    if (user.getNom() != null && !user.getNom().isEmpty()) {
                        initials += user.getNom().charAt(0);
                    }
                    if (user.getPrenom() != null && !user.getPrenom().isEmpty()) {
                        initials += user.getPrenom().charAt(0);
                    }
                    userInitialsLabel.setText(initials.toUpperCase());
                }
            }
        } catch (Exception e) {
            System.err.println("Érreur chargement info utilisateur: " + e.getMessage());
        }
    }

    @FXML
    private void nouvelleVente() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                getClass().getResource("/fxml/gestionChef-vents/factures/Facture.fxml")
            );
            javafx.scene.Parent root = loader.load();
            
            javafx.scene.Node node = chiffreAffaires.getScene().getRoot();
            javafx.scene.Parent parent = node.getParent();
            
            while (parent != null && !(parent instanceof javafx.scene.layout.BorderPane)) {
                parent = parent.getParent();
            }
            
            if (parent instanceof javafx.scene.layout.BorderPane) {
                ((javafx.scene.layout.BorderPane) parent).setCenter(root);
            }
        } catch (Exception e) {
            System.err.println("Erreur redirection nouvelle vente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void voirFactures() {
        nouvelleVente(); // Même redirection
    }
    
    @FXML
    private void genererRapport() {
        try {
            javafx.scene.control.Dialog<javafx.scene.control.ButtonType> dialog = new javafx.scene.control.Dialog<>();
            dialog.setTitle("Générer un Rapport");
            dialog.setHeaderText("Sélectionnez le type de rapport à générer");
            
            // Boutons
            javafx.scene.control.ButtonType btnVentes = new javafx.scene.control.ButtonType("Rapport des Ventes");
            javafx.scene.control.ButtonType btnCA = new javafx.scene.control.ButtonType("Rapport CA");
            javafx.scene.control.ButtonType btnClients = new javafx.scene.control.ButtonType("Rapport Clients");
            javafx.scene.control.ButtonType btnAnnuler = javafx.scene.control.ButtonType.CANCEL;
            
            dialog.getDialogPane().getButtonTypes().addAll(btnVentes, btnCA, btnClients, btnAnnuler);
            
            // Contenu
            VBox content = new VBox(15);
            content.setStyle("-fx-padding: 20;");
            
            Label label = new Label("Période du rapport:");
            label.setStyle("-fx-font-weight: bold;");
            
            javafx.scene.control.DatePicker dateDebut = new javafx.scene.control.DatePicker(LocalDate.now().minusMonths(1));
            javafx.scene.control.DatePicker dateFin = new javafx.scene.control.DatePicker(LocalDate.now());
            
            HBox dates = new HBox(10);
            dates.getChildren().addAll(
                new Label("Du:"), dateDebut,
                new Label("Au:"), dateFin
            );
            
            content.getChildren().addAll(label, dates);
            dialog.getDialogPane().setContent(content);
            
            dialog.showAndWait().ifPresent(response -> {
                if (response.equals(btnVentes)) {
                    genererRapportVentes(dateDebut.getValue(), dateFin.getValue());
                } else if (response.equals(btnCA)) {
                    genererRapportCA(dateDebut.getValue(), dateFin.getValue());
                } else if (response.equals(btnClients)) {
                    genererRapportClients(dateDebut.getValue(), dateFin.getValue());
                }
            });
            
        } catch (Exception e) {
            System.err.println("Erreur génération rapport: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void genererRapportVentes(LocalDate debut, LocalDate fin) {
        try {
            List<Facture> factures = factureDAO.readAll();
            int nbVentes = 0;
            double totalCA = 0;
            
            for (Facture f : factures) {
                if (f.getDateFacture() != null && 
                    !f.getDateFacture().isBefore(debut) && 
                    !f.getDateFacture().isAfter(fin)) {
                    nbVentes++;
                    totalCA += f.getMontantTTC();
                }
            }
            
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION
            );
            alert.setTitle("Rapport des Ventes");
            alert.setHeaderText("Période: " + debut + " au " + fin);
            alert.setContentText(
                "Nombre de ventes: " + nbVentes + "\n" +
                "Chiffre d'affaires: " + String.format("%.2f €", totalCA) + "\n" +
                "Panier moyen: " + String.format("%.2f €", nbVentes > 0 ? totalCA/nbVentes : 0)
            );
            alert.showAndWait();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void genererRapportCA(LocalDate debut, LocalDate fin) {
        try {
            List<Facture> factures = factureDAO.readAll();
            double totalHT = 0, totalTVA = 0, totalTTC = 0;
            
            for (Facture f : factures) {
                if (f.getDateFacture() != null && 
                    !f.getDateFacture().isBefore(debut) && 
                    !f.getDateFacture().isAfter(fin)) {
                    totalHT += f.getMontantHT();
                    totalTVA += f.getMontantTVA();
                    totalTTC += f.getMontantTTC();
                }
            }
            
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION
            );
            alert.setTitle("Rapport Chiffre d'Affaires");
            alert.setHeaderText("Période: " + debut + " au " + fin);
            alert.setContentText(
                "Montant HT: " + String.format("%.2f €", totalHT) + "\n" +
                "Montant TVA: " + String.format("%.2f €", totalTVA) + "\n" +
                "Montant TTC: " + String.format("%.2f €", totalTTC)
            );
            alert.showAndWait();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void genererRapportClients(LocalDate debut, LocalDate fin) {
        try {
            List<Facture> factures = factureDAO.readAll();
            Map<String, Double> clientsCA = new HashMap<>();
            
            for (Facture f : factures) {
                if (f.getDateFacture() != null && 
                    !f.getDateFacture().isBefore(debut) && 
                    !f.getDateFacture().isAfter(fin)) {
                    String client = f.getClientNom();
                    clientsCA.put(client, clientsCA.getOrDefault(client, 0.0) + f.getMontantTTC());
                }
            }
            
            StringBuilder rapport = new StringBuilder();
            clientsCA.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .limit(10)
                .forEach(e -> rapport.append(e.getKey()).append(": ")
                    .append(String.format("%.2f €", e.getValue())).append("\n"));
            
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION
            );
            alert.setTitle("Rapport Clients");
            alert.setHeaderText("Top 10 Clients - Période: " + debut + " au " + fin);
            alert.setContentText(rapport.toString());
            alert.showAndWait();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void chargerStatistiques() {
        try {
            List<Facture> factures = factureDAO.readAll();
            int nbFactures = 0;
            double totalCA = 0.0;
            LocalDate dateDebut = getDateDebut();
            
            for (Facture f : factures) {
                if (f.getDateFacture() != null && !f.getDateFacture().isBefore(dateDebut)) {
                    nbFactures++;
                    totalCA += f.getMontantTTC();
                }
            }
            
            int nbClients = clientDAO.readAll().size();
            double panier = nbFactures > 0 ? totalCA / nbFactures : 0;
            
            facturesJour.setText(String.valueOf(nbFactures));
            chiffreAffaires.setText(String.format("%.2f €", totalCA));
            clientsActifs.setText(String.valueOf(nbClients));
            panierMoyen.setText(String.format("%.2f €", panier));
            
        } catch (Exception e) {
            facturesJour.setText("0");
            chiffreAffaires.setText("0.00 €");
            clientsActifs.setText("0");
            panierMoyen.setText("0.00 €");
        }
    }
    
    private void chargerGraphiques() {
        chargerEvolutionCA();
        chargerTopProduits();
        chargerRepartitionVentes();
    }
    
    private void chargerEvolutionCA() {
        if (evolutionCA == null) return;
        
        try {
            List<Facture> factures = factureDAO.readAll();
            LocalDate dateDebut = getDateDebut();
            
            Map<LocalDate, Double> caParJour = new TreeMap<>();
            for (Facture f : factures) {
                if (f.getDateFacture() != null && !f.getDateFacture().isBefore(dateDebut)) {
                    caParJour.merge(f.getDateFacture(), (double) f.getMontantTTC(), Double::sum);
                }
            }
            
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("CA");
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
            for (Map.Entry<LocalDate, Double> entry : caParJour.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey().format(formatter), entry.getValue()));
            }
            
            evolutionCA.getData().clear();
            evolutionCA.getData().add(series);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void chargerTopProduits() {
        if (topProduits == null) return;
        
        try {
            List<Produit> produits = produitDAO.readAll();
            
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Quantité");
            
            produits.stream()
                .sorted((p1, p2) -> Integer.compare(p2.getQuantiteStock(), p1.getQuantiteStock()))
                .limit(5)
                .forEach(p -> series.getData().add(new XYChart.Data<>(p.getDesignation(), p.getQuantiteStock())));
            
            topProduits.getData().clear();
            topProduits.getData().add(series);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void chargerRepartitionVentes() {
        if (diagrammeCategories == null) return;
        
        try {
            List<Produit> produits = produitDAO.readAll();
            
            System.out.println("=== DEBUG RÉPARTITION ===");
            System.out.println("Nombre de produits: " + produits.size());
            
            if (produits.isEmpty()) {
                System.out.println("Aucun produit dans la base!");
                diagrammeCategories.getData().clear();
                diagrammeCategories.getData().add(new PieChart.Data("Aucune donnée", 1));
                return;
            }
            
            Map<String, Integer> categorieCount = new HashMap<>();
            for (Produit p : produits) {
                String cat = (p.getCategorie() != null && !p.getCategorie().isEmpty()) 
                    ? p.getCategorie() : "Non catégorisé";
                System.out.println("Produit: " + p.getDesignation() + " - Catégorie: " + cat);
                categorieCount.put(cat, categorieCount.getOrDefault(cat, 0) + 1);
            }
            
            System.out.println("Catégories trouvées: " + categorieCount);
            
            diagrammeCategories.getData().clear();
            categorieCount.forEach((cat, count) -> 
                diagrammeCategories.getData().add(new PieChart.Data(cat, count))
            );
            
        } catch (Exception e) {
            System.err.println("Erreur chargement répartition: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void chargerTransactions() {
        if (transactionsContainer == null) return;
        
        try {
            List<Facture> factures = factureDAO.readAll();
            transactionsContainer.getChildren().clear();
            
            factures.stream()
                .sorted((f1, f2) -> f2.getDateFacture().compareTo(f1.getDateFacture()))
                .limit(3)
                .forEach(f -> transactionsContainer.getChildren().add(creerTransactionCard(f)));
                
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private HBox creerTransactionCard(Facture facture) {
        HBox card = new HBox(12);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color: #f8fafc; -fx-padding: 12; -fx-background-radius: 10;");
        
        // Icône
        StackPane icon = new StackPane();
        icon.setStyle("-fx-background-color: #d1fae5; -fx-background-radius: 8; -fx-pref-width: 32; -fx-pref-height: 32;");
        Label iconLabel = new Label("✓");
        iconLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #10b981; -fx-font-weight: bold;");
        icon.getChildren().add(iconLabel);
        
        // Info
        VBox info = new VBox(3);
        info.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(info, javafx.scene.layout.Priority.ALWAYS);
        
        Label numero = new Label(facture.getNumero() != null ? facture.getNumero() : "N/A");
        numero.setStyle("-fx-font-weight: bold; -fx-font-size: 12px; -fx-text-fill: #0f172a;");
        
        Label client = new Label(facture.getClientNom());
        client.setStyle("-fx-font-size: 11px; -fx-text-fill: #64748b;");
        
        Label date = new Label(facture.getDateFacture().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        date.setStyle("-fx-font-size: 10px; -fx-text-fill: #94a3b8;");
        
        info.getChildren().addAll(numero, client, date);
        
        // Montant
        VBox montantBox = new VBox(3);
        montantBox.setAlignment(Pos.CENTER_RIGHT);
        
        Label montant = new Label(String.format("%.2f €", facture.getMontantTTC()));
        montant.setStyle("-fx-font-weight: bold; -fx-text-fill: #0f172a; -fx-font-size: 14px;");
        
        Label statut = new Label("Payée");
        statut.setStyle("-fx-font-size: 10px; -fx-text-fill: #10b981; -fx-background-color: #d1fae5; -fx-padding: 3 8; -fx-background-radius: 6; -fx-font-weight: 600;");
        
        montantBox.getChildren().addAll(montant, statut);
        
        card.getChildren().addAll(icon, info, montantBox);
        return card;
    }
    
    private LocalDate getDateDebut() {
        LocalDate now = LocalDate.now();
        switch (periodeSelectionnee) {
            case "aujourdhuit": return now;
            case "semaine": return now.minusWeeks(1);
            case "mois": return now.minusMonths(1);
            case "annee": return now.minusYears(1);
            default: return now.minusWeeks(1);
        }
    }

    @FXML
    private void selectPeriod(ActionEvent event) {
        resetAllButtons();
        Button clickedButton = (Button) event.getSource();
        periodeSelectionnee = clickedButton.getId();
        clickedButton.setStyle("-fx-background-color: white; -fx-text-fill: #0f172a; -fx-padding: 6 12; -fx-background-radius: 6; -fx-font-weight: 600;");
        chargerStatistiques();
        chargerGraphiques();
    }

    private void resetAllButtons() {
        String defaultStyle = "-fx-background-color: transparent; -fx-text-fill: #64748b; -fx-padding: 6 12; -fx-background-radius: 6;";
        aujourdhuit.setStyle(defaultStyle);
        semaine.setStyle(defaultStyle);
        mois.setStyle(defaultStyle);
        annee.setStyle(defaultStyle);
    }
    
    @FXML
    public void afficherNotifications() {
        try {
            List<String> notifications = new ArrayList<>();
            
            // Produits en rupture de stock
            List<Produit> produits = produitDAO.readAll();
            int produitsRupture = 0;
            for (Produit p : produits) {
                if (p.verifierSeuil()) {
                    produitsRupture++;
                    notifications.add("⚠ Stock faible: " + p.getDesignation() + " (" + p.getQuantiteStock() + " restants)");
                }
            }
            
            // Factures en attente
            List<Facture> factures = factureDAO.readAll();
            int facturesEnAttente = 0;
            for (Facture f : factures) {
                if ("BROUILLON".equals(f.getStatut()) || "GENEREE".equals(f.getStatut())) {
                    facturesEnAttente++;
                    notifications.add("📄 Facture en attente: " + f.getNumero());
                }
            }
            
            // Afficher dans une alerte
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION
            );
            alert.setTitle("Notifications");
            alert.setHeaderText("Vous avez " + notifications.size() + " notification(s)");
            
            if (notifications.isEmpty()) {
                alert.setContentText("✅ Aucune notification pour le moment");
            } else {
                StringBuilder content = new StringBuilder();
                notifications.stream().limit(10).forEach(n -> content.append(n).append("\n"));
                alert.setContentText(content.toString());
            }
            
            alert.showAndWait();
            
        } catch (Exception e) {
            System.err.println("Erreur affichage notifications: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void chargerNotifications() {
        try {
            int nbNotifications = 0;
            
            // Compter produits en rupture
            List<Produit> produits = produitDAO.readAll();
            for (Produit p : produits) {
                if (p.verifierSeuil()) {
                    nbNotifications++;
                }
            }
            
            // Compter factures en attente
            List<Facture> factures = factureDAO.readAll();
            for (Facture f : factures) {
                if ("BROUILLON".equals(f.getStatut()) || "GENEREE".equals(f.getStatut())) {
                    nbNotifications++;
                }
            }
            
            // Mettre à jour le badge
            if (badgeNotifications != null) {
                if (nbNotifications > 0) {
                    badgeNotifications.setText(String.valueOf(nbNotifications));
                    badgeNotifications.setVisible(true);
                } else {
                    badgeNotifications.setVisible(false);
                }
            }
            
        } catch (Exception e) {
            System.err.println("Erreur chargement notifications: " + e.getMessage());
        }
    }
}
