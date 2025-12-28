package com.stock.model.utilisateur;

public class UserFactory {

    public static Utilisateur createUser(String role, String nom, String prenom, String email, String mdp) {

        if (role == null) throw new IllegalArgumentException("Rôle non défini");

        // .toUpperCase() transforme "Administrateur" en "ADMINISTRATEUR"
        // .trim() enlève les espaces inutiles
        String roleNormalise = role.trim().toUpperCase();

        switch (roleNormalise) {
            case "ADMINISTRATEUR":
                return new Administrateur(nom, prenom, email, mdp);

            case "RESPONSABLE_APPROVISIONNEMENT":
                return new ResponsableApprovisionnement(nom, prenom, email, mdp);

            case "RESPONSABLE_VENTES":
                return new ResponsableVentes(nom, prenom, email, mdp);

            case "MAGASINIER":
                return new Magasinier(nom, prenom, email, mdp);

            default:
                throw new IllegalArgumentException("Rôle inconnu : " + role);
        }
    }
}