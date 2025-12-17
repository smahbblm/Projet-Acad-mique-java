package com.stock.Controller.controller_Dashboard_Administrateur;

import com.stock.model.stock.MouvementStock;
import com.stock.service.MouvementStockService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleIntegerProperty;



import java.time.format.DateTimeFormatter;

public class StockMovementController {

    @FXML private TableView<MouvementStock> movementTable;
    @FXML private TableColumn<MouvementStock, Integer> idColumn;
    @FXML private TableColumn<MouvementStock, String> DateColumn;
    @FXML private TableColumn<MouvementStock, String> ArticleColumn;
    @FXML private TableColumn<MouvementStock, String> TypeColumn;
    @FXML private TableColumn<MouvementStock, Integer> QuantiteColumn;
    @FXML private TableColumn<MouvementStock, Integer> StockAvantColumn;
    @FXML private TableColumn<MouvementStock, Integer> StockApresColumn;
    @FXML private TableColumn<MouvementStock, String> UtilisateurColumn;
    @FXML private TableColumn<MouvementStock, String> CommentaireColumn;
    @FXML private TextField filterField;
    @FXML private DatePicker dateFilter;
    @FXML private TableColumn<MouvementStock, String> ProduitColumn;
    @FXML private Label totalEntreesLabel;
    @FXML private Label totalSortiesLabel;
    @FXML private Label stockActuelLabel;

    private ObservableList<MouvementStock> masterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        // --- Colonnes ---
        idColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getIdMouvement()).asObject());
        DateColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getDateMouvement().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        );
        ProduitColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getProduit() != null
                        ? cell.getValue().getProduit().getDesignation()
                        : "")
        );

        ArticleColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getProduit().getReference()));
        TypeColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTypeMouvement()));
        QuantiteColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getQuantite()).asObject());
        StockAvantColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getStockAvant()).asObject());
        StockApresColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getStockApres()).asObject());
        UtilisateurColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getUtilisateur() != null ? cell.getValue().getUtilisateur().getNom() : "")
        );
        CommentaireColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getReference()));


        // --- Charger les données depuis le service ---
        try {
            MouvementStockService service = new MouvementStockService();
            masterData.addAll(service.consulterTousMouvements());
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erreur lors du chargement des mouvements : " + e.getMessage()).showAndWait();
        }

        // --- Mettre à jour les totaux dynamiquement ---
        updateTotals();

        // --- Filtrage dynamique ---
        FilteredList<MouvementStock> filteredData = new FilteredList<>(masterData, p -> true);

        filterField.textProperty().addListener((obs, oldVal, newVal) -> updateFilter(filteredData));
        dateFilter.valueProperty().addListener((obs, oldVal, newVal) -> updateFilter(filteredData));

        SortedList<MouvementStock> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(movementTable.comparatorProperty());
        movementTable.setItems(sortedData);
    }

    private void updateTotals() {
        int totalEntrees = masterData.stream()
                .filter(m -> "ENTREE".equalsIgnoreCase(m.getTypeMouvement()))
                .mapToInt(MouvementStock::getQuantite)
                .sum();

        int totalSorties = masterData.stream()
                .filter(m -> "SORTIE".equalsIgnoreCase(m.getTypeMouvement()))
                .mapToInt(MouvementStock::getQuantite)
                .sum();

        int stockActuel = totalEntrees - totalSorties;

        totalEntreesLabel.setText(String.valueOf(totalEntrees));
        totalSortiesLabel.setText(String.valueOf(totalSorties));
        stockActuelLabel.setText(String.valueOf(stockActuel));
    }

    private void updateFilter(FilteredList<MouvementStock> filteredData) {
        String text = filterField.getText();
        var selectedDate = dateFilter.getValue();

        filteredData.setPredicate(movement -> {
            boolean textMatch = true;
            if (text != null && !text.isEmpty()) {
                String lower = text.toLowerCase();
                textMatch =
                        movement.getProduit() != null && movement.getProduit().getDesignation().toLowerCase().contains(lower) ||
                                movement.getProduit().getReference().toLowerCase().contains(lower) ||
                                movement.getTypeMouvement().toLowerCase().contains(lower) ||
                                (movement.getUtilisateur() != null && movement.getUtilisateur().getNom().toLowerCase().contains(lower)) ||
                                (movement.getReference() != null && movement.getReference().toLowerCase().contains(lower));

            }

            boolean dateMatch = true;
            if (selectedDate != null) {
                dateMatch = movement.getDateMouvement().equals(selectedDate);
            }

            return textMatch && dateMatch;
        });
    }
}
