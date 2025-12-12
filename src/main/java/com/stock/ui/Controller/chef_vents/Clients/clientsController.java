package com.stock.ui.Controller.chef_vents.Clients;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;
import com.stock.service.ClientService;
import com.stock.model.partenaire.Client;

public class clientsController implements Initializable {

    @FXML private Button monbutton;
    @FXML private TextField searchField;
    @FXML private Button addClientButton;
    @FXML private TableView<Client> clientsTable;
    @FXML private TableColumn<Client, Integer> idColumn;
    @FXML private TableColumn<Client, String> nomColumn;
    @FXML private TableColumn<Client, String> prenomColumn;
    @FXML private TableColumn<Client, String> emailColumn;
    @FXML private TableColumn<Client, String> telephoneColumn;
    @FXML private TableColumn<Client, String> adresseColumn;
    @FXML private TableColumn<Client, Void> actionsColumn;
    @FXML private ObservableList<Client> clientsList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configuration des colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idClient"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));

        // Recherche en temps réel
        searchField.textProperty().addListener((obs, oldText, newText) -> {
            if (newText == null || newText.isEmpty()) {
                clientsTable.setItems(clientsList);
            } else {
                ObservableList<Client> filtered = clientsList.filtered(client ->
                        client.getNom().toLowerCase().contains(newText.toLowerCase()) ||
                                client.getPrenom().toLowerCase().contains(newText.toLowerCase()) ||
                                client.getEmail().toLowerCase().contains(newText.toLowerCase())
                );
                clientsTable.setItems(filtered);
            }
        });

        setupActionsColumn();
        loadClients();
    }

    private void setupActionsColumn() {
        actionsColumn.setCellFactory(param -> new TableCell<Client, Void>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");

            {
                // Style orange clair pour modifier
                editButton.setStyle("-fx-background-color: linear-gradient(to bottom, #fb923c, #f97316); -fx-text-fill: white; -fx-font-size: 11px; -fx-padding: 6 12; -fx-border-radius: 6; -fx-background-radius: 6;");
                
                // Style orange foncé pour supprimer
                deleteButton.setStyle("-fx-background-color: linear-gradient(to bottom, #dc2626, #b91c1c); -fx-text-fill: white; -fx-font-size: 11px; -fx-padding: 6 12; -fx-border-radius: 6; -fx-background-radius: 6;");
                
                editButton.setOnAction(event -> {
                    Client client = getTableView().getItems().get(getIndex());
                    System.out.println("Modifier: " + client.getNom());
                });
                
                deleteButton.setOnAction(event -> {
                    Client client = getTableView().getItems().get(getIndex());
                    clientsList.remove(client);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    javafx.scene.layout.HBox buttons = new javafx.scene.layout.HBox(5);
                    buttons.getChildren().addAll(editButton, deleteButton);
                    setGraphic(buttons);
                }
            }
        });
    }

    @FXML
    private void monbutton() {
        System.out.println("votre application  fait la gestion de stock ");
    }

    @FXML
    private void searchClients() {
        String searchTerm = searchField.getText();
        ClientService clientService = new ClientService();
        ObservableList<Client> searchResults = FXCollections.observableArrayList(clientService.searchClients(searchTerm));
        clientsTable.setItems(searchResults);
    }

    @FXML
    private void addClient() {
        System.out.println("le button d'ajouter  un client est cliqué  maintenant pour ajouter un client");
        ClientService clientService = new ClientService();
        Client nouveauClient = new Client("smahan", "boulmane", "Azilal/hey el wahda", "Azilal/hey el wahda", "123456789", "email@example.com");
        boolean ajoutReussi = clientService.addClient(nouveauClient);
    }

    private void loadClients() {
        try {
            ClientService clientService = new ClientService();
            clientsList.addAll(clientService.getAllClients());
            clientsTable.setItems(clientsList);
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement des clients: " + e.getMessage());
        }
    }
}