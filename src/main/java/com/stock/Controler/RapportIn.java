package com.stock.Controler;
import com.stock.model.systeme.Rapport;
import com.stock.model.stock.Inventaire;
import com.stock.model.stock.LigneInventaire;
import com.stock.service.InventaireService;
import com.stock.service.LigneInventaireService;
import com.stock.service.RapportService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.embed.swing.SwingFXUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;

import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.List;

public class RapportIn {
    @FXML
    private TableView<LigneInventaire> TableProduit;
    @FXML
    private TableColumn<LigneInventaire, String> colProduit;

    @FXML
    private TableColumn<LigneInventaire, Integer> colQtéSys;
    @FXML
    private TableColumn<LigneInventaire, Integer> colQtéRel;
    @FXML
    private TableColumn<LigneInventaire, Integer> colEcart;
    @FXML
    private TextField RechecheProd;
    @FXML
    private VBox affichageSection;
    @FXML
    private TextField typeField;
    @FXML
    private DatePicker dateAPicker;
    @FXML
    private DatePicker dateDePicker;
    @FXML
    private ComboBox<Inventaire>  inventaireCombo;

    @FXML
    private VBox formulaireSection;
    @FXML
    private Button btnRetour;
    @FXML
    private Label type;
    @FXML
    private  Label dateGe;
    @FXML
    private Label periode;
    @FXML
    private Label conformesLabel;
    @FXML
    private Label  manquantsLabel;
    @FXML
    private Label excedentsLabel;
    @FXML
     private TextArea description;
    @FXML
    private VBox rapportContenu;
    private InventaireService InventorService;
    private LigneInventaireService ligneService;
    private ObservableList<LigneInventaire> lignesInventaire = FXCollections.observableArrayList();
    private RapportService rapportService;
    private Rapport rapport;
    public RapportIn() throws Exception {
        this.InventorService=new InventaireService();
        this.ligneService=new LigneInventaireService();
        this.rapportService=new RapportService();
        this.rapport=new Rapport();
    }

