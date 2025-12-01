package com.stock.model.utilisateur;

import com.stock.model.stock.Inventaire;
import com.stock.model.stock.MouvementStock;
import com.stock.model.document.BonSortie;
import com.stock.model.produit.Produit;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe pour les magasiniers
 */
public class Magasinier extends Utilisateur {

    public Magasinier(String nom, String prenom, String email, String motDePasse) {
        super(nom, prenom, email, motDePasse, "MAGASINIER");
    }

    public void validerReceptionCommande(int idBonCommande) {
        // Implémentation à venir
    }

    public MouvementStock enregistrerEntreeStock(int idProduit, int quantite) {
        // Implémentation à venir
        return null;
    }

    public void validerBonLivraison(int idBonLivraison) {
        // Implémentation à venir
    }

    public BonSortie genererBonSortie(int idBonLivraison) {
        // Implémentation à venir
        return null;
    }

    public MouvementStock enregistrerSortieStock(int idProduit, int quantite) {
        // Implémentation à venir
        return null;
    }

    public void controlerQuantites() {
        // Implémentation à venir
    }

    public void signalerEcarts(int idProduit, int ecart) {
        // Implémentation à venir
    }

    public Inventaire realiserInventaire() {
        // Implémentation à venir
        return null;
    }

    public List<Produit> consulterListeProduits() {
        // Implémentation à venir
        return new ArrayList<>();
    }

    public List<MouvementStock> consulterHistoriqueMouvements() {
        // Implémentation à venir
        return new ArrayList<>();
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

