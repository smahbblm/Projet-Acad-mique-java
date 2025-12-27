package com.stock.model.systeme;

import java.time.LocalDate;

/**
 * Classe pour les rapports
 */
public class Rapport {
    private int idRapport;
    private String type;
    private LocalDate dateGeneration;
    private LocalDate dateDe;
    private LocalDate dateA;
    private String contenu;
    public Rapport(){

    }

    public Rapport(String type, LocalDate dateDe, LocalDate dateA) {
        this.type = type;
        this.dateGeneration = LocalDate.now();
        this.dateDe = dateDe;
        this.dateA = dateA;
    }

    // Getters et Setters
    public int getIdRapport() {
        return idRapport;
    }

    public void setIdRapport(int idRapport) {
        this.idRapport = idRapport;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDateGeneration() {
        return dateGeneration;
    }

    public void setDateGeneration(LocalDate dateGeneration) {
        this.dateGeneration = dateGeneration;
    }

    public LocalDate getDateDe() {
        return dateDe;
    }

    public void setDateDe(LocalDate dateDe) {
        this.dateDe = dateDe;
    }

    public LocalDate getDateA() {
        return dateA;
    }

    public void setDateA(LocalDate dateA) {
        this.dateA = dateA;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    // Méthodes métier
    public void generer() {
        // Implémentation à venir
    }

    public void exporter() {
        // Implémentation à venir
    }

    public void imprimer() {
        // Implémentation à venir
    }
}

