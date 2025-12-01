package com.stock.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.stock.model.produit.Produit;
import com.stock.dao.implementation.ProduitDAO;
import com.stock.util.AlertUtils;
import com.stock.util.NavigationManager;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;

import java.util.List;

/**
 * Contrôleur pour la gestion des produits (Approvisionnement)
 */
public class GestionProduitsController {

    @FXML private TextField txtRecherche;
    @FXML private ComboBox<String> comboCategorie;
    @FXML private Button btnRecherche;

    @FXML private TableView<Produit> tableProduits;
    @FXML private TableColumn<Produit, Integer> colId;
    @FXML private TableColumn<Produit, String> colNom;
    @FXML private TableColumn<Produit, String> colCategorie;
    @FXML private TableColumn<Produit, Float> colPrixAchat;
    @FXML private TableColumn<Produit, Integer> colQuantiteStock;
    @FXML private TableColumn<Produit, Integer> colSeuilMin;
    @FXML private TableColumn<Produit, String> colStatut;
    @FXML private TableColumn<Produit, String> colReference;
    @FXML private TableColumn<Produit, Float> colPrixVente;
    @FXML private TableColumn<Produit, Integer> colSeuilMax;
    @FXML private TableColumn<Produit, java.time.LocalDate> colDateAjout;
    @FXML private TableColumn<Produit, String> colDescription;

    @FXML private Label lblTotal;
    @FXML private Button btnAjouter;
    @FXML private Button btnModifier;
    @FXML private Button btnSupprimer;
    @FXML private Button btnRetour;

    private ProduitDAO produitDAO;
    private ObservableList<Produit> produits;

    @FXML
    public void initialize() {
        try {
            produitDAO = new ProduitDAO();
            produits = FXCollections.observableArrayList();

            initTable();
            chargerCategories();
            chargerProduits();

        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Erreur lors de l'initialisation: " + e.getMessage());
        }
    }

