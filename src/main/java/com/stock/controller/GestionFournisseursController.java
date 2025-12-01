package com.stock.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.stock.model.partenaire.Fournisseur;
import com.stock.dao.implementation.FournisseurDAO;
import com.stock.util.AlertUtils;
import com.stock.util.NavigationManager;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;

import java.util.List;

/**
 * Contrôleur pour la gestion des fournisseurs (Approvisionnement)
 */
public class GestionFournisseursController {

    @FXML private TextField txtRecherche;
    @FXML private Button btnRecherche;

    @FXML private TableView<Fournisseur> tableFournisseurs;
    @FXML private TableColumn<Fournisseur, Integer> colId;
    @FXML private TableColumn<Fournisseur, String> colNom;
    @FXML private TableColumn<Fournisseur, String> colContact;
    @FXML private TableColumn<Fournisseur, String> colTelephone;
    @FXML private TableColumn<Fournisseur, String> colEmail;
    @FXML private TableColumn<Fournisseur, String> colVille;
    @FXML private TableColumn<Fournisseur, Boolean> colStatut;
    @FXML private Label lblTotal;
    @FXML private Button btnAjouter;
    @FXML private Button btnModifier;
    @FXML private Button btnSupprimer;
    @FXML private Button btnRetour;
    @FXML private ComboBox<String> comboStatut;

    private FournisseurDAO fournisseurDAO;
    private ObservableList<Fournisseur> fournisseurs;

    @FXML
    public void initialize() {
        try {
            fournisseurDAO = new FournisseurDAO();
            fournisseurs = FXCollections.observableArrayList();

            initTable();
            comboStatut.setItems(FXCollections.observableArrayList("", "ACTIF", "INACTIF"));
            chargerFournisseurs();

        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Erreur lors de l'initialisation: " + e.getMessage());
        }
    }

