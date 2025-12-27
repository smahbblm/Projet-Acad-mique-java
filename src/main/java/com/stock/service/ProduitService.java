package com.stock.service;

import com.stock.dao.interfaces.IProduitDAO;
import com.stock.dao.implementation.ProduitDAO;
import com.stock.dao.interfaces.IMouvementStockDAO;
import com.stock.dao.implementation.MouvementStockDAO;
import com.stock.model.produit.Produit;

import com.stock.model.stock.MouvementStock;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class ProduitService {
    private IProduitDAO produitDAO;
    private IMouvementStockDAO mouvementStockDAO;

    public ProduitService() throws Exception {
        this.produitDAO = new ProduitDAO();
        this.mouvementStockDAO = new MouvementStockDAO();
    }


    public void ajouterProduit(Produit produit) throws Exception {
        produitDAO.create(produit);
        
        if (produit.getQuantiteStock() > 0) {
            MouvementStock mouvement = new MouvementStock("ENTREE", produit.getQuantiteStock(), "INIT-" + produit.getReference());
            mouvement.setProduit(produit);
            mouvement.setStockAvant(0);
            mouvement.setStockApres(produit.getQuantiteStock());
            mouvementStockDAO.create(mouvement);
        }
    }

    public void modifierProduit(Produit produit) throws Exception {
        Produit ancienProduit = produitDAO.read(produit.getIdProduit());
        int ancienStock = ancienProduit.getQuantiteStock();
        int nouveauStock = produit.getQuantiteStock();
        
        produitDAO.update(produit);
        
        if (ancienStock != nouveauStock) {
            int difference = nouveauStock - ancienStock;
            String typeMouvement = difference > 0 ? "ENTREE" : "SORTIE";
            
            MouvementStock mouvement = new MouvementStock(
                typeMouvement, 
                Math.abs(difference), 
                "AJUST-" + produit.getReference()
            );
            mouvement.setProduit(produit);
            mouvement.setStockAvant(ancienStock);
            mouvement.setStockApres(nouveauStock);
            mouvementStockDAO.create(mouvement);
        }
    }

    public void supprimerProduit(int idProduit) throws Exception {
        Produit produit = produitDAO.read(idProduit);
        
        if (produit.getQuantiteStock() > 0) {
            MouvementStock mouvement = new MouvementStock(
                "AJUSTEMENT", 
                produit.getQuantiteStock(), 
                "DEL-" + produit.getReference()
            );
            mouvement.setProduit(produit);
            mouvement.setStockAvant(produit.getQuantiteStock());
            mouvement.setStockApres(0);
            mouvementStockDAO.create(mouvement);
        }
        
        produitDAO.delete(idProduit);
    }


    public Produit consulterProduit(int idProduit) throws Exception {
        return produitDAO.read(idProduit);
    }

    public List<Produit> consulterTousProduits()  {
        try {
          System.out.println("je suis la fonction consulterTousProduits"+ produitDAO.readAll());
            return produitDAO.readAll();
        }catch(Exception e){
            return new ArrayList<>();
        }
    }


    public List<Produit> consulterProduitsStockBas() throws Exception {
        return produitDAO.findStockBas();
    }

    public List<Produit> consulterProduitsParCategorie(String categorie) throws Exception {
        return produitDAO.findByCategorie(categorie);
    }

    public Produit consulterProduitParReference(String reference) throws Exception {
        return produitDAO.getProduitById(reference);
    }

    public int getNombreProduit() throws Exception{
        return produitDAO.readAll().size();
    }
    public List<Produit> getProduitsStockCritique() throws Exception {
          List<Produit> list_produit=produitDAO.readAll();
          List<Produit> list_produitMin=new ArrayList<>();
          for(Produit P: list_produit) {
              if (P.getQuantiteStock() <= P.getSeuilMin()) {
                  list_produitMin.add(P);

              }

          }
          return list_produitMin;

    }
    public void mettreAJourStock(Produit p) throws Exception {
        //produitDAO.mettreAJourStock(p);
    }

    public Produit getProduitById(String idProduit) throws Exception {
        return produitDAO.getProduitById(idProduit);
    }





    public int compterProduits() throws Exception {
        List<Produit> produits = produitDAO.readAll();
        System.out.println("Nombre de produits trouvés: " + produits.size());
        return produits.size();
    }

    public int compterProduitsEnRupture() throws Exception {
        List<Produit> produits = produitDAO.readAll();
        return (int) produits.stream()
                .filter(p -> p.getQuantiteStock() < p.getSeuilMin())
                .count();
    }

    public double calculerValeurStock() throws Exception {
        List<Produit> produits = produitDAO.readAll();
        return produits.stream()
                .mapToDouble(p -> p.getQuantiteStock() * p.getPrixAchat())
                .sum();
    }


}

