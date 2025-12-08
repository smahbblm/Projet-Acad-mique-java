package com.stock.service;

import com.stock.dao.interfaces.IProduitDAO;
import com.stock.dao.implementation.ProduitDAO;
import com.stock.model.produit.Produit;
import java.util.ArrayList;
import java.util.List;

/**
 * Service pour la gestion des produits
 */
public class ProduitService {
    private IProduitDAO produitDAO;

    public ProduitService() throws Exception {
        this.produitDAO = new ProduitDAO();
    }

    public void ajouterProduit(Produit produit) throws Exception {
        produitDAO.create(produit);
    }

    public void modifierProduit(Produit produit) throws Exception {
        produitDAO.update(produit);
    }

    public void supprimerProduit(int idProduit) throws Exception {
        produitDAO.delete(idProduit);
    }

    public Produit consulterProduit(int idProduit) throws Exception {
        return produitDAO.read(idProduit);
    }

    public List<Produit> consulterTousProduits() throws Exception {
        return produitDAO.readAll();
    }

    public List<Produit> consulterProduitsStockBas() throws Exception {
        return produitDAO.findStockBas();
    }

    public List<Produit> consulterProduitsParCategorie(String categorie) throws Exception {
        return produitDAO.findByCategorie(categorie);
    }

    public Produit consulterProduitParReference(String reference) throws Exception {
        return produitDAO.findByReference(reference);
    }
}

