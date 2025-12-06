package com.stock.Controler;

import javafx.fxml.FXML;
import javafx.scene.input.DataFormat;

import javafx.scene.control.Label;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class TableuController {
    @FXML
    private Label dateLabel;
    @FXML
    private Label quantitProduits;
    @FXML
    private Label nbrMouvements;
    

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
        String formattedDate = LocalDate.now().format(formatter);
        dateLabel.setText(formattedDate);

    }


}
