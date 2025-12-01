package com.stock.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import com.stock.model.utilisateur.Utilisateur;
import com.stock.dao.implementation.UtilisateurDAO;
import com.stock.util.AlertUtils;
import com.stock.util.NavigationManager;

import java.util.List;

/**
 * Contrôleur pour la gestion des utilisateurs (Administrateur)
 */
public class GestionUtilisateursController {

    @FXML private TextField txtRecherche;
    @FXML private ComboBox<String> comboRole;
    @FXML private Button btnRecherche;

    @FXML private TableView<Utilisateur> tableUtilisateurs;
    @FXML private TableColumn<Utilisateur, Integer> colId;
    @FXML private TableColumn<Utilisateur, String> colNom;
    @FXML private TableColumn<Utilisateur, String> colPrenom;
    @FXML private TableColumn<Utilisateur, String> colEmail;
    @FXML private TableColumn<Utilisateur, String> colRole;
    @FXML private TableColumn<Utilisateur, Boolean> colActif;

    @FXML private Label lblTotal;
    @FXML private Button btnAjouter;
    @FXML private Button btnModifier;
    @FXML private Button btnSupprimer;
    @FXML private Button btnRetour;

    private UtilisateurDAO utilisateurDAO;
    private ObservableList<Utilisateur> utilisateurs;

    @FXML
    public void initialize() {
        try {
            utilisateurDAO = new UtilisateurDAO();
            utilisateurs = FXCollections.observableArrayList();

            initTable();
            chargerRoles();
            chargerUtilisateurs();

        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Erreur lors de l'initialisation: " + e.getMessage());
        }
    }