    private void initTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idFournisseur"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("raisonSociale"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colVille.setCellValueFactory(new PropertyValueFactory<>("ville"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("actif"));
    }

    private void chargerFournisseurs() {
        try {
            List<Fournisseur> listeFournisseurs = fournisseurDAO.readAll();
            fournisseurs.setAll(listeFournisseurs);
            tableFournisseurs.setItems(fournisseurs);
            lblTotal.setText("Total: " + fournisseurs.size());
        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Chargement fournisseurs: " + e.getMessage());
        }
    }

    private void chargerFournisseursDepuisBD() {
        try {
            fournisseurs.clear();
            var conn = com.stock.util.DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT * FROM fournisseurs ORDER BY raisonSociale";
            try (var stmt = conn.prepareStatement(sql);
                 var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Fournisseur f = new Fournisseur();
                    f.setIdFournisseur(rs.getInt("idFournisseur"));
                    f.setRaisonSociale(rs.getString("raisonSociale"));
                    f.setContact(rs.getString("contact"));
                    f.setTelephone(rs.getString("telephone"));
                    f.setEmail(rs.getString("email"));
                    // Adresse pour ville
                    String adresse = rs.getString("adresse");
                    if (adresse != null && adresse.contains(",")) {
                        f.setVille(adresse.split(",")[1].trim());
                    }
                    fournisseurs.add(f);
                }
            }
            tableFournisseurs.setItems(fournisseurs);
            lblTotal.setText("Total: " + fournisseurs.size());
        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Impossible de charger les fournisseurs: " + e.getMessage());
        }
    }

    private void rafraichirFournisseurs() {
        chargerFournisseurs();
    }

    @FXML
    private void handleRecherche() {
        String recherche = txtRecherche.getText().toLowerCase();
        String statutFiltre = comboStatut.getValue();

        ObservableList<Fournisseur> filtered = FXCollections.observableArrayList();

        for (Fournisseur f : fournisseurs) {
            boolean matchRecherche = recherche.isEmpty() ||
                (f.getRaisonSociale() != null && f.getRaisonSociale().toLowerCase().contains(recherche)) ||
                (f.getContact() != null && f.getContact().toLowerCase().contains(recherche)) ||
                (f.getEmail() != null && f.getEmail().toLowerCase().contains(recherche));

            boolean matchStatut = statutFiltre == null || statutFiltre.isEmpty() ||
                (statutFiltre.equals("ACTIF") && f.isActif()) ||
                (statutFiltre.equals("INACTIF") && !f.isActif());

            if (matchRecherche && matchStatut) filtered.add(f);
        }

        tableFournisseurs.setItems(filtered);
        lblTotal.setText("Total: " + filtered.size());
    }

    @FXML
    private void handleAjouter() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ajouter Fournisseur");
        dialog.setHeaderText("Saisir les informations du fournisseur");

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20, 150, 10, 10));

        TextField raisonField = new TextField();
        raisonField.setPromptText("Raison sociale");
        TextField adresseField = new TextField();
        adresseField.setPromptText("Adresse complète (ville après virgule)");
        TextField telephoneField = new TextField();
        telephoneField.setPromptText("Téléphone");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        TextField contactField = new TextField();
        contactField.setPromptText("Personne de contact");
        TextArea conditionsArea = new TextArea();
        conditionsArea.setPromptText("Conditions (optionnel)");
        conditionsArea.setPrefRowCount(3);
        CheckBox actifCheck = new CheckBox("Actif"); actifCheck.setSelected(true);

        grid.add(new Label("Raison sociale:"), 0, 0); grid.add(raisonField, 1, 0);
        grid.add(new Label("Adresse:"), 0, 1); grid.add(adresseField, 1, 1);
        grid.add(new Label("Téléphone:"), 0, 2); grid.add(telephoneField, 1, 2);
        grid.add(new Label("Email:"), 0, 3); grid.add(emailField, 1, 3);
        grid.add(new Label("Contact:"), 0, 4); grid.add(contactField, 1, 4);
        grid.add(new Label("Conditions:"), 0, 5); grid.add(conditionsArea, 1, 5);
        grid.add(new Label("Actif:"), 0, 6); grid.add(actifCheck, 1, 6);

        dialog.getDialogPane().setContent(grid);
        ButtonType ajouterBtn = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(ajouterBtn, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                if (raisonField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty()) {
                    AlertUtils.showWarning("Attention", "Raison sociale et email sont obligatoires");
                    return null;
                }
                try {
                    Fournisseur f = new Fournisseur(raisonField.getText().trim(), adresseField.getText().trim(), telephoneField.getText().trim(), emailField.getText().trim());
                    f.setContact(contactField.getText().trim());
                    f.setConditions(conditionsArea.getText().trim());
                    f.setActif(actifCheck.isSelected());
                    // Extraire ville si présente
                    String adresse = f.getAdresse();
                    if (adresse != null && adresse.contains(",")) {
                        f.setVille(adresse.substring(adresse.lastIndexOf(',') + 1).trim());
                    }
                    fournisseurDAO.create(f);
                    rafraichirFournisseurs();
                    AlertUtils.showInfo("Succès", "Fournisseur ajouté");
                } catch (Exception e) {
                    AlertUtils.showError("Erreur", "Ajout impossible: " + e.getMessage());
                    return null;
                }
            }
            return btn;
        });

        dialog.showAndWait();
    }

    @FXML
    private void handleModifier() {
        Fournisseur selected = tableFournisseurs.getSelectionModel().getSelectedItem();
        if (selected == null) { AlertUtils.showWarning("Attention", "Sélectionnez un fournisseur"); return; }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Modifier Fournisseur");
        dialog.setHeaderText("Mettre à jour les informations");

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20, 150, 10, 10));

        TextField raisonField = new TextField(selected.getRaisonSociale());
        TextField adresseField = new TextField(selected.getAdresse());
        TextField telephoneField = new TextField(selected.getTelephone());
        TextField emailField = new TextField(selected.getEmail());
        TextField contactField = new TextField(selected.getContact());
        TextArea conditionsArea = new TextArea(selected.getConditions());
        CheckBox actifCheck = new CheckBox("Actif"); actifCheck.setSelected(selected.isActif());

        grid.add(new Label("Raison sociale:"), 0, 0); grid.add(raisonField, 1, 0);
        grid.add(new Label("Adresse:"), 0, 1); grid.add(adresseField, 1, 1);
        grid.add(new Label("Téléphone:"), 0, 2); grid.add(telephoneField, 1, 2);
        grid.add(new Label("Email:"), 0, 3); grid.add(emailField, 1, 3);
        grid.add(new Label("Contact:"), 0, 4); grid.add(contactField, 1, 4);
        grid.add(new Label("Conditions:"), 0, 5); grid.add(conditionsArea, 1, 5);
        grid.add(new Label("Actif:"), 0, 6); grid.add(actifCheck, 1, 6);

        dialog.getDialogPane().setContent(grid);
        ButtonType modifierBtn = new ButtonType("Modifier", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(modifierBtn, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                if (raisonField.getText().trim().isEmpty() || emailField.getText().trim().isEmpty()) {
                    AlertUtils.showWarning("Attention", "Raison sociale et email sont obligatoires");
                    return null;
                }
                try {
                    selected.setRaisonSociale(raisonField.getText().trim());
                    selected.setAdresse(adresseField.getText().trim());
                    selected.setTelephone(telephoneField.getText().trim());
                    selected.setEmail(emailField.getText().trim());
                    selected.setContact(contactField.getText().trim());
                    selected.setConditions(conditionsArea.getText().trim());
                    selected.setActif(actifCheck.isSelected());
                    // Ville recalculée
                    String adresse = selected.getAdresse();
                    if (adresse != null && adresse.contains(",")) {
                        selected.setVille(adresse.substring(adresse.lastIndexOf(',') + 1).trim());
                    } else { selected.setVille(null); }
                    fournisseurDAO.update(selected);
                    tableFournisseurs.refresh();
                    AlertUtils.showInfo("Succès", "Fournisseur modifié");
                } catch (Exception e) {
                    AlertUtils.showError("Erreur", "Modification impossible: " + e.getMessage());
                    return null;
                }
            }
            return btn;
        });

        dialog.showAndWait();
    }

    @FXML
    private void handleSupprimer() {
        Fournisseur selected = tableFournisseurs.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Attention", "Sélectionnez un fournisseur");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Supprimer fournisseur");
        confirm.setContentText("Confirmer la suppression de: " + selected.getRaisonSociale());

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    fournisseurDAO.delete(selected.getIdFournisseur());
                    fournisseurs.remove(selected);
                    lblTotal.setText("Total: " + fournisseurs.size());
                    AlertUtils.showInfo("Succès", "Fournisseur supprimé");
                } catch (Exception e) {
                    AlertUtils.showError("Erreur", "Suppression impossible: " + e.getMessage());
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
