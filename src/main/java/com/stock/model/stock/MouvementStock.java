package com.stock.model.stock;

import com.stock.model.produit.Produit;
import com.stock.model.utilisateur.Utilisateur;
import java.time.LocalDate;

/**
 * Classe pour les mouvements de stock
 */
public class MouvementStock {
    private int idMouvement;
    private LocalDate dateMouvement;
    private String typeMouvement;
    private int quantite;
    private int stockAvant;
    private int stockApres;
    private String reference;
    private Produit produit;
    private Utilisateur utilisateur;
    public MouvementStock(){

    }
    public MouvementStock(String typeMouvement, int quantite, String reference) {
        this.dateMouvement = LocalDate.now();
        this.typeMouvement = typeMouvement;
        this.quantite = quantite;
        this.reference = reference;
    }

    // Getters et Setters
    public int getIdMouvement() {
        return idMouvement;
    }

    public void setIdMouvement(int idMouvement) {
        this.idMouvement = idMouvement;
    }

    public LocalDate getDateMouvement() {
        return dateMouvement;
    }

    public void setDateMouvement(LocalDate dateMouvement) {
        this.dateMouvement = dateMouvement;
    }

    public String getTypeMouvement() {
        return typeMouvement;
    }

    public void setTypeMouvement(String typeMouvement) {
        this.typeMouvement = typeMouvement;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public int getStockAvant() {
        return stockAvant;
    }

    public void setStockAvant(int stockAvant) {
        this.stockAvant = stockAvant;
    }

    public int getStockApres() {
        return stockApres;
    }

    public void setStockApres(int stockApres) {
        this.stockApres = stockApres;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    // Méthodes métier
    public void enregistrer() {
        // Implémentation à venir
    }

    public MouvementStock consulter() {
        // Implémentation à venir
        return this;
    }
}

