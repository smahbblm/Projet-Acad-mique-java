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
    @FXML private Label userInitials;
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

        setupActionsColumn();
        loadClients();
        loadUserInitials();
        
        // Recherche en temps réel
        if (searchField != null) {
            searchField.textProperty().addListener((obs, oldText, newText) -> {
                filterClients(newText);
            });
        }
    }
    
    private void loadUserInitials() {
        try {
            com.stock.util.SessionManager sessionManager = com.stock.util.SessionManager.getInstance();
            Object userObj = sessionManager.getCurrentUser();
            
            if (userObj != null && userInitials != null) {
                if (userObj instanceof com.stock.model.utilisateur.Utilisateur) {
                    com.stock.model.utilisateur.Utilisateur user = (com.stock.model.utilisateur.Utilisateur) userObj;
                    String initials = "";
                    if (user.getNom() != null && !user.getNom().isEmpty()) {
                        initials += user.getNom().charAt(0);
                    }
                    if (user.getPrenom() != null && !user.getPrenom().isEmpty()) {
                        initials += user.getPrenom().charAt(0);
                    }
                    userInitials.setText(initials.toUpperCase());
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des initiales: " + e.getMessage());
        }
    }
    
    private void filterClients(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            clientsTable.setItems(clientsList);
        } else {
            ObservableList<Client> filtered = FXCollections.observableArrayList();
            String search = searchText.toLowerCase();
            for (Client client : clientsList) {
                String nom = client.getNom() != null ? client.getNom().toLowerCase() : "";
                String prenom = client.getPrenom() != null ? client.getPrenom().toLowerCase() : "";
                String email = client.getEmail() != null ? client.getEmail().toLowerCase() : "";
                
                if (nom.contains(search) || prenom.contains(search) || email.contains(search)) {
                    filtered.add(client);
                }
            }
            clientsTable.setItems(filtered);
        }
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
        filterClients(searchTerm);
    }

    @FXML
    private void addClient() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/gestionChef-vents/clients/formr_ajout_client.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Ajouter un client");
            stage.setScene(new javafx.scene.Scene(root));
            
            // Recharger les clients après fermeture de la fenêtre
            stage.setOnHidden(e -> {
                clientsList.clear();
                loadClients();
            });
            
            stage.show();
        } catch (Exception e) {
            System.err.println("Erreur lors de l'ouverture du formulaire d'ajout: " + e.getMessage());
            e.printStackTrace();
        }
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