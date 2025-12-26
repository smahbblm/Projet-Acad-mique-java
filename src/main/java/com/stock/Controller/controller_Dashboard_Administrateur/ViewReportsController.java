package com.stock.Controller.controller_Dashboard_Administrateur;

import com.stock.model.systeme.Rapport;
import com.stock.service.RapportService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import java.time.LocalDate;

public class ViewReportsController {

    @FXML private TextField filterField;
    @FXML private DatePicker dateFilter;

    @FXML private TableView<Rapport> reportTable;
    @FXML private TableColumn<Rapport, String> nameColumn;
    @FXML private TableColumn<Rapport, String> typeColumn;
    @FXML private TableColumn<Rapport, String> dateColumn;
    @FXML private TableColumn<Rapport, String> formatColumn;
    @FXML private TableColumn<Rapport, Void> actionColumn;

    private final RapportService rapportService = new RapportService();
    private ObservableList<Rapport> rapports = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configColumns();
        loadRapports();
        setupFilters();
        addActionButtons();
    }

    private void configColumns() {
        nameColumn.setCellValueFactory(d ->
                new SimpleStringProperty("Rapport #" + d.getValue().getIdRapport()));
        typeColumn.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getType()));
        dateColumn.setCellValueFactory(d ->
                new SimpleStringProperty(d.getValue().getDateGeneration().toString()));
        formatColumn.setCellValueFactory(d ->
                new SimpleStringProperty("TXT / PDF"));
    }

    private void loadRapports() {
        try {
            rapports.setAll(rapportService.consulterTousLesRapports());
            reportTable.setItems(rapports);
        } catch (Exception e) {
            showError("Erreur", e.getMessage());
        }
    }

    private void setupFilters() {
        FilteredList<Rapport> filtered = new FilteredList<>(rapports, p -> true);

        filterField.textProperty().addListener((obs,o,n) -> filtered.setPredicate(this::match));
        dateFilter.valueProperty().addListener((obs,o,n) -> filtered.setPredicate(this::match));

        reportTable.setItems(filtered);
    }

    private boolean match(Rapport r) {
        String text = filterField.getText() == null ? "" : filterField.getText().toLowerCase();
        LocalDate date = dateFilter.getValue();

        boolean okText = r.getType().toLowerCase().contains(text)
                || r.getContenu().toLowerCase().contains(text);

        boolean okDate = date == null || r.getDateGeneration().isEqual(date);
        return okText && okDate;
    }

    private void addActionButtons() {
        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button view = new Button("Voir");
            private final Button del = new Button("Supprimer");

            {
                view.setOnAction(e -> showReport(getTableView().getItems().get(getIndex())));
                del.setOnAction(e -> deleteReport(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void v, boolean empty) {
                super.updateItem(v, empty);
                setGraphic(empty ? null : new HBox(8, view, del));
            }
        });
    }

    private void showReport(Rapport r) {
        TextArea area = new TextArea(r.getContenu());
        area.setEditable(false);

        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Rapport");
        a.setHeaderText(r.getType());
        a.getDialogPane().setContent(area);
        a.showAndWait();
    }

    private void deleteReport(Rapport r) {
        try {
            rapportService.supprimerRapport(r.getIdRapport());
            rapports.remove(r);
        } catch (Exception e) {
            showError("Erreur suppression", e.getMessage());
        }
    }

    private void showError(String t, String m) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(t);
        a.setContentText(m);
        a.show();
    }
}
