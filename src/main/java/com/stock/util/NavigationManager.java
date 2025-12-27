package com.stock.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe pour gérer la navigation entre les vues
 */
public class NavigationManager {
    private Stage stage;
    private static NavigationManager instance;

    private NavigationManager() {
    }

    public static synchronized NavigationManager getInstance() {
        if (instance == null) {
            instance = new NavigationManager();
        }
        return instance;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void navigate(String fxmlPath, String title) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Scene scene = new Scene(loader.load());

        // Charger le fichier CSS
        var cssResource = getClass().getResource("/css/style.css");
        if (cssResource != null) {
            scene.getStylesheets().add(cssResource.toExternalForm());
        }

        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    public void navigateWithScene(String fxmlPath, Stage window) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Scene scene = new Scene(loader.load());

        // Charger le fichier CSS
        var cssResource = getClass().getResource("/css/style.css");
        if (cssResource != null) {
            scene.getStylesheets().add(cssResource.toExternalForm());
        }

        window.setScene(scene);
    }
}

