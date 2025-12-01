package com.stock.model.utilisateur;

import com.stock.model.document.BonCommande;
import com.stock.model.partenaire.Fournisseur;
import com.stock.model.produit.Produit;
import com.stock.model.systeme.Rapport;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe pour les responsables d'approvisionnement
 */
public class ResponsableApprovisionnement extends Utilisateur {

    public ResponsableApprovisionnement(String nom, String prenom, String email, String motDePasse) {
        super(nom, prenom, email, motDePasse, "RESPONSABLE_APPROVISIONNEMENT");
    }

    public void gererProduits() {
        // Implémentation à venir
    }

    public Produit ajouterProduit(Produit produit) {
        // Implémentation à venir
        return null;
    }

    public void modifierProduit(Produit produit) {
        // Implémentation à venir
    }

    public void supprimerProduit(int idProduit) {
        // Implémentation à venir
    }

    public void gererFournisseurs() {
        // Implémentation à venir
    }

    public Fournisseur ajouterFournisseur(Fournisseur fournisseur) {
        // Implémentation à venir
        return null;
    }

    public void modifierFournisseur(Fournisseur fournisseur) {
        // Implémentation à venir
    }

    public void supprimerFournisseur(int idFournisseur) {
        // Implémentation à venir
    }

    public BonCommande creerBonCommande() {
        // Implémentation à venir
        return null;
    }

    public void modifierBonCommande(BonCommande bonCommande) {
        // Implémentation à venir
    }

    public void suivreBonCommande(int idBonCommande) {
        // Implémentation à venir
    }

    public void transmettreAuMagasinier(BonCommande bonCommande) {
        // Implémentation à venir
    }

    public List<Produit> consulterEtatStock() {
        // Implémentation à venir
        return new ArrayList<>();
    }

    public void recevoirAlertes() {
        // Implémentation à venir
    }

    public List<BonCommande> consulterHistoriqueAppro() {
        // Implémentation à venir
        return new ArrayList<>();
    }

    public Rapport genererRapportAchats() {
        // Implémentation à venir
        return null;
    }

    @Override
    public void authentifier() throws Exception {
        // Implémentation à venir
    }

    @Override
    public void deconnecter() {
        // Implémentation à venir
    }

    @Override
    public void modifierProfil() {
        // Implémentation à venir
    }
}

