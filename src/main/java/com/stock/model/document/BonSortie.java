package com.stock.model.document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe pour les bons de sortie
 */
public class BonSortie {
    private int idBonSortie;
    private String numero;
    private LocalDate dateSortie;
    private String statut;
    private List<LigneSortie> lignes;

    public BonSortie(String numero) {
        this.numero = numero;
        this.dateSortie = LocalDate.now();
        this.statut = "BROUILLON";
        this.lignes = new ArrayList<>();
    }

    // Getters et Setters
    public int getIdBonSortie() {
        return idBonSortie;
    }

    public void setIdBonSortie(int idBonSortie) {
        this.idBonSortie = idBonSortie;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getDateSortie() {
        return dateSortie;
    }

    public void setDateSortie(LocalDate dateSortie) {
        this.dateSortie = dateSortie;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public List<LigneSortie> getLignes() {
        return lignes;
    }

    public void setLignes(List<LigneSortie> lignes) {
        this.lignes = lignes;
    }

    // Méthodes métier
    public void generer() {
        this.statut = "GENEREE";
    }

    public void valider() {
        this.statut = "VALIDEE";
    }

    public void enregistrer() {
        this.statut = "ENREGISTREE";
    }
}

