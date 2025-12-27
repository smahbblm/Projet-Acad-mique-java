package com.stock.model.document;

import com.stock.model.produit.Produit;

/**
 * Classe pour les lignes de sortie
 */
public class LigneSortie {
    private int idLigne;
    private BonSortie bonSortie;
    private Produit produit;
    private int quantite;
    public LigneSortie(){

    }

    public LigneSortie(Produit produit, int quantite) {
        this.produit = produit;
        this.quantite = quantite;
    }

    // Getters et Setters
    public int getIdLigne() {
        return idLigne;
    }

    public void setIdLigne(int idLigne) {
        this.idLigne = idLigne;
    }

    public BonSortie getBonSortie() {
        return bonSortie;
    }

    public void setBonSortie(BonSortie bonSortie) {
        this.bonSortie = bonSortie;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    // Méthodes métier
    public void ajouterLigne() {
        // Implémentation à venir
    }

    public void enregistrer() {
        // Implémentation à venir
    }
}

