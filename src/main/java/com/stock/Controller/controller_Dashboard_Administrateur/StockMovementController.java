package com.stock.Controller.Dashboard_Administrateur;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.format.DateTimeFormatter;

public class StockMovementController {

    @FXML private TableView<StockMovement> movementTable;
    @FXML private TableColumn<StockMovement, Integer> idColumn;
    @FXML private TableColumn<StockMovement, String> DateColumn;
    @FXML private TableColumn<StockMovement, String> ArticleColumn;
    @FXML private TableColumn<StockMovement, String> TypeColumn;
    @FXML private TableColumn<StockMovement, Integer> QuantiteColumn;
    @FXML private TableColumn<StockMovement, Integer> StockAvantColumn;
    @FXML private TableColumn<StockMovement, Integer> StockApresColumn;
    @FXML private TableColumn<StockMovement, String> UtilisateurColumn;
    @FXML private TableColumn<StockMovement, String> CommentaireColumn;
    @FXML private TextField filterField;
    @FXML private DatePicker dateFilter;

    private ObservableList<StockMovement> masterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        // ------ Colonnes ------
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        DateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        ArticleColumn.setCellValueFactory(new PropertyValueFactory<>("article"));
        TypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        QuantiteColumn.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        StockAvantColumn.setCellValueFactory(new PropertyValueFactory<>("stockAvant"));
        StockApresColumn.setCellValueFactory(new PropertyValueFactory<>("stockApres"));
        UtilisateurColumn.setCellValueFactory(new PropertyValueFactory<>("utilisateur"));
        CommentaireColumn.setCellValueFactory(new PropertyValueFactory<>("commentaire"));

        // ------ Données test ------
        masterData.addAll(
                new StockMovement(1, "23/11/2025", "Article A", "Entrée", 10, 50, 60, "user2", "Réception fournisseur"),
                new StockMovement(2, "23/11/2025", "Article B", "Sortie", 5, 30, 25, "user1", "Vente client"),
                new StockMovement(3, "24/11/2025", "Article C", "Entrée", 20, 10, 30, "user1", "Retour client"),
                new StockMovement(4, "25/11/2025", "Article D", "Sortie", 8, 60, 52, "user3", "Transfert magasin"),
                new StockMovement(5, "25/11/2025", "Article A", "Sortie", 3, 60, 57, "user2", "Vente"),
                new StockMovement(6, "23/11/2025", "Article A", "Entrée", 10, 50, 60, "user2", "Réception fournisseur"),
                new StockMovement(7, "23/11/2025", "Article B", "Sortie", 5, 30, 25, "user1", "Vente client"),
                new StockMovement(8, "24/11/2025", "Article C", "Entrée", 20, 10, 30, "user1", "Retour client"),
                new StockMovement(9, "25/11/2025", "Article D", "Sortie", 8, 60, 52, "user3", "Transfert magasin"),
                new StockMovement(10, "25/11/2025", "Article A", "Sortie", 3, 60, 57, "user2", "Vente"),
                new StockMovement(11, "23/11/2025", "Article A", "Entrée", 10, 50, 60, "user2", "Réception fournisseur"),
                new StockMovement(12, "23/11/2025", "Article B", "Sortie", 5, 30, 25, "user1", "Vente client"),
                new StockMovement(13, "24/11/2025", "Article C", "Entrée", 20, 10, 30, "user1", "Retour client"),
                new StockMovement(14, "25/11/2025", "Article D", "Sortie", 8, 60, 52, "user3", "Transfert magasin"),
                new StockMovement(15, "25/11/2025", "Article A", "Sortie", 3, 60, 57, "user2", "Vente")
        );

        // ------ Filtrage ------
        FilteredList<StockMovement> filteredData = new FilteredList<>(masterData, p -> true);

        filterField.textProperty().addListener((obs, o, n) -> updateFilter(filteredData));
        dateFilter.valueProperty().addListener((obs, o, n) -> updateFilter(filteredData));

        SortedList<StockMovement> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(movementTable.comparatorProperty());
        movementTable.setItems(sortedData);


        int rowHeight = 45;     // hauteur d'une ligne
        int visibleRows = 10;   // nombre de lignes visibles

        movementTable.setFixedCellSize(rowHeight);
        movementTable.setPrefHeight(visibleRows * rowHeight + 30);  // +30 = header du tableau
        movementTable.setMinHeight(visibleRows * rowHeight + 30);
        movementTable.setMaxHeight(visibleRows * rowHeight + 30);

        // (Optionnel) ajuster la hauteur des lignes
        movementTable.setRowFactory(tv -> {
            TableRow<StockMovement> row = new TableRow<>();
            row.setPrefHeight(rowHeight);
            return row;
        });
        // ========================================================
    }

    private void updateFilter(FilteredList<StockMovement> filteredData) {
        String text = filterField.getText();
        var selectedDate = dateFilter.getValue();

        filteredData.setPredicate(movement -> {

            boolean textMatch = true;
            if (text != null && !text.isEmpty()) {
                String lower = text.toLowerCase();
                textMatch =
                        movement.getArticle().toLowerCase().contains(lower) ||
                                movement.getType().toLowerCase().contains(lower) ||
                                movement.getUtilisateur().toLowerCase().contains(lower) ||
                                movement.getCommentaire().toLowerCase().contains(lower);
            }

            boolean dateMatch = true;
            if (selectedDate != null) {
                String formatted = selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                dateMatch = movement.getDate().equals(formatted);
            }

            return textMatch && dateMatch;
        });
    }

    // 🔥 Classe interne
    public static class StockMovement {
        private final int id;
        private final String date, article, type, utilisateur, commentaire;
        private final int quantite, stockAvant, stockApres;

        public StockMovement(int id, String date, String article, String type, int quantite,
                             int stockAvant, int stockApres, String utilisateur, String commentaire) {
            this.id = id;
            this.date = date;
            this.article = article;
            this.type = type;
            this.quantite = quantite;
            this.stockAvant = stockAvant;
            this.stockApres = stockApres;
            this.utilisateur = utilisateur;
            this.commentaire = commentaire;
        }

        public int getId() { return id; }
        public String getDate() { return date; }
        public String getArticle() { return article; }
        public String getType() { return type; }
        public int getQuantite() { return quantite; }
        public int getStockAvant() { return stockAvant; }
        public int getStockApres() { return stockApres; }
        public String getUtilisateur() { return utilisateur; }
        public String getCommentaire() { return commentaire; }
    }
}