    private void initTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idUtilisateur"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colActif.setCellValueFactory(new PropertyValueFactory<>("actif"));
    }

    private void chargerRoles() {
        comboRole.setItems(FXCollections.observableArrayList(
            "ADMINISTRATEUR", "RESPONSABLE_APPROVISIONNEMENT",
            "RESPONSABLE_VENTES", "MAGASINIER"
        ));
    }

    private void chargerUtilisateurs() {
        try {
            List<Utilisateur> listeUtilisateurs = utilisateurDAO.readAll();
            if (listeUtilisateurs != null && !listeUtilisateurs.isEmpty()) {
                utilisateurs.setAll(listeUtilisateurs);
                tableUtilisateurs.setItems(utilisateurs);
                lblTotal.setText("Total: " + utilisateurs.size());
            } else {
                // Si DAO retourne null, charger depuis la base directement
                chargerUtilisateursDepuisBD();
            }
        } catch (Exception e) {
            System.err.println("Erreur chargement utilisateurs: " + e.getMessage());
            chargerUtilisateursDepuisBD();
        }
    }

    private void chargerUtilisateursDepuisBD() {
        try {
            utilisateurs.clear();
            var conn = com.stock.util.DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT * FROM utilisateurs ORDER BY nom, prenom";
            try (var stmt = conn.prepareStatement(sql);
                 var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    com.stock.model.utilisateur.UtilisateurGenerique user =
                        new com.stock.model.utilisateur.UtilisateurGenerique();
                    user.setIdUtilisateur(rs.getInt("idUtilisateur"));
                    user.setNom(rs.getString("nom"));
                    user.setPrenom(rs.getString("prenom"));
                    user.setEmail(rs.getString("email"));
                    user.setMotDePasse(rs.getString("motDePasse"));
                    user.setRole(rs.getString("role"));
                    user.setActif(rs.getBoolean("actif"));
                    if (rs.getDate("dateCreation") != null) {
                        user.setDateCreation(rs.getDate("dateCreation").toLocalDate());
                    }
                    utilisateurs.add(user);
                }
            }
            tableUtilisateurs.setItems(utilisateurs);
            lblTotal.setText("Total: " + utilisateurs.size());
        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Impossible de charger les utilisateurs: " + e.getMessage());
        }
    }

    @FXML
    private void handleRecherche() {
        String recherche = txtRecherche.getText().toLowerCase();
        String role = comboRole.getValue();

        ObservableList<Utilisateur> filtered = FXCollections.observableArrayList();

        for (Utilisateur u : utilisateurs) {
            boolean matchRecherche = recherche.isEmpty() ||
                u.getNom().toLowerCase().contains(recherche) ||
                u.getPrenom().toLowerCase().contains(recherche) ||
                u.getEmail().toLowerCase().contains(recherche);

            boolean matchRole = role == null || role.isEmpty() ||
                role.equals(u.getRole());

            if (matchRecherche && matchRole) {
                filtered.add(u);
            }
        }

        tableUtilisateurs.setItems(filtered);
        lblTotal.setText("Total: " + filtered.size());
    }

    @FXML
    private void handleAjouter() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un Utilisateur");
        dialog.setHeaderText("Saisir les informations du nouvel utilisateur");

        // Créer le formulaire
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nomField = new TextField();
        nomField.setPromptText("Nom");
        TextField prenomField = new TextField();
        prenomField.setPromptText("Prénom");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("ADMINISTRATEUR", "RESPONSABLE_APPROVISIONNEMENT", "RESPONSABLE_VENTES", "MAGASINIER");
        roleComboBox.setPromptText("Sélectionner un rôle");

        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Prénom:"), 0, 1);
        grid.add(prenomField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Mot de passe:"), 0, 3);
        grid.add(passwordField, 1, 3);
        grid.add(new Label("Rôle:"), 0, 4);
        grid.add(roleComboBox, 1, 4);

        dialog.getDialogPane().setContent(grid);

        // Boutons
        ButtonType ajouterButtonType = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(ajouterButtonType, ButtonType.CANCEL);

        // Validation
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ajouterButtonType) {
                // Validation des champs
                if (nomField.getText().trim().isEmpty() ||
                    prenomField.getText().trim().isEmpty() ||
                    emailField.getText().trim().isEmpty() ||
                    passwordField.getText().trim().isEmpty() ||
                    roleComboBox.getValue() == null) {
                    AlertUtils.showWarning("Attention", "Veuillez remplir tous les champs");
                    return null;
                }

                try {
                    // Créer l'utilisateur
                    com.stock.model.utilisateur.UtilisateurGenerique nouvelUtilisateur =
                        new com.stock.model.utilisateur.UtilisateurGenerique();
                    nouvelUtilisateur.setNom(nomField.getText().trim());
                    nouvelUtilisateur.setPrenom(prenomField.getText().trim());
                    nouvelUtilisateur.setEmail(emailField.getText().trim());
                    nouvelUtilisateur.setMotDePasse(passwordField.getText().trim());
                    nouvelUtilisateur.setRole(roleComboBox.getValue());
                    nouvelUtilisateur.setActif(true);
                    nouvelUtilisateur.setDateCreation(java.time.LocalDate.now()); // Date courante

                    // Sauvegarder en base
                    utilisateurDAO.create(nouvelUtilisateur);

                    // Recharger la liste depuis la base pour avoir l'ID auto-généré
                    chargerUtilisateurs();

                    AlertUtils.showInfo("Succès", "Utilisateur ajouté avec succès");

                } catch (Exception e) {
                    AlertUtils.showError("Erreur", "Erreur lors de l'ajout: " + e.getMessage());
                    return null;
                }
            }
            return dialogButton;
        });

        dialog.showAndWait();
    }

    @FXML
    private void handleModifier() {
        Utilisateur selected = tableUtilisateurs.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Attention", "Veuillez sélectionner un utilisateur à modifier");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Modifier un Utilisateur");
        dialog.setHeaderText("Modifier les informations de l'utilisateur");

        // Créer le formulaire
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nomField = new TextField(selected.getNom());
        TextField prenomField = new TextField(selected.getPrenom());
        TextField emailField = new TextField(selected.getEmail());
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Nouveau mot de passe (laisser vide pour garder l'ancien)");
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("ADMINISTRATEUR", "RESPONSABLE_APPROVISIONNEMENT", "RESPONSABLE_VENTES", "MAGASINIER");
        roleComboBox.setValue(selected.getRole());
        CheckBox actifCheckBox = new CheckBox("Utilisateur actif");
        actifCheckBox.setSelected(selected.isActif());

        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Prénom:"), 0, 1);
        grid.add(prenomField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Mot de passe:"), 0, 3);
        grid.add(passwordField, 1, 3);
        grid.add(new Label("Rôle:"), 0, 4);
        grid.add(roleComboBox, 1, 4);
        grid.add(actifCheckBox, 1, 5);

        dialog.getDialogPane().setContent(grid);

        // Boutons
        ButtonType modifierButtonType = new ButtonType("Modifier", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(modifierButtonType, ButtonType.CANCEL);

        // Validation
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == modifierButtonType) {
                // Validation des champs
                if (nomField.getText().trim().isEmpty() ||
                    prenomField.getText().trim().isEmpty() ||
                    emailField.getText().trim().isEmpty() ||
                    roleComboBox.getValue() == null) {
                    AlertUtils.showWarning("Attention", "Veuillez remplir tous les champs obligatoires");
                    return null;
                }

                try {
                    // Mettre à jour l'utilisateur
                    selected.setNom(nomField.getText().trim());
                    selected.setPrenom(prenomField.getText().trim());
                    selected.setEmail(emailField.getText().trim());
                    selected.setRole(roleComboBox.getValue());
                    selected.setActif(actifCheckBox.isSelected());

                    // Mettre à jour le mot de passe seulement s'il est saisi
                    if (!passwordField.getText().trim().isEmpty()) {
                        selected.setMotDePasse(passwordField.getText().trim());
                    }

                    // Sauvegarder en base
                    utilisateurDAO.update(selected);

                    // Rafraîchir la table
                    tableUtilisateurs.refresh();

                    AlertUtils.showInfo("Succès", "Utilisateur modifié avec succès");

                } catch (Exception e) {
                    AlertUtils.showError("Erreur", "Erreur lors de la modification: " + e.getMessage());
                    return null;
                }
            }
            return dialogButton;
        });

        dialog.showAndWait();
    }

    @FXML
    private void handleSupprimer() {
        Utilisateur selected = tableUtilisateurs.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Attention", "Veuillez sélectionner un utilisateur à supprimer");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Supprimer l'utilisateur");
        confirm.setContentText("Êtes-vous sûr de vouloir supprimer " +
            selected.getNom() + " " + selected.getPrenom() + " ?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    utilisateurDAO.delete(selected.getIdUtilisateur());
                    utilisateurs.remove(selected);
                    lblTotal.setText("Total: " + utilisateurs.size());
                    AlertUtils.showInfo("Succès", "Utilisateur supprimé avec succès");
                } catch (Exception e) {
                    AlertUtils.showError("Erreur", "Erreur lors de la suppression: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleRetour() {
        try {
            NavigationManager.getInstance().navigate("/fxml/admin/DashboardAdmin.fxml", "Dashboard Administrateur");
        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Impossible de retourner au dashboard: " + e.getMessage());
        }
    }
}
