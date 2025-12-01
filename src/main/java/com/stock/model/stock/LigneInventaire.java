package com.stock.model.stock;

import com.stock.model.produit.Produit;

/**
 * Classe pour les lignes d'inventaire
 */
public class LigneInventaire {
    private int idLigne;
    private Inventaire inventaire;
    private Produit produit;
    private int quantiteTheorique;
    private int quantiteReelle;
    private int ecart;

    public LigneInventaire(Produit produit, int quantiteTheorique) {
        this.produit = produit;
        this.quantiteTheorique = quantiteTheorique;
    }

    // Getters et Setters
    public int getIdLigne() {
        return idLigne;
    }

    public void setIdLigne(int idLigne) {
        this.idLigne = idLigne;
    }

    public Inventaire getInventaire() {
        return inventaire;
    }

    public void setInventaire(Inventaire inventaire) {
        this.inventaire = inventaire;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public int getQuantiteTheorique() {
        return quantiteTheorique;
    }

    public void setQuantiteTheorique(int quantiteTheorique) {
        this.quantiteTheorique = quantiteTheorique;
    }

    public int getQuantiteReelle() {
        return quantiteReelle;
    }

    public void setQuantiteReelle(int quantiteReelle) {
        this.quantiteReelle = quantiteReelle;
        calculerEcart();
    }

    public int getEcart() {
        return ecart;
    }

    public void setEcart(int ecart) {
        this.ecart = ecart;
    }

    // Méthodes métier
    public void enregistrer() {
        // Implémentation à venir
    }

    public int calculerEcart() {
        this.ecart = quantiteReelle - quantiteTheorique;
        return ecart;
    }
}

