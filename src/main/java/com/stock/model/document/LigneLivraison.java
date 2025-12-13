package com.stock.model.document;

import com.stock.model.produit.Produit;

/**
 * Classe pour les lignes de livraison
 */
public class LigneLivraison {
    private int idLigne;
    private BonLivraison bonLivraison;
    private Produit produit;
    private int quantite;
    private float prixUnitaire;

    public LigneLivraison(Produit produit, int quantite, float prixUnitaire) {
        this.produit = produit;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }

    public LigneLivraison() {
        // Constructeur par défaut
    }

    // Getters et Setters
    public int getIdLigne() {
        return idLigne;
    }

    public void setIdLigne(int idLigne) {
        this.idLigne = idLigne;
    }

    public BonLivraison getBonLivraison() {
        return bonLivraison;
    }

    public void setBonLivraison(BonLivraison bonLivraison) {
        this.bonLivraison = bonLivraison;
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

    public float getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(float prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    // Méthodes métier
    public void ajouterLigne() {
        // Implémentation à venir
    }

    public void modifierLigne() {
        // Implémentation à venir
    }

    public void supprimerLigne() {
        // Implémentation à venir
    }

    public float getSousTotal() {
        return prixUnitaire * quantite;
    }
}
