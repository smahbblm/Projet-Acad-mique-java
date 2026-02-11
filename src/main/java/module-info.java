module com.stock {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;

    uses java.sql.Driver;

    opens com.stock to javafx.fxml;
    opens com.stock.Controller to javafx.fxml;
    opens com.stock.Controller.controllerResponsableApprovisionnements to javafx.fxml;

    exports com.stock;
    exports com.stock.Controller;
    exports com.stock.Controller.controllerResponsableApprovisionnements;

}
