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

