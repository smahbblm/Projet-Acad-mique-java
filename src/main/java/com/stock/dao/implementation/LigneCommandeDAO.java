<<<<<<< HEAD
package com.stock.dao.implementation;

import com.stock.dao.interfaces.ILigneCommandeDAO;
import com.stock.model.document.LigneCommande;
<<<<<<< HEAD
import com.stock.model.produit.Produit;
import com.stock.util.DatabaseConnection;
=======
import com.stock.model.DatabaseConnection;
>>>>>>> e06bae3 (section de livraison)
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LigneCommandeDAO implements ILigneCommandeDAO {
    private Connection connection;

    public LigneCommandeDAO() throws Exception {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public void create(LigneCommande ligneCommande) throws Exception {}

    @Override
    public LigneCommande read(int id) throws Exception {
        return null;
    }

    @Override
    public List<LigneCommande> readAll() throws Exception {
        return new ArrayList<>();
    }

    @Override
    public void update(LigneCommande ligneCommande) throws Exception {}

    @Override
    public void delete(int id) throws Exception {}

    @Override
    public List<LigneCommande> findByBonCommande(int idBonCommande) throws Exception {
        List<LigneCommande> lignes = new ArrayList<>();
        String sql = "SELECT lc.*, p.reference, p.designation, p.description, p.prixAchat, p.prixVente, p.quantiteStock, p.seuilMin, p.seuilMax, p.categorie, p.dateAjout " +
                "FROM ligne_commande lc " +
                "JOIN produits p ON lc.idProduit = p.idProduit " +
                "WHERE lc.idBonCommande = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idBonCommande);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Créer l'objet Produit
                    Produit produit = new Produit();
                    produit.setIdProduit(rs.getInt("idProduit"));
                    produit.setReference(rs.getString("reference"));
                    produit.setDesignation(rs.getString("designation"));
                    produit.setDescription(rs.getString("description"));
                    produit.setPrixAchat(rs.getFloat("prixAchat"));
                    produit.setPrixVente(rs.getFloat("prixVente"));
                    produit.setQuantiteStock(rs.getInt("quantiteStock"));
                    produit.setSeuilMin(rs.getInt("seuilMin"));
                    produit.setSeuilMax(rs.getInt("seuilMax"));
                    produit.setCategorie(rs.getString("categorie"));
                    produit.setDateAjout(rs.getDate("dateAjout").toLocalDate());

                    // Créer la ligne de commande
                    LigneCommande ligne = new LigneCommande(
                            produit,
                            rs.getInt("quantite"),
                            rs.getFloat("prixUnitaire")
                    );
                    ligne.setIdLigne(rs.getInt("idLigne"));

                    lignes.add(ligne);
                }
            }
        }

        return lignes;
    }

}

=======
package com.stock.dao.implementation;

import com.stock.dao.interfaces.ILigneCommandeDAO;
import com.stock.model.document.LigneCommande;
import com.stock.util.DatabaseConnection;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class LigneCommandeDAO implements ILigneCommandeDAO {
    private Connection connection;

    public LigneCommandeDAO() throws Exception {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public void create(LigneCommande ligneCommande) throws Exception {}

    @Override
    public LigneCommande read(int id) throws Exception {
        return null;
    }

    @Override
    public List<LigneCommande> readAll() throws Exception {
        return new ArrayList<>();
    }

    @Override
    public void update(LigneCommande ligneCommande) throws Exception {}

    @Override
    public void delete(int id) throws Exception {}

    @Override
    public List<LigneCommande> findByBonCommande(int idBonCommande) throws Exception {
        return new ArrayList<>();
    }
}

>>>>>>> d44fc21 (fin  de code)
