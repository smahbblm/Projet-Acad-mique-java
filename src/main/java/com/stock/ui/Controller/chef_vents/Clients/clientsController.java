package com.stock.ui.Controller.chef_vents.Clients;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;
import com.stock.Service.ClientService;

public class clientsController implements Initializable {

    @FXML private Button monbutton;
    @FXML private TextField searchField;
    @FXML private Button addClientButton;
    @FXML private TableView<Client> clientsTable;
    @FXML private TableColumn<Client, Integer> idColumn; // java est un langage typé donc ,  si  j'autilise  juste tableColumn ( c'est  je dit à  java l'objet de controle  qui  va controller idColumn dans javafx est une boite ??
    @FXML private TableColumn<Client, String> nomColumn;
    @FXML private TableColumn<Client, String> prenomColumn;
    @FXML private TableColumn<Client, String> emailColumn;
    @FXML private TableColumn<Client, String> telephoneColumn;
    @FXML private TableColumn<Client, String> adresseColumn;
    @FXML private TableColumn<Client, Void> actionsColumn;

    private ObservableList<Client> clientsList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configuration des colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
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
        // searchField est un id pour le champ de recherche
        String searchTerm = searchField.getText(); // c'est pour récupèrer le contenu du champ
        ClientService clientService = new ClientService();
        ObservableList<Client> searchResults = FXCollections.observableArrayList(clientService.searchClients(searchTerm));
        clientsTable.setItems(searchResults);
    }

    @FXML
    private void addClient() {
        //lorsque le client va clicqué sur le botton il va allée cherche ici sur cette méthode
        System.out.println("le button d'ajouter  un client est cliqué  maintenant pour ajouter un client");
        //mais à ce moment là le controller va applée le service pour récuperre l'implémentation  de la méthode qui va réellement faire l'ajout
        ClientService clientService = new ClientService();
        //pour ajouter cet  client on aura besoin que lapplication  backend soit connecter .
        boolean ajoutReussi = clientService.addClient(new Client(3, "smahan", "boulmane", "email@example.com", "123456789", "Azilal/hey el wahda"));

    }

    private void loadClients() {
        // Données d'exemple pour tester les couleurs
        clientsList.addAll(
            new Client(1, "Dupont", "Jean", "jean.dupont@email.com", "0123456789", "123 Rue de la Paix"),
            new Client(2, "Martin", "Marie", "marie.martin@email.com", "0987654321", "456 Avenue des Champs")
        );
        clientsTable.setItems(clientsList);
    }

    // Classe Client simple
    public static class Client {
        private int id;
        private String nom;
        private String prenom;
        private String email;
        private String telephone;
        private String adresse;

        public Client(int id, String nom, String prenom, String email, String telephone, String adresse) {
            this.id = id;
            this.nom = nom;
            this.prenom = prenom;
            this.email = email;
            this.telephone = telephone;
            this.adresse = adresse;
        }

        public int getId() { return id; }
        public String getNom() { return nom; }
        public String getPrenom() { return prenom; }
        public String getEmail() { return email; }
        public String getTelephone() { return telephone; }
        public String getAdresse() { return adresse; }
    }
}