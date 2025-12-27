package com.stock.ui.Controller.chef_vents.consultation;

import com.stock.model.produit.Produit;
import com.stock.service.ProduitService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contrôleur pour la consultation de la disponibilité des produits
 */
public class DisponibiliteProduitsController {

    // Service
    private ProduitService produitService;

    // TableView et colonnes
    @FXML private TableView<Produit> tableProduits;
    @FXML private TableColumn<Produit, String> colReference;
    @FXML private TableColumn<Produit, String> colNomProduit;
    @FXML private TableColumn<Produit, String> colCategorie;
    @FXML private TableColumn<Produit, Integer> colQuantiteStock;
    @FXML private TableColumn<Produit, Integer> colSeuilMin;
    @FXML private TableColumn<Produit, String> colStatutStock;
    @FXML private TableColumn<Produit, Float> colPrixVente;
    @FXML private TableColumn<Produit, Void> colActions;

    // Filtres
    @FXML private ComboBox<String> filtreCategorie;
    @FXML private ComboBox<String> filtreStatutStock;
    @FXML private ComboBox<String> filtreFournisseur;
    @FXML private TextField rechercheField;

    // Statistiques
    @FXML private Text totalProduitsNumber;
    @FXML private Text stockBasNumber;
    @FXML private Text stockDisponibleNumber;
    @FXML private Text stockEpuiseNumber;

    // Boutons
    @FXML private Button btnExporter;
    @FXML private Button btnActualiser;
    @FXML private Button btnAlertes;

    // Liste des produits
    private ObservableList<Produit> produitsList;
    private ObservableList<Produit> produitsFiltered;

    /**
     * Initialisation du contrôleur
     */
    @FXML
    public void initialize() {
        System.out.println("Initialisation de DisponibiliteProduitsController");

        try {
            // Initialiser le service
            produitService = new ProduitService();

            // Configurer la table
            configurerTable();

            // Configurer les filtres
            configurerFiltres();

            // Charger les données
            chargerProduits();

            // Configurer les listeners
            configurerListeners();

        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation: " + e.getMessage());
            e.printStackTrace();
            afficherErreur("Erreur d'initialisation",
                    "Impossible d'initialiser le contrôleur: " + e.getMessage());
        }
    }

