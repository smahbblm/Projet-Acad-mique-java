package com.stock.model.produit;

import java.time.LocalDate;

/**
 * Classe pour les produits
 */
public class Produit {
    private int idProduit;
    private String reference;
    private String designation;
    private String description;
    private float prixAchat;
    private float prixVente;
    private int quantiteStock;
    private int seuilMin;
    private int seuilMax;
    private String categorie;
    private LocalDate dateAjout;
    public Produit(){

    }
    public Produit(String reference, String designation, float prixAchat, float prixVente,
                   int quantiteStock, int seuilMin, int seuilMax, String categorie) {
        this.reference = reference;
        this.designation = designation;
        this.prixAchat = prixAchat;
        this.prixVente = prixVente;
        this.quantiteStock = quantiteStock;
        this.seuilMin = seuilMin;
        this.seuilMax = seuilMax;
        this.categorie = categorie;
        this.dateAjout = LocalDate.now();
    }

    // Getters et Setters
    public int getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(int idProduit) {
        this.idProduit = idProduit;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrixAchat() {
        return prixAchat;
    }

    public void setPrixAchat(float prixAchat) {
        this.prixAchat = prixAchat;
    }

    public float getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(float prixVente) {
        this.prixVente = prixVente;
    }

    public int getQuantiteStock() {
        return quantiteStock;
    }

    public void setQuantiteStock(int quantiteStock) {
        this.quantiteStock = quantiteStock;
    }

    public int getSeuilMin() {
        return seuilMin;
    }

    public void setSeuilMin(int seuilMin) {
        this.seuilMin = seuilMin;
    }

    public int getSeuilMax() {
        return seuilMax;
    }

    public void setSeuilMax(int seuilMax) {
        this.seuilMax = seuilMax;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public LocalDate getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(LocalDate dateAjout) {
        this.dateAjout = dateAjout;
    }

    // Méthodes métier
    public boolean consulterDisponibilite() {
        return quantiteStock > 0;
    }

    public boolean verifierSeuil() {
        return quantiteStock < seuilMin;
    }

    public void mettreAJourQuantite(int quantite) {
        this.quantiteStock += quantite;
    }

    @Override
    public String toString() {
        return "Produit{" +
                "reference='" + reference + '\'' +
                ", designation='" + designation + '\'' +
                ", quantiteStock=" + quantiteStock +
                '}';
    }
}