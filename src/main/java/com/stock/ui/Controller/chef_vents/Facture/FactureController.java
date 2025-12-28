package com.stock.ui.Controller.chef_vents.Facture;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.util.List;
import com.stock.service.FactureServce;
import com.stock.model.document.Facture;

public class FactureController {

    // les cartes seront affiché dynamiquement et chargement des elements qui existenent en fxml .
    @FXML private Button btnNouvelleFacture;
    @FXML private FlowPane facturesContainer;
    @FXML private Button AfficheFcatures;

    @FXML
    public void initialize() {
        System.out.println("FactureController initialisé !");
        System.out.println("la fin d'initialisation ");
    }

    @FXML
    public void ajouterFacture() {
        System.out.println("Bouton Nouvelle Facture cliqué!");
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/gestionChef-vents/factures/ajout_facture.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Ajouter une facture");
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
            System.out.println("Formulaire d'ajout ouvert avec succès");
        } catch (Exception e) {
            System.err.println("ERREUR lors de l'ouverture du formulaire: " + e.getMessage());
            e.printStackTrace();
            
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible d'ouvrir le formulaire");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    //la méthode qui permet d'afficher les cartes des factures
    public void affiche_factures(Event event ){
        System.out.println("je suis dans la méthode qui permet d'afficher les cartes des factures ");
        FactureServce service = new FactureServce();
        List<Facture> service_factures = service.recuper_allfactures();
        //à ce niveau on a récupérer les factures depuis le dao
        System.out.println("la liste des factures récupérées est : " + service_factures.toString());
        // je dois vider le conteneur avant d'ajouter les nouvelles cartes  et c'est très important
        facturesContainer.getChildren().clear();
         for (Facture facture : service_factures) {
            System.out.println("création de facture: " + facture.toString());
            VBox carte = createFactureCard(facture);
            //c'est ça la ligne  qui rendre  vbox visible à l'écran
            facturesContainer.getChildren().add(carte);

        }
    }

/*
    // Méthode pour charger les factures depuis le DAO
    private void loadFacturesFromDatabase() {
        System.out.println("Chargement des factures depuis la base de données");
        FactureServce factsert = new FactureServce();
        //l'appele  de la méthode de la classe service pour récupérer les données
        List<Object> listefactures = factsert.allfacture();
        System.out.println("liste des factures récupérées : " + listefactures.toString());

        // 2. Vider le conteneur avant d'ajouter les nouvelles cartes
        facturesContainer.getChildren().clear();
        
        // 3. Créer une carte pour chaque facture
        //avand la connexion avec le backend j'ai aucun cartes
        for (Object facture : listefactures) {
            System.out.println("création de facture: " + facture);
            VBox carte = createFactureCard(facture);
            facturesContainer.getChildren().add(carte);
        }
    }
    */
    
    // Méthode pour créer une carte de facture
    private VBox createFactureCard(Facture facture) {
        System.out.println("je suis dans la méthode qui crée lesrts en sebasant sur les données que j'ai le db");
        VBox carte = new VBox();
        // Définir la taille de la carte
        carte.setPrefSize(280, 200);
        // Définir le style de la carte
        carte.setStyle("-fx-background-color: white; -fx-spacing: 10;");
        
        // Créer les éléments avec les vraies données
        Text titre = new Text("Facture ");
        Text id = new Text("ID: " + facture.getNumero());
        Text dateFacture = new Text("Date de facture: " + facture.getDateFacture());
        Text dateEcheance = new Text("Date d'échéance: " + facture.getDateEcheance());
        Text montantHT = new Text("Montant HT: " + facture.getMontantHT());
        Text montantTVA = new Text("Montant TVA: " + facture.getMontantTVA());
        Text montantTTC = new Text("Montant TTC: " + facture.getMontantTTC());
        Text statut = new Text("Statut: " + facture.getStatut());
        Text idClient = new Text("Client: " + (facture.getClient() != null ? facture.getClientNom() : "N/A"));
        carte.getChildren().addAll(titre, id,dateFacture,dateEcheance,montantHT,montantTVA,montantTTC,statut,idClient);
        // Appliquer le style bleu à tous les textes
        for (var node : carte.getChildren()) {
            if (node instanceof Text) {
                ((Text) node).setStyle("-fx-fill: blue;");
            }
        }
        return carte;
    }

}


