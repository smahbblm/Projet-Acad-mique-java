package com.stock.model;

import java.time.LocalDate;


public class Facture {
    
    // Attributs correspondant aux colonnes de la table SQL
    private int idFacture;
    private String numero;
    private LocalDate dateFacture;
    private LocalDate dateEcheance;
    private float montantHT;
    private float montantTVA;
    private float montantTTC;
    private String statut;
    private int idClient;
    
    // Constructeur par default
    public Facture() {
    }
    
    // Constructeur avec tous les paramètres
    public Facture(int idFacture, String numero, LocalDate dateFacture, LocalDate dateEcheance, 
                   float montantHT, float montantTVA, float montantTTC, String statut, int idClient) {
        this.idFacture = idFacture;
        this.numero = numero;
        this.dateFacture = dateFacture;
        this.dateEcheance = dateEcheance;
        this.montantHT = montantHT;
        this.montantTVA = montantTVA;
        this.montantTTC = montantTTC;
        this.statut = statut;
        this.idClient = idClient;
    }
    
    // Getters et Setters
    public int getIdFacture() {
        return idFacture;
    }
    
    public void setIdFacture(int idFacture) {
        this.idFacture = idFacture;
    }
    
    public String getNumero() {
        return numero;
    }
    
    public void setNumero(String numero) {
        this.numero = numero;
    }
    
    public LocalDate getDateFacture() {
        return dateFacture;
    }
    
    public void setDateFacture(LocalDate dateFacture) {
        this.dateFacture = dateFacture;
    }
    
    public LocalDate getDateEcheance() {
        return dateEcheance;
    }
    
    public void setDateEcheance(LocalDate dateEcheance) {
        this.dateEcheance = dateEcheance;
    }
    
    public float getMontantHT() {
        return montantHT;
    }
    
    public void setMontantHT(float montantHT) {
        this.montantHT = montantHT;
    }
    
    public float getMontantTVA() {
        return montantTVA;
    }
    
    public void setMontantTVA(float montantTVA) {
        this.montantTVA = montantTVA;
    }
    
    public float getMontantTTC() {
        return montantTTC;
    }
    
    public void setMontantTTC(float montantTTC) {
        this.montantTTC = montantTTC;
    }
    
    public String getStatut() {
        return statut;
    }
    
    public void setStatut(String statut) {
        this.statut = statut;
    }
    
    public int getIdClient() {
        return idClient;
    }
    
    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }
}
