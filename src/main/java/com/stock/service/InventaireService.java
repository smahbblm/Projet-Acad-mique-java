package com.stock.service;

import com.stock.dao.implementation.LigneInventaireDAO;
import com.stock.dao.implementation.ProduitDAO;
import com.stock.dao.interfaces.IInventaireDAO;
import com.stock.dao.implementation.InventaireDAO;
import com.stock.model.produit.Produit;
import com.stock.model.stock.Inventaire;
import com.stock.model.stock.LigneInventaire;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service pour la gestion des inventaires
 */
public class InventaireService {
    private IInventaireDAO inventaireDAO;
    private LigneInventaireDAO  ligneInventaireDAO;
    private ProduitDAO productDao;
    public ObservableList<LigneInventaire> generateLigneInventaire() throws Exception{
        List<Produit> list_produites=productDao.readAll();
        ObservableList<LigneInventaire> lignes= FXCollections.observableArrayList();
        for(Produit P: list_produites){
               LigneInventaire li=new LigneInventaire();
               li.setProduit(P);
               li.setQuantiteTheorique(P.getQuantiteStock());
               li.setQuantiteReelle(0);
               li.setEcart(0);
            lignes.add(li);
        }
        return lignes;

    }
    public InventaireService() throws Exception {
        this.inventaireDAO = new InventaireDAO();
        this.ligneInventaireDAO=new LigneInventaireDAO();
        this.productDao=new ProduitDAO();
    }

    public void creerInventaire(Inventaire inventaire) throws Exception {
        inventaireDAO.create(inventaire);
    }

    public void modifierInventaire(Inventaire inventaire) throws Exception {
        inventaireDAO.update(inventaire);
    }

    public void supprimerInventaire(int idInventaire) throws Exception {
        inventaireDAO.delete(idInventaire);
    }

    public Inventaire consulterInventaire(int idInventaire) throws Exception {
        return inventaireDAO.read(idInventaire);
    }

    public List<Inventaire> consulterTousLesInventaires() throws Exception {
        return inventaireDAO.readAll();
    }

    public List<Inventaire> consulterInventairesParStatut(String statut) throws Exception {
        return inventaireDAO.findByStatut(statut);
    }
    public List<LigneInventaire> getEcartsInventaire() throws Exception {
        List<LigneInventaire> list_inventaires=ligneInventaireDAO.readAll();
        List<LigneInventaire> inventaires=new ArrayList<>();
            for(LigneInventaire ligne:list_inventaires) {
                if (ligne.getQuantiteTheorique() != ligne.getQuantiteReelle()) {
                    inventaires.add(ligne);

                }
            }

             return inventaires;
    }

    public void  ChangerStatut(int idInventaire,String statut) throws Exception{
          inventaireDAO.ChangerStatus(idInventaire,statut);
    }
}

