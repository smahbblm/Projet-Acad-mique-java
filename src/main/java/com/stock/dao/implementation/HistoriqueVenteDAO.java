package com.stock.dao.implementation;

import com.stock.model.document.HistoriqueVente;
import com.stock.util.DatabaseConnection;
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
        String sql = "SELECT bl.numero as numeroFacture, bl.dateLivraison as dateFacture, " +
                    "CONCAT(c.nom, ' ', COALESCE(c.prenom, '')) as nomClient, " +
                    "p.designation as nomProduit, " +
                    "ll.quantite, " +
                    "ll.prixUnitaire, " +
                    "(ll.quantite * ll.prixUnitaire) as montantTotal " +
                    "FROM bon_livraison bl " +
                    "JOIN clients c ON bl.idClient = c.idClient " +
                    "JOIN ligne_livraison ll ON bl.idBonLivraison = ll.idBonLivraison " +
                    "JOIN produits p ON ll.idProduit = p.idProduit " +
                    "ORDER BY bl.dateLivraison DESC";
        
        System.out.println("Exécution de la requête: " + sql);

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
