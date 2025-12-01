package com.stock.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import com.stock.model.partenaire.Client;
import com.stock.util.AlertUtils;
import com.stock.util.NavigationManager;
import com.stock.util.DatabaseConnection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Contrôleur pour la gestion des clients (Responsable Ventes)
 */
public class GestionClientsController {

    @FXML private TextField txtRecherche;

    @FXML private TableView<Client> tableClients;
    @FXML private TableColumn<Client, Integer> colId;
    @FXML private TableColumn<Client, String> colNom;
    @FXML private TableColumn<Client, String> colPrenom;
    @FXML private TableColumn<Client, String> colRaisonSociale;
    @FXML private TableColumn<Client, String> colTelephone;
    @FXML private TableColumn<Client, String> colEmail;
    @FXML private TableColumn<Client, String> colAdresse;
    @FXML private TableColumn<Client, LocalDate> colDateInscription;

    @FXML private Label lblTotal;
    @FXML private Button btnAjouter;
    @FXML private Button btnModifier;
    @FXML private Button btnSupprimer;
    @FXML private Button btnRetour;

    private ObservableList<Client> clients;

    @FXML
    public void initialize() {
        try {
            clients = FXCollections.observableArrayList();
            initTable();
            chargerClients();
        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Erreur lors de l'initialisation: " + e.getMessage());
        }
    }

    private void initTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idClient"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colRaisonSociale.setCellValueFactory(new PropertyValueFactory<>("raisonSociale"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        colDateInscription.setCellValueFactory(new PropertyValueFactory<>("dateInscription"));

