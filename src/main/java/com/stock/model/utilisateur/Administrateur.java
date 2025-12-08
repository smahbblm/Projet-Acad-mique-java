package com.stock.model.utilisateur;

import com.stock.model.systeme.Rapport;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe pour les administrateurs
 */
public class Administrateur extends Utilisateur {

    public Administrateur(String nom, String prenom, String email, String motDePasse) {
        super(nom, prenom, email, motDePasse, "ADMINISTRATEUR");
    }

    public void gererUtilisateurs() {
        // Implémentation à venir
    }

    public void ajouterUtilisateur(Utilisateur utilisateur) {
        // Implémentation à venir
    }

    public void modifierUtilisateur(Utilisateur utilisateur) {
        // Implémentation à venir
    }

    public void supprimerUtilisateur(int idUtilisateur) {
        // Implémentation à venir
    }

    public void consulterTableauBord() {
        // Implémentation à venir
    }

    public void visualiserStatistiques() {
        // Implémentation à venir
    }

    public Rapport genererRapportAnalyse() {
        // Implémentation à venir
        return null;
    }

    public void configurerParametres() {
        // Implémentation à venir
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

