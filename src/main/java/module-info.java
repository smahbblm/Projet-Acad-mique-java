module com.stock {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;

    uses java.sql.Driver;

    opens com.stock to javafx.fxml;
    opens com.stock.Controller.Dashboard_Administrateur to javafx.fxml;
    opens com.stock.Controller.controllerResponsableApprovisionnements to javafx.fxml;

    exports com.stock;
    exports com.stock.Controller.Dashboard_Administrateur;
    exports com.stock.Controller.controllerResponsableApprovisionnements;
    exports com.stock.model.utilisateur;
    exports com.stock.model.produit;
    exports com.stock.model.partenaire;
    exports com.stock.model.document;
    exports com.stock.model.stock;
    exports com.stock.model.systeme;
}
