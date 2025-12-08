package com.stock.Controller.Dashboard_Administrateur;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UsersPageController {

    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, Integer> idColumn;
    @FXML private TableColumn<User, String> nomColumn;
    @FXML private TableColumn<User, String> prenomColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, String> roleColumn;
    @FXML private TableColumn<User, Boolean> actifColumn;
    @FXML private TableColumn<User, Void> actionColumn;
    @FXML private TextField searchField;

    private ObservableList<User> usersList;

    @FXML
    public void initialize() {

        // Colonnes
        idColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        nomColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNom()));
        prenomColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPrenom()));
        emailColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
        roleColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRole()));
        actifColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleBooleanProperty(data.getValue().isActif()).asObject());

        // Boutons Modifier / Supprimer
        actionColumn.setCellFactory(col -> new TableCell<User, Void>() {
            private final Button editBtn = new Button("Modifier");
            private final Button deleteBtn = new Button("Supprimer");
            private final HBox pane = new HBox(5, editBtn, deleteBtn);

            {
                editBtn.setStyle("-fx-background-color:#ffd700;");
                deleteBtn.setStyle("-fx-background-color:#e53e3e; -fx-text-fill:white;");

                editBtn.setOnAction(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    UsersPageController.this.openEditUserPage(user);
                });

                deleteBtn.setOnAction(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    usersList.remove(user);
                    showInfo("Supprimer", "Utilisateur supprimé : " + user.getNom());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });

        // Données statiques
        usersList = FXCollections.observableArrayList(
                new User(1, "Dupont", "Marie", "marie@stock.com", "MAGASINIER", true),
                new User(2, "Smith", "Alice", "alice@stock.com", "R_VENTES", false),
                new User(3, "Martin", "Paul", "paul@stock.com", "MAGASINIER", true),
                new User(4, "Durand", "Lucie", "lucie@stock.com", "R_APPROVISIONNEMENT", true)
        );

        usersTable.setItems(usersList);

        // Filtrage dynamique
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String filter = newVal.toLowerCase();
            usersTable.setItems(usersList.filtered(user ->
                    user.getNom().toLowerCase().contains(filter) ||
                            user.getPrenom().toLowerCase().contains(filter) ||
                            user.getEmail().toLowerCase().contains(filter)
            ));
        });
        usersTable.setRowFactory(tv -> {
            TableRow<UsersPageController.User> row = new TableRow<>();
            row.setPrefHeight(40);
            return row;
        });
    }

    // --------------------------------------------------
    //   MISE À JOUR DE LA TABLE APRÈS MODIFICATION
    // --------------------------------------------------
    public void updateUserInTable(User oldUser, User newUser) {
        int index = usersList.indexOf(oldUser);
        if (index != -1) {
            usersList.set(index, newUser);
        }
    }

    public void refreshTable() {
        usersTable.refresh();
    }

    // Ouvrir la page d'édition
    private void openEditUserPage(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard_Administrateur/EditUserPage.fxml"));
            Parent root = loader.load();

            EditUserPageController controller = loader.getController();
            controller.setUserData(user);
            controller.setParentController(this);

            Stage stage = new Stage();
            stage.setTitle("Modifier l'utilisateur");
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Ajouter un utilisateur
    public void addUserToTable(User user) {
        usersList.add(user);
    }

    // Ouvrir la page AddUser
    @FXML
    private void handleShowAddUserPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard_Administrateur/AddUserPage.fxml"));
            Parent root = loader.load();

            AddUserPageController controller = loader.getController();
            controller.setParentController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un utilisateur");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Alertes
    private void showInfo(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // --------------------------------------------------
    //                CLASS INTERNE USER
    // --------------------------------------------------
    public static class User {
        private final int id;
        private String nom;
        private String prenom;
        private String email;
        private String role;
        private boolean actif;

        public User(int id, String nom, String prenom, String email, String role, boolean actif) {
            this.id = id;
            this.nom = nom;
            this.prenom = prenom;
            this.email = email;
            this.role = role;
            this.actif = actif;
        }

        public int getId() { return id; }
        public String getNom() { return nom; }
        public String getPrenom() { return prenom; }
        public String getEmail() { return email; }
        public String getRole() { return role; }
        public boolean isActif() { return actif; }

        public void setNom(String nom) { this.nom = nom; }
        public void setPrenom(String prenom) { this.prenom = prenom; }
        public void setEmail(String email) { this.email = email; }
        public void setRole(String role) { this.role = role; }
        public void setActif(boolean actif) { this.actif = actif; }
    }
}
