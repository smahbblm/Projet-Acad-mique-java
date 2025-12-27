package com.stock.model.document;

import com.stock.model.partenaire.Fournisseur;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe pour les bons de commande
 */
public class BonCommande {
    private int idBonCommande;
    private String numero;
    private LocalDate dateCommande;
    private LocalDate dateLivraisonPrevue;
    private String statut;
    private float montantTotal;
    private String observations;
    private Fournisseur fournisseur;
    private List<LigneCommande> lignes;

    public BonCommande(String numero, LocalDate dateLivraisonPrevue, Fournisseur fournisseur) {
        this.numero = numero;
        this.dateCommande = LocalDate.now();
        this.dateLivraisonPrevue = dateLivraisonPrevue;
        this.statut = "BROUILLON";
        this.montantTotal = 0;
        this.fournisseur = fournisseur;
        this.lignes = new ArrayList<>();
    }

    public BonCommande(){
        this.dateCommande = LocalDate.now();
        this.statut = "BROUILLON";
        this.lignes = new ArrayList<>();
    }

    // Getters et Setters
    public int getIdBonCommande() {
        return idBonCommande;
    }

    public void setIdBonCommande(int idBonCommande) {
        this.idBonCommande = idBonCommande;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(LocalDate dateCommande) {
        this.dateCommande = dateCommande;
    }

    public LocalDate getDateLivraisonPrevue() {
        return dateLivraisonPrevue;
    }

    public void setDateLivraisonPrevue(LocalDate dateLivraisonPrevue) {
        this.dateLivraisonPrevue = dateLivraisonPrevue;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public float getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(float montantTotal) {
        this.montantTotal = montantTotal;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public List<LigneCommande> getLignes() {
        return lignes;
    }

    public void setLignes(List<LigneCommande> lignes) {
        this.lignes = lignes;
    }

    // Méthodes métier
    public void creer() {
        this.statut = "EN_ATTENTE";
    }

    public void modifier() {
        // Implémentation à venir
    }

    public void annuler() {
        this.statut = "ANNULEE";
    }

    public void valider() {
        this.statut = "VALIDEE";
    }

    public void transmettre() {
        this.statut = "TRANSMISE";
    }

    public float calculerMontant() {
        montantTotal = 0;
        for (LigneCommande ligne : lignes) {
            montantTotal += ligne.getSousTotal();
        }
        return montantTotal;
    }
    public String getNomFournisseur() {
        return fournisseur != null ? fournisseur.getRaisonSociale() : "";
    }
    public int getNbProduits() {
        return lignes != null ? lignes.size() : 0;
    }

}