    public void initialize() {
        // Important: garder managed à true pour que le VBox prenne de l'espace
        affichageSection.setVisible(false);
        affichageSection.setManaged(false);


        TableProduit.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Configuration des colonnes - utiliser les bonnes propriétés
        colProduit.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getProduit() != null
                                ? cellData.getValue().getProduit().getDescription()
                                : ""
                )
        );
        colQtéSys.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getQuantiteTheorique()).asObject()
        );
        colQtéRel.setCellValueFactory(new PropertyValueFactory<>("quantiteReelle"));
        colEcart.setCellValueFactory(new PropertyValueFactory<>("ecart"));









        // Configurer le bouton retour
        if (btnRetour != null) {
            btnRetour.setOnAction(this::retourFormulaire);
        }
        try{
            List<Inventaire> inventaires =InventorService.consulterInventairesParStatut("Terminé");
            inventaireCombo.setItems(FXCollections.observableArrayList(inventaires));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        // récupère tous les inventaires



        inventaireCombo.setCellFactory(lv -> new ListCell<Inventaire>() {
            @Override
            protected void updateItem(Inventaire item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("Inventaire du " + item.getDateInventaire() + " - " + item.getStatut());
                }
            }
        });
    }

    private void calculerStatistiques(List<LigneInventaire> lignes) {

        int conformes = 0;
        int manquants = 0;
        int excedents = 0;

        for (LigneInventaire ligne : lignes) {
            int ecart = ligne.getEcart(); // Assure-toi que cette méthode existe !

            if (ecart == 0) {
                conformes++;
            } else if (ecart < 0) {
                manquants++;
            } else {
                excedents++;
            }
        }

        conformesLabel.setText(String.valueOf(conformes));
        manquantsLabel.setText(String.valueOf(manquants));
        excedentsLabel.setText(String.valueOf(excedents));
    }

    @FXML
    public void GenererRapport(ActionEvent event) {
        // Validation des champs
        if (typeField.getText().isEmpty()) {
            showAlert("Erreur", "Veuillez saisir le type de rapport");
            return;
        }
        if (dateDePicker.getValue() == null || dateAPicker.getValue() == null) {
            showAlert("Erreur", "Veuillez sélectionner les dates");
            return;
        }
        if (dateDePicker.getValue().isAfter(dateAPicker.getValue())) {
            showAlert("Erreur", "La date de début doit être avant la date de fin");
            return;
        }
        String Type=typeField.getText();
        LocalDate date_debut=dateAPicker.getValue();
        LocalDate date_fin= dateDePicker.getValue();
        LocalDate dateGeneration=LocalDate.now();

        type.setText("Type :"+Type);
        dateGe.setText("Date de génération:"+dateGeneration);
        periode.setText(" période:Du " + date_debut+" AU "+ date_fin);

        // Charger les données (à adapter selon votre logique métier)
        chargerDonneesInventaire();

        // Masquer le formulaire et afficher le rapport
        formulaireSection.setVisible(false);
        formulaireSection.setManaged(false);

        affichageSection.setVisible(true);
        affichageSection.setManaged(true);

        try {
            // 1️⃣ Récupérer l'id de l'inventaire sélectionné dans le ComboBox
            Inventaire selectedInventaire = inventaireCombo.getSelectionModel().getSelectedItem();
            if (selectedInventaire == null) {
                System.out.println("Aucun inventaire sélectionné !");
                return;
            }
            int idInventaire = selectedInventaire.getIdInventaire();
            String observation=selectedInventaire.getObservations();
            description.setText(observation);
            // 2️⃣ Récupérer les lignes d'inventaire depuis la base de données
            List<LigneInventaire> lignes = ligneService.findLigne(idInventaire);
            calculerStatistiques(lignes);
            // 3️⃣ Convertir la liste en ObservableList
            ObservableList<LigneInventaire> data = FXCollections.observableArrayList(lignes);

            // 4️⃣ Ajouter les données dans le TableView
            TableProduit.setItems(data);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @FXML
    public void retourFormulaire(ActionEvent event) {
        // Retour au formulaire
        affichageSection.setVisible(false);
        affichageSection.setManaged(false);

        formulaireSection.setVisible(true);
        formulaireSection.setManaged(true);

        // Réinitialiser le formulaire si nécessaire
        // typeField.clear();
        // dateDePicker.setValue(null);
        // dateAPicker.setValue(null);
    }

    /**
     * Méthode pour charger les données d'inventaire
     * À adapter selon votre logique métier
     */
    private void chargerDonneesInventaire() {
        lignesInventaire.clear();

        // Exemple de données fictives - À REMPLACER par vos vraies données
        // Vous devriez charger depuis votre base de données ou service

        // Exemple:
        // lignesInventaire.addAll(inventaireService.getLignesInventaire(
        //     dateDePicker.getValue(),
        //     dateAPicker.getValue()
        // ));

        // Pour tester, ajoutez quelques lignes factices:
        /*
        Produit p1 = new Produit("Riz Basmati", "REF001", 100);
        LigneInventaire li1 = new LigneInventaire(p1);
        li1.setQuantiteReelle(95);
        li1.calculerEcart();
        lignesInventaire.add(li1);

        Produit p2 = new Produit("Farine", "REF002", 50);
        LigneInventaire li2 = new LigneInventaire(p2);
        li2.setQuantiteReelle(52);
        li2.calculerEcart();
        lignesInventaire.add(li2);
        */
    }


    @FXML
    private void genererPdfRapport(ActionEvent event) {
        try {
            // 1️⃣ Capturer le VBox
            WritableImage snapshot = rapportContenu.snapshot(null, null);
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(snapshot, null);

            // 2️⃣ Créer un document PDF
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            // 3️⃣ Ajouter l'image du VBox dans le PDF
            var pdImage = LosslessFactory.createFromImage(document, bufferedImage);
            try (var contentStream = new org.apache.pdfbox.pdmodel.PDPageContentStream(document, page)) {
                contentStream.drawImage(pdImage, 0, 0, page.getMediaBox().getWidth(), page.getMediaBox().getHeight());
            }

            // 4️⃣ Sauvegarder le PDF
            document.save("Rapport_Inventaire.pdf");
            document.close();

            System.out.println("PDF généré avec succès !");
            String typeI=type.getText();
            LocalDate date_debut=dateAPicker.getValue();
            LocalDate date_fin=dateDePicker.getValue();
            LocalDate date_generation=LocalDate.now();
            String Contenu= "Inventaire avec des statistique sur les produites";
            rapport.setType(typeI);
            rapport.setDateGeneration(date_generation);
            rapport.setDateA(date_debut);
            rapport.setDateDe(date_fin);
            rapport.setContenu(Contenu);
            rapportService.enregistrerRapportManuel(rapport);
            System.out.println("rapport a été enregistrer avec succés");



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}