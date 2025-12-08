package com.stock.model.partenaire;

import com.stock.model.document.BonCommande;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe pour les fournisseurs
 */
public class Fournisseur {
    private int idFournisseur;
    private String raisonSociale;
    private String adresse;
    private String telephone;
    private String email;
    private String contact;
    private String conditions;
    private String ville;
    private boolean actif;

    public Fournisseur() {
        // Constructeur par défaut requis pour instanciation dynamique (JDBC, FXML, contrôleurs)
    }

    public Fournisseur(String raisonSociale, String adresse, String telephone, String email) {
        this.raisonSociale = raisonSociale;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
    }

    // Getters et Setters
    public int getIdFournisseur() {
        return idFournisseur;
    }

    public void setIdFournisseur(int idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    public String getRaisonSociale() {
        return raisonSociale;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    // Méthodes métier
    public void ajouterFournisseur() {
        // Implémentation à venir
    }

    public void modifierFournisseur() {
        // Implémentation à venir
    }

    public void supprimerFournisseur() {
        // Implémentation à venir
    }

    public List<BonCommande> consulterHistorique() {
        // Implémentation à venir
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Fournisseur{" +
                "raisonSociale='" + raisonSociale + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
