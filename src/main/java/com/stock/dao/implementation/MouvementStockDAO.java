
package com.stock.dao.implementation;

import com.stock.dao.interfaces.IMouvementStockDAO;
import com.stock.model.produit.Produit;
import com.stock.model.stock.MouvementStock;

import com.stock.model.utilisateur.Magasinier;
import com.stock.model.utilisateur.Utilisateur;


import com.stock.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class MouvementStockDAO implements IMouvementStockDAO {
    private Connection connection;

    public MouvementStockDAO() throws Exception {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override

    public void create(MouvementStock mv) throws Exception {
        String sql = "INSERT INTO mouvement_stock " +
                "(dateMouvement, typeMouvement, quantite, stockAvant, stockApres, reference, idProduit, idUtilisateur) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, java.sql.Date.valueOf(mv.getDateMouvement()));
            stmt.setString(2, mv.getTypeMouvement());
            stmt.setInt(3, mv.getQuantite());
            stmt.setInt(4, mv.getStockAvant());
            stmt.setInt(5, mv.getStockApres());
            stmt.setString(6, mv.getReference());
            stmt.setInt(7, mv.getProduit().getIdProduit());
            stmt.setInt(8, mv.getUtilisateur() != null ? mv.getUtilisateur().getIdUtilisateur() : null);

            stmt.executeUpdate();
        }
    }


    @Override
    public MouvementStock read(int id) throws Exception {
        return null;
    }

    @Override
    public List<MouvementStock> readAll() throws Exception {

        List<MouvementStock> mouvements = new ArrayList<>();
        String sql = "SELECT ms.*, p.reference, p.designation, u.nom " +
                "FROM mouvement_stock ms " +
                "JOIN produits p ON ms.idProduit = p.idProduit " +
                "LEFT JOIN utilisateurs u ON ms.idUtilisateur = u.idUtilisateur " +
                "ORDER BY ms.dateMouvement DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             var rs = stmt.executeQuery()) {
            while (rs.next()) {
                MouvementStock mouvement = new MouvementStock(

                        rs.getString("typeMouvement"),
                        rs.getInt("quantite"),
                        rs.getString("reference")
                );

                mouvement.setIdMouvement(rs.getInt("idMouvement"));
                mouvement.setDateMouvement(rs.getObject("dateMouvement", java.time.LocalDate.class));
                mouvement.setStockAvant(rs.getInt("stockAvant"));
                mouvement.setStockApres(rs.getInt("stockApres"));

                com.stock.model.produit.Produit produit = new com.stock.model.produit.Produit(
                        rs.getString("reference"),
                        rs.getString("designation"),
                        0, 0, 0, 0, 0, ""
                );
                produit.setIdProduit(rs.getInt("idProduit"));
                mouvement.setProduit(produit);

                mouvements.add(mouvement);
            }
        }
        return mouvements;

    }

    @Override
    public void update(MouvementStock mouvementStock) throws Exception {
    }

    @Override
    public void delete(int id) throws Exception {
    }

    @Override
    public List<MouvementStock> findByProduit(int idProduit) throws Exception {
        return new ArrayList<>();
    }


    @Override
    public List<MouvementStock> findByType(String type) throws Exception {
        List<MouvementStock> mouvements = new ArrayList<>();
        String sql = "SELECT ms.*, p.reference, p.designation, u.nom " +
                "FROM mouvement_stock ms " +
                "JOIN produits p ON ms.idProduit = p.idProduit " +
                "LEFT JOIN utilisateurs u ON ms.idUtilisateur = u.idUtilisateur " +
                "WHERE ms.typeMouvement = ? " +
                "ORDER BY ms.dateMouvement DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, type);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MouvementStock mouvement = new MouvementStock(
                            rs.getString("typeMouvement"),
                            rs.getInt("quantite"),
                            rs.getString("reference")
                    );
                    mouvement.setIdMouvement(rs.getInt("idMouvement"));
                    mouvement.setDateMouvement(rs.getObject("dateMouvement", java.time.LocalDate.class));
                    mouvement.setStockAvant(rs.getInt("stockAvant"));
                    mouvement.setStockApres(rs.getInt("stockApres"));

                    Produit produit = new Produit(
                            rs.getString("reference"),
                            rs.getString("designation"),
                            0, 0, 0, 0, 0, ""
                    );
                    produit.setIdProduit(rs.getInt("idProduit"));
                    mouvement.setProduit(produit);

                    mouvements.add(mouvement);
                }
            }
        }

        return mouvements;
    }


    public List<Object[]> chiffreAffairesParMois() throws Exception {
        List<Object[]> result = new ArrayList<>();

        String sql = """
                    SELECT DATE_FORMAT(dateMouvement, '%Y-%m') AS mois,
                           SUM(ms.quantite * p.prixVente) AS total
                    FROM mouvement_stock ms
                    JOIN produits p ON ms.idProduit = p.idProduit
                    WHERE ms.typeMouvement = 'SORTIE'
                    GROUP BY mois
                    ORDER BY mois
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             var rs = stmt.executeQuery()) {

            while (rs.next()) {
                result.add(new Object[]{
                        rs.getString("mois"),
                        rs.getDouble("total")
                });
            }
        }
        return result;
    }

    public List<Object[]> topVentes() throws Exception {
        List<Object[]> result = new ArrayList<>();

        String sql = """
                    SELECT p.designation, SUM(ms.quantite) AS total
                    FROM mouvement_stock ms
                    JOIN produits p ON ms.idProduit = p.idProduit
                    WHERE ms.typeMouvement = 'SORTIE'
                    GROUP BY p.designation
                    ORDER BY total DESC
                    LIMIT 5
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             var rs = stmt.executeQuery()) {

            while (rs.next()) {
                result.add(new Object[]{
                        rs.getString("designation"),
                        rs.getInt("total")
                });
            }
        }
        return result;
    }

    public List<Object[]> commandesParMois() throws Exception {
        List<Object[]> result = new ArrayList<>();

        // SQL : groupe par mois uniquement les mouvements SORTIE
        String sql = """
                    SELECT DATE_FORMAT(dateMouvement, '%Y-%m') AS mois,
                           COUNT(*) AS total
                    FROM mouvement_stock
                    WHERE typeMouvement = 'SORTIE'
                    GROUP BY mois
                    ORDER BY mois
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             var rs = stmt.executeQuery()) {

            while (rs.next()) {
                String mois = rs.getString("mois");
                int total = rs.getInt("total");
                result.add(new Object[]{mois, total});
            }
        }

        // Optionnel : ajouter les mois manquants avec 0 si tu veux tous les mois de l’année
        String[] allMonths = {"2025-08", "2025-09", "2025-10", "2025-11", "2025-12"};
        List<Object[]> finalResult = new ArrayList<>();
        for (String m : allMonths) {
            boolean found = result.stream().anyMatch(r -> r[0].equals(m));
            if (!found) {
                finalResult.add(new Object[]{m, 0});
            } else {
                // ajouter le vrai total
                result.stream().filter(r -> r[0].equals(m)).forEach(finalResult::add);
            }
        }

        // Trier par mois
        finalResult.sort((a, b) -> ((String) a[0]).compareTo((String) b[0]));

        return finalResult;
    }
}
