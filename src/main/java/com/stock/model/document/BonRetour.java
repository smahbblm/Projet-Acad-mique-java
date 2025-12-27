package com.stock.model.document;

import com.stock.model.produit.Produit;
import com.stock.model.partenaire.Client;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class BonRetour {
    private int idBonRetour;
    private String numero;
    private LocalDate dateRetour;
    private String motif;
    private String statut;
    private Client client;
    private Map<Produit, Integer> lignes;

    public BonRetour() {
        this.lignes = new HashMap<>();
        this.statut = "BROUILLON";
        this.dateRetour = LocalDate.now();
    }

    public BonRetour(String numero, String motif, Client client) {
        this();
        this.numero = numero;
        this.motif = motif;
        this.client = client;
    }

    public void creer() {
        this.statut = "CREE";
    }

    public void valider() {
        this.statut = "VALIDEE";
    }

    public void transmettre() {
        this.statut = "TRANSMISE";
    }

    public void ajouterProduit(Produit produit, int quantite) {
        this.lignes.put(produit, quantite);
    }

    // Getters et Setters
    public int getIdBonRetour() { return idBonRetour; }
    public void setIdBonRetour(int idBonRetour) { this.idBonRetour = idBonRetour; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public LocalDate getDateRetour() { return dateRetour; }
    public void setDateRetour(LocalDate dateRetour) { this.dateRetour = dateRetour; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public Map<Produit, Integer> getLignes() { return lignes; }
    public void setLignes(Map<Produit, Integer> lignes) { this.lignes = lignes; }
    
    public String getClientNom() {
        if (client != null) {
            return client.getNom() + " " + client.getPrenom();
        }
        return "";
    }
}
