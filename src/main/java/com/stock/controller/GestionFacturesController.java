package com.stock.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import com.stock.model.document.Facture;
import com.stock.model.partenaire.Client;
import com.stock.util.AlertUtils;
import com.stock.util.NavigationManager;
import com.stock.util.DatabaseConnection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

/**
 * Contrôleur pour la gestion des factures (Responsable Ventes)
 */
public class GestionFacturesController {

    @FXML private TextField txtRecherche;
    @FXML private ComboBox<String> comboStatut;

    @FXML private TableView<Facture> tableFactures;
    @FXML private TableColumn<Facture, Integer> colId;
    @FXML private TableColumn<Facture, String> colNumero;
    @FXML private TableColumn<Facture, String> colClient;
    @FXML private TableColumn<Facture, LocalDate> colDateFacture;
    @FXML private TableColumn<Facture, LocalDate> colDateEcheance;
    @FXML private TableColumn<Facture, Float> colMontantHT;
    @FXML private TableColumn<Facture, Float> colMontantTVA;
    @FXML private TableColumn<Facture, Float> colMontantTTC;
    @FXML private TableColumn<Facture, String> colStatut;

    @FXML private Label lblTotal;
    @FXML private Button btnGenerer;
    @FXML private Button btnMarquerPayee;
    @FXML private Button btnAnnuler;
    @FXML private Button btnRetour;

    private ObservableList<Facture> factures;

    @FXML
    public void initialize() {
        try {
            factures = FXCollections.observableArrayList();
            initTable();
            initComboStatut();
            chargerFactures();
        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Erreur lors de l'initialisation: " + e.getMessage());
        }
    }

    private void initTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idFacture"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colClient.setCellValueFactory(new PropertyValueFactory<>("clientNom"));
        colDateFacture.setCellValueFactory(new PropertyValueFactory<>("dateFacture"));
        colDateEcheance.setCellValueFactory(new PropertyValueFactory<>("dateEcheance"));
        colMontantHT.setCellValueFactory(new PropertyValueFactory<>("montantHT"));
        colMontantTVA.setCellValueFactory(new PropertyValueFactory<>("montantTVA"));
        colMontantTTC.setCellValueFactory(new PropertyValueFactory<>("montantTTC"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));

