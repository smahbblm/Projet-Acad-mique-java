module com.stock {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires java.desktop;
    // PDFBox - NOMS CORRECTS DES MODULES
    requires org.apache.pdfbox;
    requires org.apache.fontbox;

    requires javafx.swing;
    requires mysql.connector.j;


    uses java.sql.Driver;

    opens com.stock to javafx.fxml;
    opens com.stock.Controler to javafx.fxml;
    opens com.stock.model.utilisateur to javafx.base;
    opens com.stock.model.produit to javafx.base;
    opens com.stock.model.partenaire to javafx.base;
    opens com.stock.model.document to javafx.base;
    opens com.stock.model.stock to javafx.base;

    exports com.stock;
    exports com.stock.Controler;
    exports com.stock.model.utilisateur;
    exports com.stock.model.produit;
    exports com.stock.model.partenaire;
    exports com.stock.model.document;
    exports com.stock.model.stock;
    exports com.stock.model.systeme;
}