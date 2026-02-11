package com.stock.model.utilisateur;

import com.stock.model.document.BonLivraison;
import com.stock.model.document.Facture;
import com.stock.model.partenaire.Client;
import com.stock.model.systeme.Rapport;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe pour les responsables des ventes
 */
public class ResponsableVentes extends Utilisateur {

    public ResponsableVentes(String nom, String prenom, String email, String motDePasse) {
        super(nom, prenom, email, motDePasse, "RESPONSABLE_VENTES");
    }

    public void gererClients() {
        // Implémentation à venir
    }

    public Client ajouterClient(Client client) {
        // Implémentation à venir
        return null;
    }

    public void modifierClient(Client client) {
        // Implémentation à venir
    }

    public void supprimerClient(int idClient) {
        // Implémentation à venir
    }

    public boolean consulterDisponibilite(int idProduit, int quantite) {
        // Implémentation à venir
        return false;
    }

    public BonLivraison creerBonLivraison() {
        // Implémentation à venir
        return null;
    }

    public Facture genererFacture(BonLivraison bonLivraison) {
        // Implémentation à venir
        return null;
    }

    public void transmettreBonLivraison(BonLivraison bonLivraison) {
        // Implémentation à venir
    }

    public List<BonLivraison> consulterHistoriqueVentes() {
        // Implémentation à venir
        return new ArrayList<>();
    }

    public Rapport genererRapportVentes() {
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

