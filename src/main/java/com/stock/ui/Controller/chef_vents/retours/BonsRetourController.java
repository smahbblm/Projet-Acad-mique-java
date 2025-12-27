package com.stock.ui.Controller.chef_vents.retours;

import com.stock.model.document.BonRetour;
import com.stock.service.BonRetourService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class BonsRetourController implements javafx.fxml.Initializable {

    @FXML private Button btnNouveauRetour;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> filtreStatut;
    @FXML private TableView<BonRetour> retoursTable;
    @FXML private TableColumn<BonRetour, String> numeroColumn;
    @FXML private TableColumn<BonRetour, LocalDate> dateColumn;
    @FXML private TableColumn<BonRetour, String> clientColumn;
    @FXML private TableColumn<BonRetour, String> motifColumn;
    @FXML private TableColumn<BonRetour, String> statutColumn;
    @FXML private TableColumn<BonRetour, Void> actionsColumn;

    private ObservableList<BonRetour> retoursList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("BonsRetourController initialisé");
        
        // Configurer les colonnes
        numeroColumn.setCellValueFactory(new PropertyValueFactory<>("numero"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateRetour"));
        clientColumn.setCellValueFactory(new PropertyValueFactory<>("clientNom"));
        motifColumn.setCellValueFactory(new PropertyValueFactory<>("motif"));
        statutColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        
        // Configurer les filtres
        filtreStatut.getItems().addAll("BROUILLON", "CREE", "VALIDEE", "TRANSMISE");
        
        setupActionsColumn();
        chargerBonsRetour();
    }

    private void setupActionsColumn() {
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button btnValider = new Button("Valider");
            private final Button btnTransmettre = new Button("Transmettre");

            {
                btnValider.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                btnTransmettre.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");

                btnValider.setOnAction(e -> {
                    BonRetour retour = getTableView().getItems().get(getIndex());
                    validerRetour(retour);
                });

                btnTransmettre.setOnAction(e -> {
                    BonRetour retour = getTableView().getItems().get(getIndex());
                    transmettreRetour(retour);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    javafx.scene.layout.HBox hbox = new javafx.scene.layout.HBox(5);
                    hbox.getChildren().addAll(btnValider, btnTransmettre);
                    setGraphic(hbox);
                }
            }
        });
    }

    private void chargerBonsRetour() {
        BonRetourService service = new BonRetourService();
        retoursList = service.getAllBonsRetour();
        retoursTable.setItems(retoursList);
    }

    @FXML
    private void ajouterBonRetour() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Nouveau Bon de Retour");
        alert.setHeaderText("Fonctionnalité en cours de développement");
        alert.setContentText("Le formulaire d'ajout de bon de retour sera bientôt disponible");
        alert.showAndWait();
    }

    private void validerRetour(BonRetour retour) {
        BonRetourService service = new BonRetourService();
        if (service.validerBonRetour(retour.getIdBonRetour())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText("Bon de retour validé");
            alert.showAndWait();
            chargerBonsRetour();
        }
    }

    private void transmettreRetour(BonRetour retour) {
        BonRetourService service = new BonRetourService();
        if (service.transmettreBoRetour(retour.getIdBonRetour())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText("Bon de retour transmis");
            alert.showAndWait();
            chargerBonsRetour();
        }
    }
}
