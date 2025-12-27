package com.stock.model.utilisateur;

public class UserFactory {

    public static Utilisateur createUser(String role, String nom, String prenom, String email, String mdp) {

        if (role == null) throw new IllegalArgumentException("Rôle non défini");

        switch (role.trim()) {  // on enlève les espaces avant/après
            case "Administrateur":
                return new Administrateur(nom, prenom, email, mdp);

            case "Responsable_Approvisionnement":
                return new ResponsableApprovisionnement(nom, prenom, email, mdp);

            case "Responsable_Ventes":
                return new ResponsableVentes(nom, prenom, email, mdp);

            case "Magasinier":
                return new Magasinier(nom, prenom, email, mdp);

            default:
                throw new IllegalArgumentException("Rôle inconnu : " + role);
        }
    }
}

