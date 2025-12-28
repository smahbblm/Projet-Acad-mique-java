
package com.stock;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main_smahan extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Gestion de Stock");
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/form.fxml"));
        Parent root = loader.load();
        
        stage.setScene(new Scene(root));
        stage.show();
    }
}
