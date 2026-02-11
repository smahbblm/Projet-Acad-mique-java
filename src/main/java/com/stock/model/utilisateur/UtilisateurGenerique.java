package com.stock.model.utilisateur;

/**
 * Classe utilisateur générique pour la connexion
 */
public class UtilisateurGenerique extends Utilisateur {

    public UtilisateurGenerique() {
        super();
    }

    public UtilisateurGenerique(String nom, String prenom, String email, String motDePasse, String role) {
        super(nom, prenom, email, motDePasse, role);
    }

    @Override
    public void authentifier() throws Exception {
        // Implémentation de base
        System.out.println("Utilisateur authentifié: " + getEmail());
    }

    @Override
    public void deconnecter() {
        // Implémentation de base
        System.out.println("Utilisateur déconnecté: " + getEmail());
    }

    @Override
    public void modifierProfil() {
        // Implémentation de base
        System.out.println("Profil modifié pour: " + getEmail());
    }
}

