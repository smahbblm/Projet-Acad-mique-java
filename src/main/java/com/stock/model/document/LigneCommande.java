package com.stock.model.document;

import com.stock.model.produit.Produit;

/**
 * Classe pour les lignes de commande
 */
public class LigneCommande {
    private int idLigne;
    private BonCommande bonCommande;
    private Produit produit;
    private int quantite;
    private float prixUnitaire;
    private float sousTotal;

    public LigneCommande(Produit produit, int quantite, float prixUnitaire) {
        this.produit = produit;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.sousTotal = quantite * prixUnitaire;
    }

    // Getters et Setters
    public int getIdLigne() {
        return idLigne;
    }

    public void setIdLigne(int idLigne) {
        this.idLigne = idLigne;
    }

    public BonCommande getBonCommande() {
        return bonCommande;
    }

    public void setBonCommande(BonCommande bonCommande) {
        this.bonCommande = bonCommande;
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
        calculerSousTotal();
    }

    public float getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(float prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
        calculerSousTotal();
    }

    public float getSousTotal() {
        return sousTotal;
    }

    public void setSousTotal(float sousTotal) {
        this.sousTotal = sousTotal;
    }

    // Méthodes métier
    public void ajouterLigne() {
        // Implémentation à venir
    }

    public void modifierLigne() {
        calculerSousTotal();
    }

    public void supprimerLigne() {
        // Implémentation à venir
    }

    public float calculerSousTotal() {
        this.sousTotal = quantite * prixUnitaire;
        return sousTotal;
    }
}

