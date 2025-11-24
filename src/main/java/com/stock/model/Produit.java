package com.stock.model;

import java.util.Date;

public class Produit {
    private int idProduit;
    private String reference;
    private  String type;
    private String description;
    private  float prixAchat;
    private  int quantite;
    private Date dateAjout;
    public Produit(String reference,String description,int quantite,Date dateAjout){
        this.reference=reference;
        this.type=type;
        this.description=description;
        this.quantite=quantite;
        this.dateAjout=dateAjout;
    }
    public int getIdProduit() {
        return idProduit;
    }

    public String getReference() {
        return reference;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public float getPrixAchat() {
        return prixAchat;
    }

    public int getQuantite() {
        return quantite;
    }

    public Date getDateAjout() {
        return dateAjout;
    }
    public void getAllproduit(){

    }
}
