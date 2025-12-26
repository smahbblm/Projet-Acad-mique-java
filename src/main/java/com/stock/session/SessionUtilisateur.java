package com.stock.session;

import com.stock.model.utilisateur.Utilisateur;

public class SessionUtilisateur {

    private static Utilisateur utilisateurConnecte;

    public static void setUtilisateurConnecte(Utilisateur u) {
        utilisateurConnecte = u;
    }

    public static Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    public static void clear() {
        utilisateurConnecte = null;
    }
}
