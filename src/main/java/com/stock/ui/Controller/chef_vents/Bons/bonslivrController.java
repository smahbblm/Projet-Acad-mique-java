package com.stock.ui.Controller.chef_vents.Bons;

import com.stock.model.document.BonLivraison;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.stock.service.BonService;
import com.stock.util.PDFExporter;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class bonslivrController {


    //La  définition des  elements  fxml  dans notre projet
    @FXML  private Button supprimerbon;
    @FXML  private TextField recherchebon;
    @FXML  private Button btnbonplus;
    @FXML  private TableView<BonLivraison> tablebon;
    @FXML  private TableColumn<BonLivraison, String> colNumero;
    @FXML  private TableColumn<BonLivraison, LocalDate> colDate;
    @FXML  private TableColumn<BonLivraison, String> colClient;
    @FXML  private TableColumn<BonLivraison, String> colStatut;
    @FXML  private TableColumn<BonLivraison, Void> colActions;
    @FXML  private ComboBox<String> filtreStatut;
    @FXML  private ComboBox<String> filtreClient;
    @FXML  private Text totalBonsNumber;
    @FXML  private Text enAttenteNumber;
    @FXML  private Text livresNumber;
    @FXML  private Text annulesNumber;
    @FXML  private Button btnExporter;
    @FXML  private Button btnActualiser;


    public void initialize() {
        System.out.println("je suis exactement le controller du votre  fenetre de gestion de Clients . ");
        chargerBons();
        chargerFiltres();
        
        // Ajouter listener pour recherche en temps réel
        recherchebon.textProperty().addListener((observable, oldValue, newValue) -> filtrerBons());
    }
    
    @FXML
    public void ajoutebon(){
        //chargement de fichier fxml  pour  ajouter un bon de livraison
        System.out.println("chargement du formulaire");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gestionChef-vents/Bons_liv/NouveauBon.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnbonplus.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        }catch(IOException e){
            e.printStackTrace();
        };
    }
    
    //la méthode pour la supprission d'un  bon de livraison
    @FXML
    public void supprimebon() {
        BonService Bonservice = new BonService();
        List<String> listeBons = Bonservice.liste_numero_bonliv();
        System.out.println("listeBons");

        // 2. Créer un dialog de choix
        ChoiceDialog<String> dialog = new ChoiceDialog<>(listeBons.get(0), listeBons);
        dialog.setTitle("Supprimer un bon");
        dialog.setHeaderText("Sélectionner le bon à supprimer");
        dialog.setContentText("Numéro du bon:");

        // 3. Afficher et récupérer le choix
        Optional<String> result = dialog.showAndWait();

        // 4. Si l'utilisateur a choisi
        if (result.isPresent()) {
            String numeroChoisi = result.get();
            Bonservice.supprimerBon(numeroChoisi);
            System.out.println("Bon supprimé : " + numeroChoisi);
            chargerBons();
        }
    }
    
    // Fonction pour récupérer et afficher tous les bons de livraison
    @FXML
    public void chargerBons() {
        BonService Bonservice = new BonService();
        ObservableList<BonLivraison> listeBons = Bonservice.getAllBons();
        tablebon.setItems(listeBons);
        
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateLivraison"));
        colClient.setCellValueFactory(new PropertyValueFactory<>("clientNom"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        
        // Ajouter les boutons d'actions
        colActions.setCellFactory(param -> new TableCell<BonLivraison, Void>() {
            private final Button btnVoir = new Button("Voir");
            private final Button btnModifier = new Button("Modifier");
            private final Button btnSupprimer = new Button("Supprimer");
            private final Button btnExporter = new Button("Exporter");
            
            {
                // Styles des boutons
                btnVoir.setStyle("-fx-font-size: 10px; -fx-padding: 2 5 2 5;");
                btnModifier.setStyle("-fx-font-size: 10px; -fx-padding: 2 5 2 5;");
                btnSupprimer.setStyle("-fx-font-size: 10px; -fx-padding: 2 5 2 5;");
                btnExporter.setStyle("-fx-font-size: 10px; -fx-padding: 2 5 2 5;");
                
                btnVoir.setOnAction(e -> voirBon(getTableView().getItems().get(getIndex())));
                btnModifier.setOnAction(e -> modifierBon(getTableView().getItems().get(getIndex())));
                btnSupprimer.setOnAction(e -> supprimerBonIndividuel(getTableView().getItems().get(getIndex())));
                btnExporter.setOnAction(e -> exporterBonIndividuel(getTableView().getItems().get(getIndex())));
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    javafx.scene.layout.HBox hbox = new javafx.scene.layout.HBox(5);
                    hbox.getChildren().addAll(btnVoir, btnModifier, btnSupprimer, btnExporter);
                    setGraphic(hbox);
                }
            }
        });
        
        // Mettre à jour les statistiques
        mettreAJourStatistiques(listeBons);
    }
    
    // Fonction pour charger les filtres (clients et statuts)
    @FXML
    public void chargerFiltres() {
        BonService BonService = new BonService();
        
        // Charger les clients
        ObservableList<String> listeClients = BonService.getclient();
        filtreClient.setItems(listeClients);
        
        // Charger les statuts
        ObservableList<String> listeStatuts = javafx.collections.FXCollections.observableArrayList(
            "BROUILLON", "EN_ATTENTE", "VALIDEE", "TRANSMISE"
        );
        filtreStatut.setItems(listeStatuts);
        
        // Ajouter les listeners pour filtrage automatique
        filtreClient.setOnAction(e -> filtrerBons());
        filtreStatut.setOnAction(e -> filtrerBons());
    }
    
    // Fonction pour filtrer les bons selon les critères sélectionnés
    private void filtrerBons() {
        BonService BonService = new BonService();
        ObservableList<BonLivraison> tousLesBons = BonService.getAllBons();
        ObservableList<BonLivraison> bonsFiltres = javafx.collections.FXCollections.observableArrayList();
        
        String clientSelectionne = filtreClient.getValue();
        String statutSelectionne = filtreStatut.getValue();
        String recherche = recherchebon.getText().toLowerCase();
        
        for (BonLivraison bon : tousLesBons) {
            boolean correspondClient = (clientSelectionne == null || clientSelectionne.isEmpty() || 
                                       bon.getClientNom().equals(clientSelectionne));
            boolean correspondStatut = (statutSelectionne == null || statutSelectionne.isEmpty() || 
                                       bon.getStatut().equals(statutSelectionne));
            boolean correspondRecherche = (recherche == null || recherche.isEmpty() ||
                                          bon.getNumero().toLowerCase().contains(recherche) ||
                                          bon.getClientNom().toLowerCase().contains(recherche));
            
            if (correspondClient && correspondStatut && correspondRecherche) {
                bonsFiltres.add(bon);
            }
        }
        
        tablebon.setItems(bonsFiltres);
    }
    
    // Fonction pour mettre à jour les statistiques
    private void mettreAJourStatistiques(ObservableList<BonLivraison> listeBons) {
        int total = listeBons.size();
        int enAttente = 0;
        int livres = 0;
        int annules = 0;
        
        for (BonLivraison bon : listeBons) {
            String statut = bon.getStatut();
            if ("EN_ATTENTE".equals(statut) || "BROUILLON".equals(statut)) {
                enAttente++;
            } else if ("VALIDEE".equals(statut) || "TRANSMISE".equals(statut)) {
                livres++;
            } else if ("ANNULE".equals(statut)) {
                annules++;
            }
        }
        
        totalBonsNumber.setText(String.valueOf(total));
        enAttenteNumber.setText(String.valueOf(enAttente));
        livresNumber.setText(String.valueOf(livres));
        annulesNumber.setText(String.valueOf(annules));
    }
    
    // Fonction pour exporter un bon sélectionné
    @FXML
    public void exporterBon() {
        BonLivraison bonSelectionne = tablebon.getSelectionModel().getSelectedItem();
        
        if (bonSelectionne == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucun bon sélectionné");
            alert.setHeaderText("Veuillez sélectionner un bon à exporter");
            alert.showAndWait();
            return;
        }
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sauvegarder le bon de livraison");
        fileChooser.setInitialFileName("BonLivraison_" + bonSelectionne.getNumero());
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf")
        );
        
        java.io.File fichier = fileChooser.showSaveDialog(btnExporter.getScene().getWindow());
        
        if (fichier != null) {
            PDFExporter.exporterBonLivraison(bonSelectionne, fichier.getAbsolutePath().replace(".pdf", ""));
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Export réussi");
            alert.setHeaderText("Bon de livraison exporté avec succès!");
            alert.setContentText("Fichier sauvegardé: " + fichier.getAbsolutePath());
            alert.showAndWait();
        }
    }
    
    // Fonction pour actualiser la liste des bons
    @FXML
    public void actualiserBons() {
        chargerBons();
        
        // Réinitialiser les filtres
        filtreClient.setValue(null);
        filtreStatut.setValue(null);
        recherchebon.clear();
        
        System.out.println("Liste des bons actualisée");
    }
    
    // Actions sur les bons individuels
    private void voirBon(BonLivraison bon) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails du bon");
        alert.setHeaderText("Bon N° " + bon.getNumero());
        alert.setContentText("Client: " + bon.getClientNom() + "\n" +
                           "Date: " + bon.getDateLivraison() + "\n" +
                           "Statut: " + bon.getStatut() + "\n" +
                           "Adresse: " + bon.getAdresseLivraison());
        alert.showAndWait();
    }
    
    private void modifierBon(BonLivraison bon) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Modifier le bon");
        alert.setHeaderText("Modification du bon N° " + bon.getNumero());
        alert.setContentText("Fonctionnalité à implémenter");
        alert.showAndWait();
    }
    
    private void supprimerBonIndividuel(BonLivraison bon) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmer la suppression");
        confirmation.setHeaderText("Supprimer le bon N° " + bon.getNumero() + " ?");
        confirmation.setContentText("Cette action est irréversible.");
        
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            BonService BonService = new BonService();
            BonService.supprimerBon(bon.getNumero());
            chargerBons();
        }
    }
    
    private void exporterBonIndividuel(BonLivraison bon) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter le bon de livraison");
        fileChooser.setInitialFileName("BonLivraison_" + bon.getNumero());
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf")
        );
        
        java.io.File fichier = fileChooser.showSaveDialog(tablebon.getScene().getWindow());
        
        if (fichier != null) {
            PDFExporter.exporterBonLivraison(bon, fichier.getAbsolutePath().replace(".pdf", ""));
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Export réussi");
            alert.setHeaderText("Bon exporté avec succès!");
            alert.showAndWait();
        }
    }

}