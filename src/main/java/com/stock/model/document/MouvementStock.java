package com.stock.model.document;

import java.time.LocalDate;

/**
 * Représente un mouvement de stock (ENTREE, SORTIE, AJUSTEMENT, INVENTAIRE)
 * Correspond à la table mouvement_stock.
 */
public class MouvementStock {
    private int idMouvement;
    private LocalDate dateMouvement;
    private String typeMouvement; // ENTREE, SORTIE, AJUSTEMENT, INVENTAIRE
    private int quantite;
    private Integer stockAvant;
    private Integer stockApres;
    private String reference; // référence produit
    private int idProduit;
    private Integer idUtilisateur; // facultatif

    public MouvementStock() {
    }

    public MouvementStock(LocalDate dateMouvement, String typeMouvement, int quantite, Integer stockAvant, Integer stockApres, String reference, int idProduit, Integer idUtilisateur) {
        this.dateMouvement = dateMouvement;
        this.typeMouvement = typeMouvement;
        this.quantite = quantite;
        this.stockAvant = stockAvant;
        this.stockApres = stockApres;
        this.reference = reference;
        this.idProduit = idProduit;
        this.idUtilisateur = idUtilisateur;
    }

    public int getIdMouvement() { return idMouvement; }
    public void setIdMouvement(int idMouvement) { this.idMouvement = idMouvement; }

    public LocalDate getDateMouvement() { return dateMouvement; }
    public void setDateMouvement(LocalDate dateMouvement) { this.dateMouvement = dateMouvement; }

    public String getTypeMouvement() { return typeMouvement; }
    public void setTypeMouvement(String typeMouvement) { this.typeMouvement = typeMouvement; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public Integer getStockAvant() { return stockAvant; }
    public void setStockAvant(Integer stockAvant) { this.stockAvant = stockAvant; }

    public Integer getStockApres() { return stockApres; }
    public void setStockApres(Integer stockApres) { this.stockApres = stockApres; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public int getIdProduit() { return idProduit; }
    public void setIdProduit(int idProduit) { this.idProduit = idProduit; }

    public Integer getIdUtilisateur() { return idUtilisateur; }
    public void setIdUtilisateur(Integer idUtilisateur) { this.idUtilisateur = idUtilisateur; }

    // Helpers
    public boolean isEntree() { return "ENTREE".equalsIgnoreCase(typeMouvement); }
    public boolean isSortie() { return "SORTIE".equalsIgnoreCase(typeMouvement); }
    public boolean isAjustement() { return "AJUSTEMENT".equalsIgnoreCase(typeMouvement); }
    public boolean isInventaire() { return "INVENTAIRE".equalsIgnoreCase(typeMouvement); }
}

