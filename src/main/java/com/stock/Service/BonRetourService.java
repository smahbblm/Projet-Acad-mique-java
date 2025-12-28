package com.stock.service;

import com.stock.dao.implementation.BonRetourDAO;
import com.stock.model.document.BonRetour;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class BonRetourService {

    public boolean creerBonRetour(BonRetour bonRetour) {
        try {
            BonRetourDAO dao = new BonRetourDAO();
            bonRetour.creer();
            dao.create(bonRetour);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ObservableList<BonRetour> getAllBonsRetour() {
        try {
            BonRetourDAO dao = new BonRetourDAO();
            List<BonRetour> bons = dao.readAll();
            
            // Si la base est vide, ajouter des données statiques
            if (bons.isEmpty()) {
                System.out.println("Aucun bon de retour en base, ajout de données statiques...");
                bons = creerDonneesStatiques();
            }
            
            return FXCollections.observableArrayList(bons);
        } catch (Exception e) {
            e.printStackTrace();
            // En cas d'erreur, retourner des données statiques
            return FXCollections.observableArrayList(creerDonneesStatiques());
        }
    }
    
    private List<BonRetour> creerDonneesStatiques() {
        List<BonRetour> bons = new java.util.ArrayList<>();
        
        try {
            // Créer des clients fictifs
            com.stock.model.partenaire.Client client1 = new com.stock.model.partenaire.Client(
                "Dupont", "Jean", "Dupont SARL", "123 Rue A", "0601020304", "dupont@test.com"
            );
            client1.setIdClient(1);
            
            com.stock.model.partenaire.Client client2 = new com.stock.model.partenaire.Client(
                "Martin", "Sophie", "Martin SA", "456 Rue B", "0605060708", "martin@test.com"
            );
            client2.setIdClient(2);
            
            // Bon de retour 1
            BonRetour br1 = new BonRetour("BR001", "Produit défectueux", client1);
            br1.setIdBonRetour(1);
            br1.setDateRetour(java.time.LocalDate.now().minusDays(5));
            br1.setStatut("VALIDEE");
            bons.add(br1);
            
            // Bon de retour 2
            BonRetour br2 = new BonRetour("BR002", "Erreur de commande", client2);
            br2.setIdBonRetour(2);
            br2.setDateRetour(java.time.LocalDate.now().minusDays(3));
            br2.setStatut("EN_ATTENTE");
            bons.add(br2);
            
            // Bon de retour 3
            BonRetour br3 = new BonRetour("BR003", "Produit non conforme", client1);
            br3.setIdBonRetour(3);
            br3.setDateRetour(java.time.LocalDate.now().minusDays(2));
            br3.setStatut("TRANSMISE");
            bons.add(br3);
            
            // Bon de retour 4
            BonRetour br4 = new BonRetour("BR004", "Changement d'avis", client2);
            br4.setIdBonRetour(4);
            br4.setDateRetour(java.time.LocalDate.now().minusDays(1));
            br4.setStatut("BROUILLON");
            bons.add(br4);
            
            // Bon de retour 5
            BonRetour br5 = new BonRetour("BR005", "Produit endommagé", client1);
            br5.setIdBonRetour(5);
            br5.setDateRetour(java.time.LocalDate.now());
            br5.setStatut("VALIDEE");
            bons.add(br5);
            
            System.out.println("Données statiques créées: " + bons.size() + " bons de retour");
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la création des données statiques: " + e.getMessage());
            e.printStackTrace();
        }
        
        return bons;
    }

    public boolean validerBonRetour(int idBonRetour) {
        try {
            BonRetourDAO dao = new BonRetourDAO();
            BonRetour bonRetour = dao.read(idBonRetour);
            if (bonRetour != null) {
                bonRetour.valider();
                dao.update(bonRetour);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean transmettreBoRetour(int idBonRetour) {
        try {
            BonRetourDAO dao = new BonRetourDAO();
            BonRetour bonRetour = dao.read(idBonRetour);
            if (bonRetour != null) {
                bonRetour.transmettre();
                dao.update(bonRetour);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
