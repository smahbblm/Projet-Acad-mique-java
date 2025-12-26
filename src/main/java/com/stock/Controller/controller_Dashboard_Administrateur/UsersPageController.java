package com.stock.Controller.controller_Dashboard_Administrateur;

import com.stock.model.utilisateur.Utilisateur;
import com.stock.service.UtilisateurService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class UsersPageController {

    @FXML private TableView<Utilisateur> usersTable;
    @FXML private TableColumn<Utilisateur, Integer> idColumn;
    @FXML private TableColumn<Utilisateur, String> nomColumn;
    @FXML private TableColumn<Utilisateur, String> prenomColumn;
    @FXML private TableColumn<Utilisateur, String> emailColumn;
    @FXML private TableColumn<Utilisateur, String> roleColumn;
    @FXML private TableColumn<Utilisateur, Boolean> actifColumn;
    @FXML private TableColumn<Utilisateur, String> dateCreationColumn;
    @FXML private TableColumn<Utilisateur, Void> actionColumn;
    @FXML private TextField searchField;
    @FXML private TableColumn<Utilisateur, String> passwordColumn;


    private ObservableList<Utilisateur> usersList = FXCollections.observableArrayList();
    private UtilisateurService utilisateurService;

    @FXML
    public void initialize() {
        try {
            utilisateurService = new UtilisateurService();
            setupColumns();
            loadUsers();          // 🔹 Charge les utilisateurs existants
            setupSearchFilter();
            setupActionButtons();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur d'initialisation", e.getMessage());
        }
    }

    /** Charge tous les utilisateurs existants dans la table */
    public void loadUsers() {
        try {
            // 🔹 Récupère tous les utilisateurs depuis la base
            usersList = FXCollections.observableArrayList(utilisateurService.getAllUsers());
            usersTable.setItems(usersList);
            usersTable.refresh();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur de chargement", e.getMessage());
        }
    }

    private void setupColumns() {
        idColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getIdUtilisateur()).asObject());
        nomColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNom()));
        prenomColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPrenom()));
        emailColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEmail()));
        roleColumn.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getRole().substring(0,1).toUpperCase() +
                                c.getValue().getRole().substring(1).toLowerCase()
                )
        );
        passwordColumn.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(c.getValue().getPassword())
        );

        actifColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleBooleanProperty(c.getValue().isActif()).asObject());
        dateCreationColumn.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getDateCreation() != null ? c.getValue().getDateCreation().toString() : ""
        ));
    }

    private void setupSearchFilter() {
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String filter = newVal.toLowerCase();

            usersTable.setItems(usersList.filtered(u ->
                    u.getNom().toLowerCase().contains(filter) ||
                            u.getPrenom().toLowerCase().contains(filter) ||
                            u.getEmail().toLowerCase().contains(filter) ||
                            u.getRole().toLowerCase().contains(filter)   // 🔹 Filtre par rôle
            ));
        });
    }

    private void setupActionButtons() {
        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("Modifier");
            private final Button deleteBtn = new Button("Supprimer");
            private final HBox pane = new HBox(10, editBtn, deleteBtn);

            {
                editBtn.setOnAction(e -> {
                    Utilisateur user = getCurrentUser();
                    if (user != null) openEditUserPage(user);
                });

                deleteBtn.setOnAction(e -> {
                    Utilisateur user = getCurrentUser();
                    if (user != null) deleteUser(user);
                });
            }

            private Utilisateur getCurrentUser() {
                int i = getIndex();
                if (i >= 0 && i < usersTable.getItems().size()) return usersTable.getItems().get(i);
                return null;
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void deleteUser(Utilisateur u) {
        try {
            utilisateurService.deleteUser(u.getIdUtilisateur());
            loadUsers();  // 🔹 Recharge après suppression
            showInfo("Succès", "Utilisateur supprimé");
        } catch (Exception e) {
            showError("Erreur suppression", e.getMessage());
        }
    }

    private void openEditUserPage(Utilisateur u) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard_Administrateur/EditUserPage.fxml"));
            Parent root = loader.load();
            EditUserPageController controller = loader.getController();
            controller.setUserData(u);
            controller.setParentController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier l'utilisateur");
            stage.show();

        } catch (Exception e) {
            showError("Erreur ouverture page", e.getMessage());
        }
    }

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
            showError("Erreur ouverture page", e.getMessage());
        }
    }

    private void showInfo(String t, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(t); a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }

    private void showError(String t, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(t); a.setHeaderText(null); a.setContentText(msg); a.showAndWait();
    }
}
