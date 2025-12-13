package com.stock.model.document;

import com.stock.model.partenaire.Client;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe pour les factures
 */
public class Facture {
    private int idFacture;
    private String numero;
    private LocalDate dateFacture;
    private LocalDate dateEcheance;
    private float montantHT;
    private float montantTVA;
    private float montantTTC;
    private String statut;
    private Client client;
    private BonLivraison bonLivraison;

    public Facture() {
        // Constructeur par défaut pour instanciation dynamique
        this.dateFacture = LocalDate.now();
        this.statut = "BROUILLON";
        this.montantHT = 0;
        this.montantTVA = 0;
        this.montantTTC = 0;
    }

    public Facture(String numero, LocalDate dateEcheance, Client client) {
        this.numero = numero;
        this.dateFacture = LocalDate.now();
        this.dateEcheance = dateEcheance;
        this.client = client;
        this.statut = "BROUILLON";
        this.montantHT = 0;
        this.montantTVA = 0;
        this.montantTTC = 0;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public BonLivraison getBonLivraison() {
        return bonLivraison;
    }

    public void setBonLivraison(BonLivraison bonLivraison) {
        this.bonLivraison = bonLivraison;
    }

    // Méthodes métier
    public void generer() {
        this.statut = "GENEREE";
    }

    public void calculerMontants() {
        float tva = montantHT * 0.20f;  // TVA de 20%
        this.montantTVA = tva;
        this.montantTTC = montantHT + tva;
    }

    public void imprimer() {
        // Implémentation à venir
    }

    public void annuler() {
        this.statut = "ANNULEE";
    }

    /**
     * Méthode helper pour affichage du nom client dans les tableaux
     */
    public String getClientNom() {
        if (client != null) {
            return client.getNom() + " " + client.getPrenom();
        }
        return "";
    }
}