    /**
     * Configurer les colonnes de la table
     */
    private void configurerTable() {
        // Configuration des colonnes simples
        colReference.setCellValueFactory(new PropertyValueFactory<>("reference"));
        colNomProduit.setCellValueFactory(new PropertyValueFactory<>("designation"));
        colCategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        colQuantiteStock.setCellValueFactory(new PropertyValueFactory<>("quantiteStock"));
        colSeuilMin.setCellValueFactory(new PropertyValueFactory<>("seuilMin"));
        colPrixVente.setCellValueFactory(new PropertyValueFactory<>("prixVente"));

        // Colonne Statut avec logique personnalisée
        colStatutStock.setCellValueFactory(cellData -> {
            Produit produit = cellData.getValue();
            String statut;

            if (produit.getQuantiteStock() == 0) {
                statut = "⛔ Épuisé";
            } else if (produit.verifierSeuil()) {
                statut = "⚠️ Stock bas";
            } else {
                statut = "✅ Disponible";
            }

            return new SimpleStringProperty(statut);
        });

        // Style des cellules de statut
        colStatutStock.setCellFactory(column -> new TableCell<Produit, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);

                    if (item.contains("Épuisé")) {
                        setStyle("-fx-text-fill: #d32f2f; -fx-font-weight: bold;");
                    } else if (item.contains("Stock bas")) {
                        setStyle("-fx-text-fill: #f57c00; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #388e3c; -fx-font-weight: bold;");
                    }
                }
            }
        });

        // Colonne Actions avec boutons
        colActions.setCellFactory(param -> new TableCell<Produit, Void>() {
            private final Button btnDetails = new Button("👁️");
            private final HBox container = new HBox(5, btnDetails);

            {
                btnDetails.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-cursor: hand;");
                btnDetails.setOnAction(event -> {
                    Produit produit = getTableView().getItems().get(getIndex());
                    afficherDetailsProduit(produit);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : container);
            }
        });

        // Style des lignes selon le statut du stock
        tableProduits.setRowFactory(tv -> new TableRow<Produit>() {
            @Override
            protected void updateItem(Produit produit, boolean empty) {
                super.updateItem(produit, empty);

                if (produit == null || empty) {
                    setStyle("");
                } else if (produit.getQuantiteStock() == 0) {
                    setStyle("-fx-background-color: #ffebee;"); // Rouge très clair
                } else if (produit.verifierSeuil()) {
                    setStyle("-fx-background-color: #fff3e0;"); // Orange très clair
                }
            }
        });
    }

    /**
     * Configurer les filtres
     */
    private void configurerFiltres() {
        // Filtre par catégorie
        ObservableList<String> categories = FXCollections.observableArrayList(
                "Toutes les catégories",
                "Électronique",
                "Informatique",
                "Mobilier",
                "Fournitures",
                "Alimentation",
                "Vêtements",
                "Autres"
        );
        filtreCategorie.setItems(categories);
        filtreCategorie.setValue("Toutes les catégories");

        // Filtre par statut de stock
        ObservableList<String> statutsStock = FXCollections.observableArrayList(
                "Tous les statuts",
                "✅ Disponible",
                "⚠️ Stock bas",
                "⛔ Épuisé"
        );
        filtreStatutStock.setItems(statutsStock);
        filtreStatutStock.setValue("Tous les statuts");

        // Filtre par fournisseur (à adapter selon vos besoins)
        ObservableList<String> fournisseurs = FXCollections.observableArrayList(
                "Tous les fournisseurs"
        );
        filtreFournisseur.setItems(fournisseurs);
        filtreFournisseur.setValue("Tous les fournisseurs");
    }

    /**
     * Configurer les listeners pour les filtres et recherche
     */
    private void configurerListeners() {
        // Listener pour la recherche en temps réel
        rechercheField.textProperty().addListener((observable, oldValue, newValue) -> {
            appliquerFiltres();
        });

        // Listeners pour les filtres
        filtreCategorie.valueProperty().addListener((observable, oldValue, newValue) -> {
            appliquerFiltres();
        });

        filtreStatutStock.valueProperty().addListener((observable, oldValue, newValue) -> {
            appliquerFiltres();
        });

        filtreFournisseur.valueProperty().addListener((observable, oldValue, newValue) -> {
            appliquerFiltres();
        });
    }

    /**
     * Charger tous les produits
     */
    private void chargerProduits() {
        try {
            System.out.println("Chargement des produits...");
            List<Produit> produits = produitService.consulterTousProduits();
            System.out.println("Nombre de produits récupérés: " + produits.size());

            produitsList = FXCollections.observableArrayList(produits);
            produitsFiltered = FXCollections.observableArrayList(produits);

            tableProduits.setItems(produitsFiltered);

            // Mettre à jour les statistiques
            mettreAJourStatistiques();

            System.out.println("Produits chargés avec succès");

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des produits: " + e.getMessage());
            e.printStackTrace();
            afficherErreur("Erreur de chargement",
                    "Impossible de charger les produits: " + e.getMessage());
        }
    }

    /**
     * Appliquer les filtres sélectionnés
     */
    private void appliquerFiltres() {
        if (produitsList == null) return;

        String recherche = rechercheField.getText().toLowerCase();
        String categorieSelectionnee = filtreCategorie.getValue();
        String statutSelectionne = filtreStatutStock.getValue();

        List<Produit> produitsFiltres = produitsList.stream()
                .filter(p -> {
                    // Filtre par recherche (référence ou désignation)
                    boolean matchRecherche = recherche.isEmpty() ||
                            p.getReference().toLowerCase().contains(recherche) ||
                            p.getDesignation().toLowerCase().contains(recherche);

                    // Filtre par catégorie
                    boolean matchCategorie = categorieSelectionnee.equals("Toutes les catégories") ||
                            p.getCategorie().equals(categorieSelectionnee);

                    // Filtre par statut de stock
                    boolean matchStatut = true;
                    if (statutSelectionne.contains("Disponible")) {
                        matchStatut = p.getQuantiteStock() > p.getSeuilMin();
                    } else if (statutSelectionne.contains("Stock bas")) {
                        matchStatut = p.verifierSeuil() && p.getQuantiteStock() > 0;
                    } else if (statutSelectionne.contains("Épuisé")) {
                        matchStatut = p.getQuantiteStock() == 0;
                    }

                    return matchRecherche && matchCategorie && matchStatut;
                })
                .collect(Collectors.toList());

        produitsFiltered = FXCollections.observableArrayList(produitsFiltres);
        tableProduits.setItems(produitsFiltered);

        // Mettre à jour les statistiques avec les produits filtrés
        mettreAJourStatistiques();
    }

    /**
     * Mettre à jour les statistiques affichées
     */
    private void mettreAJourStatistiques() {
        if (produitsList == null) return;

        int total = produitsList.size();
        long stockBas = produitsList.stream()
                .filter(p -> p.verifierSeuil() && p.getQuantiteStock() > 0)
                .count();
        long stockDisponible = produitsList.stream()
                .filter(p -> p.getQuantiteStock() > p.getSeuilMin())
                .count();
        long stockEpuise = produitsList.stream()
                .filter(p -> p.getQuantiteStock() == 0)
                .count();

        totalProduitsNumber.setText(String.valueOf(total));
        stockBasNumber.setText(String.valueOf(stockBas));
        stockDisponibleNumber.setText(String.valueOf(stockDisponible));
        stockEpuiseNumber.setText(String.valueOf(stockEpuise));
    }

    /**
     * Actualiser le stock
     */
    @FXML
    public void actualiserStock() {
        System.out.println("Actualisation du stock");
        chargerProduits();
        afficherInfo("Actualisation", "La liste des produits a été actualisée.");
    }

    /**
     * Exporter le stock dans un fichier CSV
     */
    @FXML
    public void exporterStock() {
        System.out.println("Export du stock");

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Exporter le stock");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Fichier CSV", "*.csv")
            );
            fileChooser.setInitialFileName("stock_produits.csv");

            File file = fileChooser.showSaveDialog(btnExporter.getScene().getWindow());

            if (file != null) {
                try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                    // En-têtes
                    writer.println("Référence;Désignation;Catégorie;Quantité Stock;Seuil Min;Seuil Max;Prix Achat;Prix Vente;Statut;Date Ajout");

                    // Données
                    for (Produit p : produitsList) {
                        String statut;
                        if (p.getQuantiteStock() == 0) {
                            statut = "Épuisé";
                        } else if (p.verifierSeuil()) {
                            statut = "Stock bas";
                        } else {
                            statut = "Disponible";
                        }

                        writer.println(String.format("%s;%s;%s;%d;%d;%d;%.2f;%.2f;%s;%s",
                                p.getReference(),
                                p.getDesignation(),
                                p.getCategorie(),
                                p.getQuantiteStock(),
                                p.getSeuilMin(),
                                p.getSeuilMax(),
                                p.getPrixAchat(),
                                p.getPrixVente(),
                                statut,
                                p.getDateAjout().format(DateTimeFormatter.ISO_LOCAL_DATE)
                        ));
                    }

                    afficherInfo("Export réussi",
                            "Le fichier a été exporté avec succès vers:\n" + file.getAbsolutePath());

                } catch (Exception e) {
                    afficherErreur("Erreur d'export",
                            "Erreur lors de l'écriture du fichier: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            afficherErreur("Erreur d'export",
                    "Erreur lors de l'export: " + e.getMessage());
        }
    }

    /**
     * Afficher les alertes de stock
     */
    @FXML
    public void voirAlertes() {
        System.out.println("Affichage des alertes de stock");

        List<Produit> produitsEnAlerte = produitsList.stream()
                .filter(p -> p.getQuantiteStock() == 0 || p.verifierSeuil())
                .collect(Collectors.toList());

        if (produitsEnAlerte.isEmpty()) {
            afficherInfo("Aucune alerte", "Aucun produit n'est en situation d'alerte.");
            return;
        }

        StringBuilder message = new StringBuilder();
        message.append("⚠️ ALERTES DE STOCK ⚠️\n\n");

        long epuises = produitsEnAlerte.stream().filter(p -> p.getQuantiteStock() == 0).count();
        long stockBas = produitsEnAlerte.stream().filter(p -> p.verifierSeuil() && p.getQuantiteStock() > 0).count();

        message.append(String.format("📊 Résumé:\n"));
        message.append(String.format("   - Produits épuisés: %d\n", epuises));
        message.append(String.format("   - Produits en stock bas: %d\n\n", stockBas));

        message.append("🔴 Produits épuisés:\n");
        produitsEnAlerte.stream()
                .filter(p -> p.getQuantiteStock() == 0)
                .limit(5)
                .forEach(p -> message.append(String.format("   • %s - %s\n",
                        p.getReference(), p.getDesignation())));

        if (epuises > 5) {
            message.append(String.format("   ... et %d autres\n", epuises - 5));
        }

        message.append("\n🟠 Produits en stock bas:\n");
        produitsEnAlerte.stream()
                .filter(p -> p.verifierSeuil() && p.getQuantiteStock() > 0)
                .limit(5)
                .forEach(p -> message.append(String.format("   • %s - %s (Stock: %d / Seuil: %d)\n",
                        p.getReference(), p.getDesignation(),
                        p.getQuantiteStock(), p.getSeuilMin())));

        if (stockBas > 5) {
            message.append(String.format("   ... et %d autres\n", stockBas - 5));
        }

        afficherAlerte("Alertes de Stock", message.toString());
    }

    /**
     * Afficher les détails d'un produit
     */
    private void afficherDetailsProduit(Produit produit) {
        StringBuilder details = new StringBuilder();
        details.append("📦 DÉTAILS DU PRODUIT\n\n");
        details.append(String.format("Référence: %s\n", produit.getReference()));
        details.append(String.format("Désignation: %s\n", produit.getDesignation()));
        details.append(String.format("Description: %s\n",
                produit.getDescription() != null ? produit.getDescription() : "N/A"));
        details.append(String.format("Catégorie: %s\n\n", produit.getCategorie()));

        details.append("💰 PRIX\n");
        details.append(String.format("Prix d'achat: %.2f DH\n", produit.getPrixAchat()));
        details.append(String.format("Prix de vente: %.2f DH\n", produit.getPrixVente()));
        details.append(String.format("Marge: %.2f DH (%.1f%%)\n\n",
                produit.getPrixVente() - produit.getPrixAchat(),
                ((produit.getPrixVente() - produit.getPrixAchat()) / produit.getPrixAchat()) * 100));

        details.append("📊 STOCK\n");
        details.append(String.format("Quantité en stock: %d\n", produit.getQuantiteStock()));
        details.append(String.format("Seuil minimum: %d\n", produit.getSeuilMin()));
        details.append(String.format("Seuil maximum: %d\n", produit.getSeuilMax()));

        String statut;
        if (produit.getQuantiteStock() == 0) {
            statut = "⛔ Épuisé";
        } else if (produit.verifierSeuil()) {
            statut = "⚠️ Stock bas";
        } else {
            statut = "✅ Disponible";
        }
        details.append(String.format("Statut: %s\n\n", statut));

        details.append(String.format("📅 Date d'ajout: %s",
                produit.getDateAjout().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

        afficherInfo("Détails du produit", details.toString());
    }

    /**
     * Afficher un message d'information
     */
    private void afficherInfo(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Afficher un message d'alerte
     */
    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Afficher un message d'erreur
     */
    private void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}