        // Formater les dates
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        colDateFacture.setCellFactory(col -> new TableCell<Facture, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.format(formatter));
            }
        });

        colDateEcheance.setCellFactory(col -> new TableCell<Facture, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.format(formatter));
            }
        });

        // Formater les montants
        colMontantHT.setCellFactory(col -> new TableCell<Facture, Float>() {
            @Override
            protected void updateItem(Float item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("%.2f €", item));
            }
        });

        colMontantTVA.setCellFactory(col -> new TableCell<Facture, Float>() {
            @Override
            protected void updateItem(Float item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("%.2f €", item));
            }
        });

        colMontantTTC.setCellFactory(col -> new TableCell<Facture, Float>() {
            @Override
            protected void updateItem(Float item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.format("%.2f €", item));
            }
        });

        // Colorer le statut
        colStatut.setCellFactory(col -> new TableCell<Facture, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    switch (item) {
                        case "GENEREE":
                            setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
                            break;
                        case "PAYEE":
                            setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                            break;
                        case "ANNULEE":
                            setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });
    }

    private void initComboStatut() {
        comboStatut.setItems(FXCollections.observableArrayList("", "BROUILLON", "GENEREE", "PAYEE", "ANNULEE"));
    }

    private void chargerFactures() {
        try {
            factures.clear();
            var conn = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT f.idFacture, f.numero, f.dateFacture, f.dateEcheance, " +
                        "f.montantHT, f.montantTVA, f.montantTTC, f.statut, " +
                        "c.idClient, c.nom, c.prenom, c.raisonSociale, c.adresse, c.telephone, c.email " +
                        "FROM factures f " +
                        "JOIN clients c ON f.idClient = c.idClient " +
                        "ORDER BY f.dateFacture DESC";

            try (var stmt = conn.prepareStatement(sql);
                 var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Créer l'objet Client
                    Client client = new Client(
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("raisonSociale"),
                        rs.getString("adresse"),
                        rs.getString("telephone"),
                        rs.getString("email")
                    );
                    client.setIdClient(rs.getInt("idClient"));

                    // Créer la Facture
                    Facture facture = new Facture();
                    facture.setIdFacture(rs.getInt("idFacture"));
                    facture.setNumero(rs.getString("numero"));
                    if (rs.getDate("dateFacture") != null) {
                        facture.setDateFacture(rs.getDate("dateFacture").toLocalDate());
                    }
                    if (rs.getDate("dateEcheance") != null) {
                        facture.setDateEcheance(rs.getDate("dateEcheance").toLocalDate());
                    }
                    facture.setMontantHT(rs.getFloat("montantHT"));
                    facture.setMontantTVA(rs.getFloat("montantTVA"));
                    facture.setMontantTTC(rs.getFloat("montantTTC"));
                    facture.setStatut(rs.getString("statut"));
                    facture.setClient(client);

                    factures.add(facture);
                }
            }

            tableFactures.setItems(factures);
            lblTotal.setText("Total: " + factures.size());
        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Impossible de charger les factures: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRecherche() {
        String recherche = txtRecherche.getText().toLowerCase();
        String statutFiltre = comboStatut.getValue();
        ObservableList<Facture> filtered = FXCollections.observableArrayList();

        for (Facture f : factures) {
            boolean matchRecherche = recherche.isEmpty() ||
                (f.getNumero() != null && f.getNumero().toLowerCase().contains(recherche)) ||
                (f.getClientNom() != null && f.getClientNom().toLowerCase().contains(recherche));

            boolean matchStatut = statutFiltre == null || statutFiltre.isEmpty() ||
                statutFiltre.equals(f.getStatut());

            if (matchRecherche && matchStatut) {
                filtered.add(f);
            }
        }

        tableFactures.setItems(filtered);
        lblTotal.setText("Total: " + filtered.size());
    }

    @FXML
    private void handleGenerer() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Générer une Facture");
        dialog.setHeaderText("Saisir les informations de la facture");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField numeroField = new TextField();
        numeroField.setPromptText("Numéro (ex: FACT-2024-001)");

        // Charger la liste des clients
        ComboBox<Client> clientCombo = new ComboBox<>();
        clientCombo.setPromptText("Sélectionner un client");
        try {
            List<Client> clients = chargerClients();
            clientCombo.setItems(FXCollections.observableArrayList(clients));
            clientCombo.setCellFactory(param -> new ListCell<Client>() {
                @Override
                protected void updateItem(Client item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getNom() + " " + item.getPrenom());
                }
            });
            clientCombo.setButtonCell(new ListCell<Client>() {
                @Override
                protected void updateItem(Client item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getNom() + " " + item.getPrenom());
                }
            });
        } catch (Exception e) {
            AlertUtils.showError("Erreur", "Impossible de charger les clients");
        }

        DatePicker dateEcheancePicker = new DatePicker();
        dateEcheancePicker.setValue(LocalDate.now().plusDays(30));

        TextField montantHTField = new TextField();
        montantHTField.setPromptText("Montant HT");

        grid.add(new Label("Numéro:"), 0, 0);
        grid.add(numeroField, 1, 0);
        grid.add(new Label("Client:"), 0, 1);
        grid.add(clientCombo, 1, 1);
        grid.add(new Label("Date Échéance:"), 0, 2);
        grid.add(dateEcheancePicker, 1, 2);
        grid.add(new Label("Montant HT:"), 0, 3);
        grid.add(montantHTField, 1, 3);

        dialog.getDialogPane().setContent(grid);
        ButtonType genererBtn = new ButtonType("Générer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(genererBtn, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == genererBtn) {
                try {
                    String numero = numeroField.getText().trim();
                    Client client = clientCombo.getValue();
                    LocalDate dateEcheance = dateEcheancePicker.getValue();
                    float montantHT = Float.parseFloat(montantHTField.getText().trim());

                    if (numero.isEmpty() || client == null) {
                        AlertUtils.showWarning("Attention", "Numéro et client sont obligatoires");
                        return null;
                    }

                    // Calculer TVA et TTC
                    float montantTVA = montantHT * 0.20f;
                    float montantTTC = montantHT + montantTVA;

                    var conn = DatabaseConnection.getInstance().getConnection();
                    String sql = "INSERT INTO factures (numero, dateFacture, dateEcheance, montantHT, montantTVA, montantTTC, statut, idClient) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                    try (var stmt = conn.prepareStatement(sql)) {
                        stmt.setString(1, numero);
                        stmt.setObject(2, LocalDate.now());
                        stmt.setObject(3, dateEcheance);
                        stmt.setFloat(4, montantHT);
                        stmt.setFloat(5, montantTVA);
                        stmt.setFloat(6, montantTTC);
                        stmt.setString(7, "GENEREE");
                        stmt.setInt(8, client.getIdClient());
                        stmt.executeUpdate();
                    }

                    chargerFactures();
                    AlertUtils.showInfo("Succès", "Facture générée avec succès");
                } catch (NumberFormatException e) {
                    AlertUtils.showError("Erreur", "Montant invalide");
                    return null;
                } catch (Exception e) {
                    AlertUtils.showError("Erreur", "Erreur lors de la génération: " + e.getMessage());
                    return null;
                }
            }
            return btn;
        });

        dialog.showAndWait();
    }

    private List<Client> chargerClients() throws Exception {
        List<Client> clients = new ArrayList<>();
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
                clients.add(client);
            }
        }
        return clients;
    }

    @FXML
    private void handleMarquerPayee() {
        Facture selected = tableFactures.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Attention", "Veuillez sélectionner une facture");
            return;
        }

        if (!"GENEREE".equals(selected.getStatut())) {
            AlertUtils.showWarning("Attention", "Seules les factures générées peuvent être marquées comme payées");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Marquer comme payée");
        confirm.setContentText("Confirmer le paiement de la facture " + selected.getNumero() + " ?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    var conn = DatabaseConnection.getInstance().getConnection();
                    String sql = "UPDATE factures SET statut='PAYEE' WHERE idFacture=?";

                    try (var stmt = conn.prepareStatement(sql)) {
                        stmt.setInt(1, selected.getIdFacture());
                        stmt.executeUpdate();
                    }

                    chargerFactures();
                    AlertUtils.showInfo("Succès", "Facture marquée comme payée");
                } catch (Exception e) {
                    AlertUtils.showError("Erreur", "Erreur lors de la mise à jour: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleAnnuler() {
        Facture selected = tableFactures.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Attention", "Veuillez sélectionner une facture");
            return;
        }

        if ("PAYEE".equals(selected.getStatut())) {
            AlertUtils.showWarning("Attention", "Impossible d'annuler une facture déjà payée");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Annuler la facture");
        confirm.setContentText("Confirmer l'annulation de la facture " + selected.getNumero() + " ?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    var conn = DatabaseConnection.getInstance().getConnection();
                    String sql = "UPDATE factures SET statut='ANNULEE' WHERE idFacture=?";

                    try (var stmt = conn.prepareStatement(sql)) {
                        stmt.setInt(1, selected.getIdFacture());
                        stmt.executeUpdate();
                    }

                    chargerFactures();
                    AlertUtils.showInfo("Succès", "Facture annulée");
                } catch (Exception e) {
                    AlertUtils.showError("Erreur", "Erreur lors de l'annulation: " + e.getMessage());
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

