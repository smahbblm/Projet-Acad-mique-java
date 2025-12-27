package com.stock.model.stock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe pour les inventaires
 */
public class Inventaire {
    private int idInventaire;
    private LocalDate dateInventaire;
    private String statut;
    private String observations;
    private List<LigneInventaire> lignes;

    public Inventaire() {
        this.dateInventaire = LocalDate.now();
        this.statut = "BROUILLON";
        this.lignes = new ArrayList<>();
    }

    // Getters et Setters
    public int getIdInventaire() {
        return idInventaire;
    }

    public void setIdInventaire(int idInventaire) {
        this.idInventaire = idInventaire;
    }

    public LocalDate getDateInventaire() {
        return dateInventaire;
    }

    public void setDateInventaire(LocalDate dateInventaire) {
        this.dateInventaire = dateInventaire;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public List<LigneInventaire> getLignes() {
        return lignes;
    }

    public void setLignes(List<LigneInventaire> lignes) {
        this.lignes = lignes;
    }

    // Méthodes métier
    public void creer() {
        this.statut = "EN_COURS";
    }

    public void realiser() {
        // Implémentation à venir
    }

    public void valider() {
        this.statut = "VALIDEE";
    }

    public com.stock.model.systeme.Rapport genererRapport() {
        // Implémentation à venir
        return null;
    }
}