        // Formater la date
        colDateInscription.setCellFactory(col -> new TableCell<Client, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }
            }
        });
    }

    private void chargerClients() {
        try {
            clients.clear();
            var conn = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT * FROM clients ORDER BY nom, prenom";

            try (var stmt = conn.prepareStatement(sql);
                 var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Client client = new Client(
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("raisonSociale"),
                        rs.getString("adresse"),
                        rs.getString("telephone"),
                        rs.getString("email")
                    );
                    client.setIdClient(rs.getInt("idClient"));
                    if (rs.getDate("dateInscription") != null) {
                        client.setDateInscription(rs.getDate("dateInscription").toLocalDate());
                    }
                    clients.add(client);
                }
            }

            tableClients.setItems(clients);
            lblTotal.setText("Total: " + clients.size());
        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Impossible de charger les clients: " + e.getMessage());
        }
    }

    @FXML
    private void handleRecherche() {
        String recherche = txtRecherche.getText().toLowerCase();
        ObservableList<Client> filtered = FXCollections.observableArrayList();

        for (Client c : clients) {
            boolean match = recherche.isEmpty() ||
                (c.getNom() != null && c.getNom().toLowerCase().contains(recherche)) ||
                (c.getPrenom() != null && c.getPrenom().toLowerCase().contains(recherche)) ||
                (c.getEmail() != null && c.getEmail().toLowerCase().contains(recherche)) ||
                (c.getTelephone() != null && c.getTelephone().toLowerCase().contains(recherche));

            if (match) {
                filtered.add(c);
            }
        }

        tableClients.setItems(filtered);
        lblTotal.setText("Total: " + filtered.size());
    }

    @FXML
    private void handleAjouter() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un Client");
        dialog.setHeaderText("Saisir les informations du client");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nomField = new TextField();
        nomField.setPromptText("Nom");
        TextField prenomField = new TextField();
        prenomField.setPromptText("Prénom");
        TextField raisonSocialeField = new TextField();
        raisonSocialeField.setPromptText("Raison sociale (optionnel)");
        TextField telephoneField = new TextField();
        telephoneField.setPromptText("Téléphone");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        TextArea adresseArea = new TextArea();
        adresseArea.setPromptText("Adresse complète");
        adresseArea.setPrefRowCount(3);

        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Prénom:"), 0, 1);
        grid.add(prenomField, 1, 1);
        grid.add(new Label("Raison sociale:"), 0, 2);
        grid.add(raisonSocialeField, 1, 2);
        grid.add(new Label("Téléphone:"), 0, 3);
        grid.add(telephoneField, 1, 3);
        grid.add(new Label("Email:"), 0, 4);
        grid.add(emailField, 1, 4);
        grid.add(new Label("Adresse:"), 0, 5);
        grid.add(adresseArea, 1, 5);

        dialog.getDialogPane().setContent(grid);
        ButtonType ajouterBtn = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(ajouterBtn, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ajouterBtn) {
                try {
                    String nom = nomField.getText().trim();
                    String prenom = prenomField.getText().trim();
                    String email = emailField.getText().trim();

                    if (nom.isEmpty() || email.isEmpty()) {
                        AlertUtils.showWarning("Attention", "Nom et email sont obligatoires");
                        return null;
                    }

                    var conn = DatabaseConnection.getInstance().getConnection();
                    String sql = "INSERT INTO clients (nom, prenom, raisonSociale, adresse, telephone, email, dateInscription) VALUES (?, ?, ?, ?, ?, ?, ?)";

                    try (var stmt = conn.prepareStatement(sql)) {
                        stmt.setString(1, nom);
                        stmt.setString(2, prenom);
                        stmt.setString(3, raisonSocialeField.getText().trim());
                        stmt.setString(4, adresseArea.getText().trim());
                        stmt.setString(5, telephoneField.getText().trim());
                        stmt.setString(6, email);
                        stmt.setObject(7, LocalDate.now());
                        stmt.executeUpdate();
                    }

                    chargerClients();
                    AlertUtils.showInfo("Succès", "Client ajouté avec succès");
                } catch (Exception e) {
                    AlertUtils.showError("Erreur", "Erreur lors de l'ajout: " + e.getMessage());
                    return null;
                }
            }
            return btn;
        });

        dialog.showAndWait();
    }

    @FXML
    private void handleModifier() {
        Client selected = tableClients.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Attention", "Veuillez sélectionner un client à modifier");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Modifier un Client");
        dialog.setHeaderText("Mettre à jour les informations du client");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nomField = new TextField(selected.getNom());
        TextField prenomField = new TextField(selected.getPrenom());
        TextField raisonSocialeField = new TextField(selected.getRaisonSociale());
        TextField telephoneField = new TextField(selected.getTelephone());
        TextField emailField = new TextField(selected.getEmail());
        TextArea adresseArea = new TextArea(selected.getAdresse());
        adresseArea.setPrefRowCount(3);

        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Prénom:"), 0, 1);
        grid.add(prenomField, 1, 1);
        grid.add(new Label("Raison sociale:"), 0, 2);
        grid.add(raisonSocialeField, 1, 2);
        grid.add(new Label("Téléphone:"), 0, 3);
        grid.add(telephoneField, 1, 3);
        grid.add(new Label("Email:"), 0, 4);
        grid.add(emailField, 1, 4);
        grid.add(new Label("Adresse:"), 0, 5);
        grid.add(adresseArea, 1, 5);

        dialog.getDialogPane().setContent(grid);
        ButtonType modifierBtn = new ButtonType("Modifier", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(modifierBtn, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == modifierBtn) {
                try {
                    String nom = nomField.getText().trim();
                    String email = emailField.getText().trim();

                    if (nom.isEmpty() || email.isEmpty()) {
                        AlertUtils.showWarning("Attention", "Nom et email sont obligatoires");
                        return null;
                    }

                    var conn = DatabaseConnection.getInstance().getConnection();
                    String sql = "UPDATE clients SET nom=?, prenom=?, raisonSociale=?, adresse=?, telephone=?, email=? WHERE idClient=?";

                    try (var stmt = conn.prepareStatement(sql)) {
                        stmt.setString(1, nom);
                        stmt.setString(2, prenomField.getText().trim());
                        stmt.setString(3, raisonSocialeField.getText().trim());
                        stmt.setString(4, adresseArea.getText().trim());
                        stmt.setString(5, telephoneField.getText().trim());
                        stmt.setString(6, email);
                        stmt.setInt(7, selected.getIdClient());
                        stmt.executeUpdate();
                    }

                    chargerClients();
                    AlertUtils.showInfo("Succès", "Client modifié avec succès");
                } catch (Exception e) {
                    AlertUtils.showError("Erreur", "Erreur lors de la modification: " + e.getMessage());
                    return null;
                }
            }
            return btn;
        });

        dialog.showAndWait();
    }

    @FXML
    private void handleSupprimer() {
        Client selected = tableClients.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Attention", "Veuillez sélectionner un client à supprimer");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Supprimer le client");
        confirm.setContentText("Êtes-vous sûr de vouloir supprimer " + selected.getNom() + " " + selected.getPrenom() + " ?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    var conn = DatabaseConnection.getInstance().getConnection();
                    String sql = "DELETE FROM clients WHERE idClient=?";

                    try (var stmt = conn.prepareStatement(sql)) {
                        stmt.setInt(1, selected.getIdClient());
                        stmt.executeUpdate();
                    }

                    clients.remove(selected);
                    lblTotal.setText("Total: " + clients.size());
                    AlertUtils.showInfo("Succès", "Client supprimé avec succès");
                } catch (Exception e) {
                    AlertUtils.showError("Erreur", "Erreur lors de la suppression: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleRetour() {
        try {
            NavigationManager.getInstance().navigate("/fxml/ventes/DashboardVentes.fxml", "Dashboard Ventes");
        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Impossible de retourner au dashboard: " + e.getMessage());
        }
    }
}

