package com.stock.Controler;

import com.stock.model.document.BonCommande;
import com.stock.model.stock.MouvementStock;

import com.stock.model.produit.Produit;
import com.stock.model.stock.LigneInventaire;
import com.stock.service.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.MenuItem;
import javafx.scene.input.DataFormat;

import javafx.scene.control.Label;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;


public class TableuController {
    @FXML
    private Label dateLabel;
    @FXML
    private Label quantitProduits;
    @FXML
    private Label nbrMouvements;
    @FXML
    private Label EcartInventor;
    @FXML
    private Label Produit_stockMin;
    @FXML
    private Label validation;
    @FXML
    private LineChart<String, Number> mouvementChart;
    @FXML
    private Label  AlertCritique;
    @FXML
    private Label nbrEnAttente;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;
    @FXML
    private MenuItem menuSemaine;

    @FXML
    private MenuItem menuMois;

    @FXML
    private MenuItem menuAnnee;
    @FXML
    private Label nbrNotif;
    private ProduitService produitService;
     private StockService stockService;
     private InventaireService inventorService;
     private CommandeService commandeService;
     private AlertService alerteService;

     public TableuController(){
         try{
            produitService=new ProduitService();
            stockService=new StockService();
            inventorService=new InventaireService();
            commandeService=new CommandeService();
            alerteService=new AlertService();

         }catch(Exception e){
             e.printStackTrace();
         }
     }
    private void afficherGraphique(List<MouvementStock> mouvements) {
        mouvements.sort(Comparator.comparing(MouvementStock::getDateMouvement));
        mouvementChart.getData().clear();

        // Obtenir l'axe X et définir les catégories dans l'ordre
        CategoryAxis xAxis = (CategoryAxis) mouvementChart.getXAxis();

        // Extraire toutes les dates uniques dans l'ordre
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
        List<String> datesOrdonnees = mouvements.stream()
                .map(m -> m.getDateMouvement().format(formatter))
                .distinct()
                .collect(Collectors.toList());

        xAxis.setCategories(FXCollections.observableArrayList(datesOrdonnees));

        XYChart.Series<String, Number> serieEntrees = new XYChart.Series<>();
        serieEntrees.setName("Entrées");

        XYChart.Series<String, Number> serieSorties = new XYChart.Series<>();
        serieSorties.setName("Sorties");

        // Initialiser toutes les dates avec 0
        for (String date : datesOrdonnees) {
            serieEntrees.getData().add(new XYChart.Data<>(date, 0));
            serieSorties.getData().add(new XYChart.Data<>(date, 0));
        }

        // Remplir avec les vraies valeurs
        for (MouvementStock m : mouvements) {
            String date = m.getDateMouvement().format(formatter);

            if (m.getTypeMouvement().equalsIgnoreCase("ENTREE")) {
                // Trouver le point correspondant et mettre à jour
                for (XYChart.Data<String, Number> data : serieEntrees.getData()) {
                    if (data.getXValue().equals(date)) {
                        data.setYValue(data.getYValue().intValue() + m.getQuantite());
                        break;
                    }
                }
            } else if (m.getTypeMouvement().equalsIgnoreCase("SORTIE")) {
                for (XYChart.Data<String, Number> data : serieSorties.getData()) {
                    if (data.getXValue().equals(date)) {
                        data.setYValue(data.getYValue().intValue() + m.getQuantite());
                        break;
                    }
                }
            }
        }

        mouvementChart.getData().add(serieEntrees);
        mouvementChart.getData().add(serieSorties);
    }






    @FXML
    public void initialize()  {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
        String formattedDate = LocalDate.now().format(formatter);
        dateLabel.setText(formattedDate);
        try{
            nbrEnAttente.setText(String.valueOf(alerteService.getNombreCommandeENAttente_Validation()));
            AlertCritique.setText(String.valueOf(alerteService.getNombreAlertsCritique()));
            nbrNotif.setText(String.valueOf(alerteService.getNombreTotalAlertes()));
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            quantitProduits.setText(String.valueOf(produitService.getNombreProduit()));
            nbrMouvements.setText(String.valueOf(stockService.getNombre_mouvement()));
            // Code corrigé pour afficher uniquement les écarts critiques
            Supplier<String> messageEcart = () -> {
                try {
                    List<LigneInventaire> list_lignes = inventorService.getEcartsInventaire();
                    List<String> referencesCritiques = new ArrayList<>();

                    for (LigneInventaire ligne : list_lignes) {
                        String reference = ligne.getProduit().getReference();
                        int ecart = ligne.getEcart();

                        if (ecart < 0) {
                            referencesCritiques.add(reference);
                        }
                    }

                    if (referencesCritiques.isEmpty()) {
                        return "Aucun écart critique détecté";
                    }

                    return "Différence détectée - Produits: " +
                            String.join(", ", referencesCritiques);


                } catch (Exception e) {
                    return "Erreur lors du chargement des écarts";
                }
            };
            afficherGraphique(stockService.getMouvementsMois());
            menuSemaine.setOnAction(e -> {
                try {
                    afficherGraphique(stockService.getMouvementsSemaine());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            menuMois.setOnAction(e -> {
                try {
                    afficherGraphique(stockService.getMouvementsMois());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            menuAnnee.setOnAction(e -> {
                try {
                    afficherGraphique(stockService.getMouvementsAnnee());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

// 🟦 Maintenant on met le résultat de la lambda dans setText()
            EcartInventor.setText(messageEcart.get());




            Supplier<String> messageStockMin = () -> {
                try {
                    List<Produit>  list_produites = produitService.getProduitsStockCritique();
                    List<String> referencesCritiques = new ArrayList<>();

                    for (Produit P: list_produites) {
                        String reference = P.getReference();

                            referencesCritiques.add(reference);

                    }

                    if (referencesCritiques.isEmpty()) {
                        return "Aucun écart critique détecté";
                    }

                    return " Produits: " +
                            String.join(", ", referencesCritiques);

                } catch (Exception e) {
                    return "Aucun produit sous le seuil";
                }
            };

// 🟦 Maintenant on met le résultat de la lambda dans setText()
            Produit_stockMin.setText( messageStockMin.get());
            Supplier<String> messageAttenteValidation = () -> {
                try {
                    int nbr = commandeService.getBonsEntreeEnAttenteValidation().size();

                    if (nbr == 0) {
                        return "Aucun bon d'entrée en attente de validation";
                    }

                    return nbr + " bon" + (nbr > 1 ? "s" : "") + " d'entrée en attente de validation";

                } catch (Exception e) {
                    return "Erreur lors du chargement des validations";
                }
            };

// Affichage
            validation.setText(messageAttenteValidation.get());

        }catch(Exception e){
            e.printStackTrace();
        }



    }


}
