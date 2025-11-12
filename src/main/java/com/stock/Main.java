package com.stock;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

//avand de crée  la classe qui va hérité de la classe Application on dois crée les  fichiers fxml
public  class Main extends Application  {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DashboardChef_vents.fxml"));
        //charger le contenu du fichier dashboard.fxml
        Parent root = loader.load();
        Scene scene = new Scene(root);

        // 2. AJOUTER CETTE LIGNE pour lier le fichier CSS à la scène
        scene.getStylesheets().add(getClass().getResource("/css/css_dashboard_chef_v.css").toExternalForm());

        //les infos sur la fenetre
        stage.setTitle("Tableau de bord des vents");
        stage.setScene(scene);
        stage.show();
    }
}