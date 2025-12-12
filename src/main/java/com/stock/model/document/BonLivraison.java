package com.stock.model.document;

import com.stock.model.partenaire.Client;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe pour les bons de livraison
 */
public class BonLivraison {
    private int idBonLivraison;
    private String numero;
    private LocalDate dateLivraison;
    private String statut;
    private String adresseLivraison;
    private String observations;
    private Client client;
    private List<LigneLivraison> lignes;
    private BonSortie bonSortie;
    private Facture facture;

    public BonLivraison() {
        // Constructeur par défaut pour instanciation dynamique
        this.lignes = new ArrayList<>();
        this.statut = "BROUILLON";
    }

    public BonLivraison(String numero, LocalDate dateLivraison, Client client) {
        this.numero = numero;
        this.dateLivraison = dateLivraison;
        this.client = client;
        this.statut = "BROUILLON";
        this.lignes = new ArrayList<>();
    }

    // Getters et Setters
    public int getIdBonLivraison() {
        return idBonLivraison;
    }

    public void setIdBonLivraison(int idBonLivraison) {
        this.idBonLivraison = idBonLivraison;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(LocalDate dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getAdresseLivraison() {
        return adresseLivraison;
    }

    public void setAdresseLivraison(String adresseLivraison) {
        this.adresseLivraison = adresseLivraison;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<LigneLivraison> getLignes() {
        return lignes;
    }

    public void setLignes(List<LigneLivraison> lignes) {
        this.lignes = lignes;
    }

    public BonSortie getBonSortie() {
        return bonSortie;
    }

    public void setBonSortie(BonSortie bonSortie) {
        this.bonSortie = bonSortie;
    }

    public Facture getFacture() {
        return facture;
    }

    public void setFacture(Facture facture) {
        this.facture = facture;
    }

    // Méthodes métier
    public void creer() {
        this.statut = "EN_ATTENTE";
    }

    public void modifier() {
        // Implémentation à venir
    }

    public void valider() {
        this.statut = "VALIDEE";
    }

    public void transmettre() {
        this.statut = "TRANSMISE";
    }

    /**
     * Méthode helper pour affichage du nom client dans les tableaux
     */
    public String getClientNom() {
        if (client != null && client.getNom() != null) {
            return client.getNom();
        }
        return "";
    }
}
