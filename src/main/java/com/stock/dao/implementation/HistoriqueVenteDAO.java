package com.stock.dao.implementation;

import com.stock.model.HistoriqueVente;
import com.stock.model.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HistoriqueVenteDAO {
    private Connection connection;

    public HistoriqueVenteDAO() throws Exception {
        this.connection = DatabaseConnection.getConnection();
    }

    public List<HistoriqueVente> getAllVentes() throws Exception {
        List<HistoriqueVente> ventes = new ArrayList<>();
        String sql = "SELECT f.numero as numeroFacture, f.dateFacture, c.nom as nomClient, " +
                    "p.designation as nomProduit, lf.quantite, lf.prixUnitaire, " +
                    "(lf.quantite * lf.prixUnitaire) as montantTotal " +
                    "FROM factures f " +
                    "JOIN clients c ON f.idClient = c.idClient " +
                    "JOIN lignes_facture lf ON f.idFacture = lf.idFacture " +
                    "JOIN produits p ON lf.idProduit = p.idProduit " +
                    "ORDER BY f.dateFacture DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                try {
                    HistoriqueVente vente = new HistoriqueVente();
                    vente.setNumeroFacture(rs.getString("numeroFacture"));
                    vente.setDateVente(rs.getDate("dateFacture").toLocalDate());
                    vente.setNomClient(rs.getString("nomClient"));
                    vente.setNomProduit(rs.getString("nomProduit"));
                    vente.setQuantite(rs.getInt("quantite"));
                    vente.setPrixUnitaire(rs.getDouble("prixUnitaire"));
                    vente.setMontantTotal(rs.getDouble("montantTotal"));
                    
                    ventes.add(vente);
                } catch (Exception e) {
                    System.out.println("Erreur lors de la création d'une vente: " + e.getMessage());
                }
            }
        }
        
        System.out.println("Nombre de ventes récupérées: " + ventes.size());
        return ventes;
    }
}