    private void initTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idProduit"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("designation"));
        colCategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        colPrixAchat.setCellValueFactory(new PropertyValueFactory<>("prixAchat"));
        colQuantiteStock.setCellValueFactory(new PropertyValueFactory<>("quantiteStock"));
        colSeuilMin.setCellValueFactory(new PropertyValueFactory<>("seuilMin"));
        colReference.setCellValueFactory(new PropertyValueFactory<>("reference"));
        colPrixVente.setCellValueFactory(new PropertyValueFactory<>("prixVente"));
        colSeuilMax.setCellValueFactory(new PropertyValueFactory<>("seuilMax"));
        colDateAjout.setCellValueFactory(new PropertyValueFactory<>("dateAjout"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Format date
        colDateAjout.setCellFactory(col -> new TableCell<Produit, java.time.LocalDate>() {
            @Override protected void updateItem(java.time.LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item==null) setText(null); else setText(item.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))); }
        });

        // Colonne statut personnalisée
        colStatut.setCellFactory(column -> new TableCell<Produit, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                } else {
                    Produit produit = getTableRow().getItem();
                    if (produit.getQuantiteStock() == 0) {
                        setText("Rupture");
                        setStyle("-fx-text-fill: red;");
                    } else if (produit.getQuantiteStock() < produit.getSeuilMin()) {
                        setText("Alerte");
                        setStyle("-fx-text-fill: orange;");
                    } else {
                        setText("OK");
                        setStyle("-fx-text-fill: green;");
                    }
                }
            }
        });
    }

    private void chargerCategories() {
        try {
            // Charger les catégories depuis la base de données
            List<String> categories = produitDAO.getCategories();
            comboCategorie.setItems(FXCollections.observableArrayList(categories));
        } catch (Exception e) {
            System.err.println("Erreur chargement catégories: " + e.getMessage());
        }
    }

    private void chargerProduits() {
        try {
            List<Produit> listeProduits = produitDAO.readAll();
            if (listeProduits != null && !listeProduits.isEmpty()) {
                produits.setAll(listeProduits);
                tableProduits.setItems(produits);
                lblTotal.setText("Total: " + produits.size());
            } else {
                // Si DAO retourne null, charger depuis la base directement
                chargerProduitsDepuisBD();
            }
        } catch (Exception e) {
            System.err.println("Erreur chargement produits: " + e.getMessage());
            chargerProduitsDepuisBD();
        }
    }

    private void chargerProduitsDepuisBD() {
        try {
            produits.clear();
            var conn = com.stock.util.DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT * FROM produits ORDER BY designation";
            try (var stmt = conn.prepareStatement(sql);
                 var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Produit p = new Produit(
                        rs.getString("reference"),
                        rs.getString("designation"),
                        rs.getFloat("prixAchat"),
                        rs.getFloat("prixVente"),
                        rs.getInt("quantiteStock"),
                        rs.getInt("seuilMin"),
                        rs.getInt("seuilMax"),
                        rs.getString("categorie")
                    );
                    p.setIdProduit(rs.getInt("idProduit"));
                    if (rs.getDate("dateAjout") != null) {
                        p.setDateAjout(rs.getDate("dateAjout").toLocalDate());
                    }
                    produits.add(p);
                }
            }
            tableProduits.setItems(produits);
            lblTotal.setText("Total: " + produits.size());
        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Impossible de charger les produits: " + e.getMessage());
        }
    }

    @FXML
    private void handleRecherche() {
        String recherche = txtRecherche.getText().toLowerCase();
        String categorie = comboCategorie.getValue();

        ObservableList<Produit> filtered = FXCollections.observableArrayList();

        for (Produit p : produits) {
            boolean matchRecherche = recherche.isEmpty() ||
                p.getDesignation().toLowerCase().contains(recherche) ||
                p.getReference().toLowerCase().contains(recherche);

            boolean matchCategorie = categorie == null || categorie.isEmpty() ||
                categorie.equals(p.getCategorie());

            if (matchRecherche && matchCategorie) {
                filtered.add(p);
            }
        }

        tableProduits.setItems(filtered);
        lblTotal.setText("Total: " + filtered.size());
    }

    private boolean referenceExiste(String ref) {
        try {
            return produitDAO.findByReference(ref) != null;
        } catch (Exception e) {
            return false;
        }
    }

    @FXML
    private void handleAjouter() {
        // Création de la boîte de dialogue
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un produit");
        dialog.setHeaderText("Veuillez remplir les informations du produit");

        // Création des champs de saisie
        TextField refField = new TextField();
        refField.setPromptText("Référence");

        TextField desField = new TextField();
        desField.setPromptText("Désignation");

        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Description (optionnelle)");
        descriptionArea.setPrefRowCount(3);
        descriptionArea.setWrapText(true);

        TextField prixAchatField = new TextField();
        prixAchatField.setPromptText("Prix d'achat");

        TextField prixVenteField = new TextField();
        prixVenteField.setPromptText("Prix de vente");

        TextField quantiteField = new TextField();
        quantiteField.setPromptText("Quantité en stock");

        TextField seuilMinField = new TextField();
        seuilMinField.setPromptText("Seuil minimum");

        TextField seuilMaxField = new TextField();
        seuilMaxField.setPromptText("Seuil maximum");

        ComboBox<String> categorieField = new ComboBox<>();
        categorieField.setPromptText("Catégorie");
        categorieField.setEditable(true);

        // Remplir le ComboBox des catégories
        try {
            List<String> categories = produitDAO.getCategories();
            categorieField.setItems(FXCollections.observableArrayList(categories));
        } catch (Exception e) {
            System.err.println("Erreur chargement catégories: " + e.getMessage());
        }

        // Création des boutons
        ButtonType ajouterBtn = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        ButtonType annulerBtn = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(ajouterBtn, annulerBtn);

        // Ajout des champs au contenu de la boîte de dialogue
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.add(new Label("Référence:"), 0, 0);
        grid.add(refField, 1, 0);
        grid.add(new Label("Désignation:"), 0, 1);
        grid.add(desField, 1, 1);
        grid.add(new Label("Description:"), 0, 2);
        grid.add(descriptionArea, 1, 2);
        grid.add(new Label("Prix d'achat:"), 0, 3);
        grid.add(prixAchatField, 1, 3);
        grid.add(new Label("Prix de vente:"), 0, 4);
        grid.add(prixVenteField, 1, 4);
        grid.add(new Label("Quantité en stock:"), 0, 5);
        grid.add(quantiteField, 1, 5);
        grid.add(new Label("Seuil minimum:"), 0, 6);
        grid.add(seuilMinField, 1, 6);
        grid.add(new Label("Seuil maximum:"), 0, 7);
        grid.add(seuilMaxField, 1, 7);
        grid.add(new Label("Catégorie:"), 0, 8);
        grid.add(categorieField, 1, 8);
        dialog.getDialogPane().setContent(grid);

        // Gestion des événements de bouton
        dialog.setResultConverter(btn -> {
            if (btn == ajouterBtn) {
                try {
                    String ref = refField.getText().trim();
                    String designation = desField.getText().trim();
                    if (ref.isEmpty() || designation.isEmpty()) {
                        AlertUtils.showWarning("Attention","Référence et désignation obligatoires"); return null;
                    }
                    if (referenceExiste(ref)) {
                        AlertUtils.showWarning("Attention","Référence déjà utilisée"); return null;
                    }
                    float prixAchat = Float.parseFloat(prixAchatField.getText().trim());
                    float prixVente = Float.parseFloat(prixVenteField.getText().trim());
                    int quantiteStock = Integer.parseInt(quantiteField.getText().trim());
                    int seuilMin = Integer.parseInt(seuilMinField.getText().trim());
                    int seuilMax = Integer.parseInt(seuilMaxField.getText().trim());
                    if (prixAchat < 0 || prixVente < 0) {
                        AlertUtils.showWarning("Attention","Les prix ne peuvent pas être négatifs"); return null;
                    }
                    if (seuilMin > seuilMax) {
                        AlertUtils.showWarning("Attention","Seuil minimum > seuil maximum"); return null;
                    }
                    String categorie = categorieField.getValue();

                    // Création du produit
                    Produit nouveauProduit = new Produit(ref, designation, prixAchat, prixVente, quantiteStock, seuilMin, seuilMax, categorie);
                    nouveauProduit.setDescription(descriptionArea.getText().trim());

                    // Insertion dans la base de données
                    produitDAO.create(nouveauProduit);

                    // Rafraîchir la liste
                    rafraichirProduits();

                    AlertUtils.showInfo("Succès", "Produit ajouté avec succès");
                } catch (NumberFormatException nfe) {
                    AlertUtils.showError("Erreur", "Valeurs numériques invalides"); return null;
                } catch (Exception e) {
                    AlertUtils.showError("Erreur", "Erreur lors de l'ajout: " + e.getMessage()); return null;
                }
            }
            return btn;
        });

        dialog.showAndWait();
    }

    private void rafraichirProduits() { chargerProduits(); }

    @FXML
    private void handleModifier() {
        Produit selected = tableProduits.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Attention", "Veuillez sélectionner un produit à modifier");
            return;
        }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Modifier un produit");
        dialog.setHeaderText("Mettre à jour les informations du produit");

        TextField refField = new TextField(selected.getReference());
        TextField desField = new TextField(selected.getDesignation());

        TextArea descriptionArea = new TextArea(selected.getDescription() != null ? selected.getDescription() : "");
        descriptionArea.setPromptText("Description (optionnelle)");
        descriptionArea.setPrefRowCount(3);
        descriptionArea.setWrapText(true);

        TextField prixAchatField = new TextField(String.valueOf(selected.getPrixAchat()));
        TextField prixVenteField = new TextField(String.valueOf(selected.getPrixVente()));
        TextField quantiteField = new TextField(String.valueOf(selected.getQuantiteStock()));
        TextField seuilMinField = new TextField(String.valueOf(selected.getSeuilMin()));
        TextField seuilMaxField = new TextField(String.valueOf(selected.getSeuilMax()));

        ComboBox<String> categorieField = new ComboBox<>();
        categorieField.setPromptText("Catégorie");
        categorieField.setEditable(true);
        try { categorieField.setItems(FXCollections.observableArrayList(produitDAO.getCategories())); } catch (Exception ignored) {}
        categorieField.setValue(selected.getCategorie());

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20,150,10,10));
        grid.add(new Label("Référence:"),0,0); grid.add(refField,1,0);
        grid.add(new Label("Désignation:"),0,1); grid.add(desField,1,1);
        grid.add(new Label("Description:"),0,2); grid.add(descriptionArea,1,2);
        grid.add(new Label("Prix d'achat:"),0,3); grid.add(prixAchatField,1,3);
        grid.add(new Label("Prix de vente:"),0,4); grid.add(prixVenteField,1,4);
        grid.add(new Label("Quantité en stock:"),0,5); grid.add(quantiteField,1,5);
        grid.add(new Label("Seuil minimum:"),0,6); grid.add(seuilMinField,1,6);
        grid.add(new Label("Seuil maximum:"),0,7); grid.add(seuilMaxField,1,7);
        grid.add(new Label("Catégorie:"),0,8); grid.add(categorieField,1,8);

        dialog.getDialogPane().setContent(grid);
        ButtonType modifierBtn = new ButtonType("Modifier", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(modifierBtn, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == modifierBtn) {
                try {
                    String ref = refField.getText().trim();
                    String designation = desField.getText().trim();
                    if (ref.isEmpty() || designation.isEmpty()) {
                        AlertUtils.showWarning("Attention","Référence et désignation obligatoires"); return null;
                    }
                    if (!ref.equals(selected.getReference()) && referenceExiste(ref)) {
                        AlertUtils.showWarning("Attention","Nouvelle référence déjà utilisée"); return null;
                    }
                    float prixAchat = Float.parseFloat(prixAchatField.getText().trim());
                    float prixVente = Float.parseFloat(prixVenteField.getText().trim());
                    int quantiteStock = Integer.parseInt(quantiteField.getText().trim());
                    int seuilMin = Integer.parseInt(seuilMinField.getText().trim());
                    int seuilMax = Integer.parseInt(seuilMaxField.getText().trim());
                    if (prixAchat < 0 || prixVente < 0) {
                        AlertUtils.showWarning("Attention","Les prix ne peuvent pas être négatifs"); return null;
                    }
                    if (seuilMin > seuilMax) {
                        AlertUtils.showWarning("Attention","Seuil minimum > seuil maximum"); return null;
                    }
                    selected.setReference(ref);
                    selected.setDesignation(designation);
                    selected.setDescription(descriptionArea.getText().trim());
                    selected.setPrixAchat(prixAchat);
                    selected.setPrixVente(prixVente);
                    selected.setQuantiteStock(quantiteStock);
                    selected.setSeuilMin(seuilMin);
                    selected.setSeuilMax(seuilMax);
                    selected.setCategorie(categorieField.getValue());
                    produitDAO.update(selected);
                    tableProduits.refresh();
                    AlertUtils.showInfo("Succès","Produit modifié avec succès");
                } catch (NumberFormatException nfe) {
                    AlertUtils.showError("Erreur","Valeurs numériques invalides"); return null;
                } catch (Exception e) {
                    AlertUtils.showError("Erreur","Erreur lors de la modification: " + e.getMessage()); return null;
                }
            }
            return btn;
        });
        dialog.showAndWait();
    }

    @FXML
    private void handleSupprimer() {
        Produit selected = tableProduits.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Attention", "Veuillez sélectionner un produit à supprimer");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Supprimer le produit");
        confirm.setContentText("Êtes-vous sûr de vouloir supprimer " + selected.getDesignation() + " ?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    produitDAO.delete(selected.getIdProduit());
                    produits.remove(selected);
                    lblTotal.setText("Total: " + produits.size());
                    AlertUtils.showInfo("Succès", "Produit supprimé avec succès");
                } catch (Exception e) {
                    AlertUtils.showError("Erreur", "Erreur lors de la suppression: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleRetour() {
        try {
            NavigationManager.getInstance().navigate("/fxml/appro/DashboardAppro.fxml", "Dashboard Approvisionnement");
        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Impossible de retourner au dashboard: " + e.getMessage());
        }
    }
}
