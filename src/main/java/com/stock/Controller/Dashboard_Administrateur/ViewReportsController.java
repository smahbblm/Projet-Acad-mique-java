package com.stock.Controller.Dashboard_Administrateur;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.time.LocalDate;

public class ViewReportsController {

    @FXML private TableView<Report> reportTable;
    @FXML private TableColumn<Report, String> nameColumn;
    @FXML private TableColumn<Report, String> typeColumn;
    @FXML private TableColumn<Report, LocalDate> dateColumn;
    @FXML private TableColumn<Report, String> formatColumn;
    @FXML private TableColumn<Report, Void> actionColumn;

    @FXML private TextField filterField;
    @FXML private DatePicker dateFilter;

    private ObservableList<Report> masterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        // --- Configuration des colonnes ---
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        formatColumn.setCellValueFactory(new PropertyValueFactory<>("format"));

        // --- Colonne Action avec deux boutons ---
        actionColumn.setCellFactory(col -> new TableCell<Report, Void>() {

            private final Button openBtn = new Button("Ouvrir");
            private final Button delBtn = new Button("Supprimer");
            private final HBox container = new HBox(10);

            {
                openBtn.setStyle("-fx-background-color:#2b6cb0; -fx-text-fill:white;");
                delBtn.setStyle("-fx-background-color:#e53e3e; -fx-text-fill:white;");

                container.getChildren().addAll(openBtn, delBtn);

                openBtn.setOnAction(e -> {
                    Report report = getTableView().getItems().get(getIndex());
                    if (report != null) {
                        System.out.println("Ouverture du rapport : " + report.getName());
                    }
                });

                delBtn.setOnAction(e -> {
                    int idx = getIndex();
                    if (idx >= 0 && idx < getTableView().getItems().size()) {
                        Report report = getTableView().getItems().get(idx);
                        masterData.remove(report); // suppression correcte
                        System.out.println("Rapport supprimé : " + report.getName());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                }
            }
        });



        // --- Définir la hauteur fixe des lignes et nombre de lignes visibles ---
        int rowHeight = 40;
        int visibleRows = 13;
        reportTable.setFixedCellSize(rowHeight);
        reportTable.setPrefHeight(visibleRows * rowHeight + 30); // 30 pour l'entête
        reportTable.setMinHeight(visibleRows * rowHeight + 30);
        reportTable.setMaxHeight(visibleRows * rowHeight + 30);

        // --- Données statiques ---
        masterData.addAll(
                new Report("Rapport A", "Entrées", LocalDate.now().minusDays(1), "PDF"),
                new Report("Rapport B", "Sorties", LocalDate.now().minusDays(2), "Excel"),
                new Report("Rapport C", "Inventaire", LocalDate.now().minusDays(3), "CSV"),
                new Report("Rapport D", "Par utilisateur", LocalDate.now(), "PDF"),
                new Report("Rapport E", "Entrées", LocalDate.now().minusDays(1), "PDF"),
                new Report("Rapport F", "Sorties", LocalDate.now().minusDays(2), "Excel"),
                new Report("Rapport G", "Inventaire", LocalDate.now().minusDays(3), "CSV"),
                new Report("Rapport H", "Par utilisateur", LocalDate.now(), "PDF"),
                new Report("Rapport I", "Entrées", LocalDate.now().minusDays(1), "PDF"),
                new Report("Rapport J", "Sorties", LocalDate.now().minusDays(2), "Excel"),
                new Report("Rapport K", "Inventaire", LocalDate.now().minusDays(3), "CSV"),
                new Report("Rapport L", "Par utilisateur", LocalDate.now(), "PDF"),
                new Report("Rapport G", "Inventaire", LocalDate.now().minusDays(3), "CSV"),
                new Report("Rapport H", "Par utilisateur", LocalDate.now(), "PDF"),
                new Report("Rapport I", "Entrées", LocalDate.now().minusDays(1), "PDF"),
                new Report("Rapport J", "Sorties", LocalDate.now().minusDays(2), "Excel"),
                new Report("Rapport K", "Inventaire", LocalDate.now().minusDays(3), "CSV"),
                new Report("Rapport L", "Par utilisateur", LocalDate.now(), "PDF")
        );

        // --- Filtrage dynamique ---
        FilteredList<Report> filteredData = new FilteredList<>(masterData, p -> true);
        filterField.textProperty().addListener((obs, oldVal, newVal) -> updateFilter(filteredData));
        dateFilter.valueProperty().addListener((obs, oldVal, newVal) -> updateFilter(filteredData));

        // --- Tri et affichage ---
        SortedList<Report> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(reportTable.comparatorProperty());
        reportTable.setItems(sortedData);
    }

    private void updateFilter(FilteredList<Report> filteredData) {
        String text = filterField.getText();
        LocalDate selectedDate = dateFilter.getValue();

        filteredData.setPredicate(report -> {
            boolean textMatch = true;
            if (text != null && !text.isEmpty()) {
                String lower = text.toLowerCase();
                textMatch = report.getName().toLowerCase().contains(lower)
                        || report.getType().toLowerCase().contains(lower);
            }

            boolean dateMatch = true;
            if (selectedDate != null) {
                dateMatch = report.getDate().equals(selectedDate);
            }

            return textMatch && dateMatch;
        });
    }

    // --- Classe interne statique pour les rapports ---
    public static class Report {
        private final String name;
        private final String type;
        private final LocalDate date;
        private final String format;

        public Report(String name, String type, LocalDate date, String format) {
            this.name = name;
            this.type = type;
            this.date = date;
            this.format = format;
        }

        public String getName() { return name; }
        public String getType() { return type; }
        public LocalDate getDate() { return date; }
        public String getFormat() { return format; }
    }
}
