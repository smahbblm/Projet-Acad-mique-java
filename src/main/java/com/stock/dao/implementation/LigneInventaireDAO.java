
package com.stock.dao.implementation;

import com.stock.dao.interfaces.ILigneInventaireDAO;
import com.stock.model.produit.Produit;
import com.stock.model.stock.LigneInventaire;
import com.stock.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LigneInventaireDAO implements ILigneInventaireDAO {
    private Connection connection;
  private ProduitDAO produitDAO;
    public LigneInventaireDAO() throws Exception {
        this.connection = DatabaseConnection.getConnection();
        this.produitDAO=new ProduitDAO();
    }

    @Override
    public void create(LigneInventaire ligneInventaire) throws Exception {
        String sql = "INSERT INTO ligne_inventaire(idInventaire,idProduit,quantiteTheorique,quantiteReelle,ecart) VALUES (?,?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, ligneInventaire.getInventaire().getIdInventaire());
            stmt.setInt(2, ligneInventaire.getProduit().getIdProduit());
            stmt.setInt(3,ligneInventaire.getQuantiteTheorique());
            stmt.setInt(4,ligneInventaire.getQuantiteReelle());
            stmt.setInt(5,ligneInventaire.getEcart());
            stmt.executeUpdate();

        }
        catch( SQLException e ){
             e.printStackTrace();
        }
    }
    @Override
    public LigneInventaire read(int idInventaire) throws Exception {
        return null;
    }

    @Override
    public List<LigneInventaire> readAll() throws Exception {

        return new ArrayList<>();
    }

    @Override
    public void update(LigneInventaire ligneInventaire) throws Exception {}

    @Override
    public void delete(int id) throws Exception {}

    @Override
    public List<LigneInventaire> findByInventaire(int idInventaire) throws Exception {

            List<LigneInventaire> list_lignes = new ArrayList<>();
            String sql = "SELECT * FROM ligne_inventaire WHERE idInventaire = ?";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, idInventaire);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    LigneInventaire ligne = new LigneInventaire();

                    ligne.setIdLigne(rs.getInt("idLigne"));
                    ligne.setQuantiteTheorique(rs.getInt("quantiteTheorique"));
                    ligne.setQuantiteReelle(rs.getInt("quantiteReelle"));
                    ligne.setEcart(rs.getInt("ecart"));

                    // 🔥 CHARGER LE PRODUIT
                    int idProduit = rs.getInt("idProduit");
                    Produit produit = produitDAO.read(idProduit); // ➜ récupère l'objet Produit
                    ligne.setProduit(produit);

                    list_lignes.add(ligne);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return list_lignes;
        }






}
