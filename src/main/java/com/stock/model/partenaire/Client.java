package com.stock.model.partenaire;

import com.stock.model.document.BonLivraison;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe pour les clients
 */
public class Client {
    private int idClient;
    private String nom;
    private String prenom;
    private String raisonSociale;
    private String adresse;
    private String telephone;
    private String email;
    private LocalDate dateInscription;

    public Client(String nom, String prenom, String raisonSociale, String adresse,
                  String telephone, String email) {
        this.nom = nom;
        this.prenom = prenom;
        this.raisonSociale = raisonSociale;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.dateInscription = LocalDate.now();
    }

    // Getters et Setters
    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
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

    public LocalDate getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }

    // Méthodes métier
    public void ajouterClient() {
        // Implémentation à venir
    }

    public void modifierClient() {
        // Implémentation à venir
    }

    public void supprimerClient() {
        // Implémentation à venir
    }

    public List<BonLivraison> consulterHistorique() {
        // Implémentation à venir
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Client{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

