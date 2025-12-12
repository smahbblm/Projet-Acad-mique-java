
package com.stock.dao.implementation;

import com.stock.dao.interfaces.IBonLivraisonDAO;
import com.stock.model.document.BonLivraison;
import com.stock.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation DAO pour les bons de livraison
 */
public class BonLivraisonDAO implements IBonLivraisonDAO {
    private Connection connection;

    public BonLivraisonDAO() throws Exception {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void create(BonLivraison bonLivraison) throws Exception {
        String sql = "INSERT INTO bon_livraison (numero, dateLivraison, statut, adresseLivraison, observations, idClient) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, bonLivraison.getNumero());
            stmt.setObject(2, bonLivraison.getDateLivraison());
            stmt.setString(3, bonLivraison.getStatut());
            stmt.setString(4, bonLivraison.getAdresseLivraison());
            stmt.setString(5, bonLivraison.getObservations());
            stmt.setInt(6, bonLivraison.getClient().getIdClient());
            stmt.executeUpdate();
        }
    }

    @Override
    public BonLivraison read(int id) throws Exception {
        String sql = "SELECT bl.*, c.nom, c.prenom, c.raisonSociale, c.adresse, c.telephone, c.email " +
                    "FROM bon_livraison bl " +
                    "JOIN clients c ON bl.idClient = c.idClient " +
                    "WHERE bl.idBonLivraison=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBonLivraison(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<BonLivraison> readAll() throws Exception {
        List<BonLivraison> bonsLivraison = new ArrayList<>();
        String sql = "SELECT bl.*, c.nom, c.prenom, c.raisonSociale, c.adresse, c.telephone, c.email " +
                    "FROM bon_livraison bl " +
                    "JOIN clients c ON bl.idClient = c.idClient " +
                    "ORDER BY bl.dateLivraison DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             var rs = stmt.executeQuery()) {
            while (rs.next()) {
                bonsLivraison.add(mapResultSetToBonLivraison(rs));
            }
        }
        return bonsLivraison;
    }

    @Override
    public void update(BonLivraison bonLivraison) throws Exception {
        String sql = "UPDATE bon_livraison SET numero=?, dateLivraison=?, statut=?, adresseLivraison=?, observations=? " +
                     "WHERE idBonLivraison=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, bonLivraison.getNumero());
            stmt.setObject(2, bonLivraison.getDateLivraison());
            stmt.setString(3, bonLivraison.getStatut());
            stmt.setString(4, bonLivraison.getAdresseLivraison());
            stmt.setString(5, bonLivraison.getObservations());
            stmt.setInt(6, bonLivraison.getIdBonLivraison());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM bon_livraison WHERE idBonLivraison=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public BonLivraison findByNumero(String numero) throws Exception {
        String sql = "SELECT bl.*, c.nom, c.prenom, c.raisonSociale, c.adresse, c.telephone, c.email " +
                    "FROM bon_livraison bl " +
                    "JOIN clients c ON bl.idClient = c.idClient " +
                    "WHERE bl.numero=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, numero);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBonLivraison(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<BonLivraison> findByStatut(String statut) throws Exception {
        List<BonLivraison> bonsLivraison = new ArrayList<>();
        String sql = "SELECT bl.*, c.nom, c.prenom, c.raisonSociale, c.adresse, c.telephone, c.email " +
                    "FROM bon_livraison bl " +
                    "JOIN clients c ON bl.idClient = c.idClient " +
                    "WHERE bl.statut=? ORDER BY bl.dateLivraison DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, statut);
            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bonsLivraison.add(mapResultSetToBonLivraison(rs));
                }
            }
        }
        return bonsLivraison;
    }

    @Override
    public List<BonLivraison> findByClient(int idClient) throws Exception {
        List<BonLivraison> bonsLivraison = new ArrayList<>();
        String sql = "SELECT bl.*, c.nom, c.prenom, c.raisonSociale, c.adresse, c.telephone, c.email " +
                    "FROM bon_livraison bl " +
                    "JOIN clients c ON bl.idClient = c.idClient " +
                    "WHERE bl.idClient=? ORDER BY bl.dateLivraison DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idClient);
            try (var rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bonsLivraison.add(mapResultSetToBonLivraison(rs));
                }
            }
        }
        return bonsLivraison;
    }

    private BonLivraison mapResultSetToBonLivraison(java.sql.ResultSet rs) throws Exception {
        // Créer le client
        com.stock.model.partenaire.Client client = new com.stock.model.partenaire.Client(
            rs.getString("nom"),
            rs.getString("prenom"),
            rs.getString("raisonSociale"),
            rs.getString("adresse"),
            rs.getString("telephone"),
            rs.getString("email")
        );
        client.setIdClient(rs.getInt("idClient"));

        // Créer le bon de livraison
        BonLivraison bonLivraison = new BonLivraison(
            rs.getString("numero"),
            rs.getObject("dateLivraison", java.time.LocalDate.class),
            client
        );

        bonLivraison.setIdBonLivraison(rs.getInt("idBonLivraison"));
        bonLivraison.setStatut(rs.getString("statut"));
        bonLivraison.setAdresseLivraison(rs.getString("adresseLivraison"));
        bonLivraison.setObservations(rs.getString("observations"));

        return bonLivraison;
    }
    public void updateStatut(BonLivraison bonLivraison) throws Exception {
        String sql = "UPDATE bon_livraison SET statut=? WHERE idBonLivraison=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, bonLivraison.getStatut());
            stmt.setInt(2, bonLivraison.getIdBonLivraison());
            stmt.executeUpdate();
        }
    }
}


//package com.stock.dao.implementation;
//
//import com.stock.dao.interfaces.IBonLivraisonDAO;
//import com.stock.model.document.BonLivraison;
//import com.stock.util.DatabaseConnection;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Implémentation DAO pour les bons de livraison
// */
//public class BonLivraisonDAO implements IBonLivraisonDAO {
//    private Connection connection;
//
//    public BonLivraisonDAO() throws Exception {
//        this.connection = DatabaseConnection.getInstance().getConnection();
//    }
//
//    @Override
//    public void create(BonLivraison bonLivraison) throws Exception {
//        String sql = "INSERT INTO bon_livraison (numero, dateLivraison, statut, adresseLivraison, observations, idClient) " +
//                     "VALUES (?, ?, ?, ?, ?, ?)";
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setString(1, bonLivraison.getNumero());
//            stmt.setObject(2, bonLivraison.getDateLivraison());
//            stmt.setString(3, bonLivraison.getStatut());
//            stmt.setString(4, bonLivraison.getAdresseLivraison());
//            stmt.setString(5, bonLivraison.getObservations());
//            stmt.setInt(6, bonLivraison.getClient().getIdClient());
//            stmt.executeUpdate();
//        }
//    }
//
//    @Override
//    public BonLivraison read(int id) throws Exception {
//        String sql = "SELECT bl.*, c.nom, c.prenom, c.raisonSociale, c.adresse, c.telephone, c.email " +
//                    "FROM bon_livraison bl " +
//                    "JOIN clients c ON bl.idClient = c.idClient " +
//                    "WHERE bl.idBonLivraison=?";
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setInt(1, id);
//            try (var rs = stmt.executeQuery()) {
//                if (rs.next()) {
//                    return mapResultSetToBonLivraison(rs);
//                }
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public List<BonLivraison> readAll() throws Exception {
//        List<BonLivraison> bonsLivraison = new ArrayList<>();
//        String sql = "SELECT bl.*, c.nom, c.prenom, c.raisonSociale, c.adresse, c.telephone, c.email " +
//                    "FROM bon_livraison bl " +
//                    "JOIN clients c ON bl.idClient = c.idClient " +
//                    "ORDER BY bl.dateLivraison DESC";
//        try (PreparedStatement stmt = connection.prepareStatement(sql);
//             var rs = stmt.executeQuery()) {
//            while (rs.next()) {
//                bonsLivraison.add(mapResultSetToBonLivraison(rs));
//            }
//        }
//        return bonsLivraison;
//    }
//
//    @Override
//    public void update(BonLivraison bonLivraison) throws Exception {
//        String sql = "UPDATE bon_livraison SET numero=?, dateLivraison=?, statut=?, adresseLivraison=?, observations=? " +
//                     "WHERE idBonLivraison=?";
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setString(1, bonLivraison.getNumero());
//            stmt.setObject(2, bonLivraison.getDateLivraison());
//            stmt.setString(3, bonLivraison.getStatut());
//            stmt.setString(4, bonLivraison.getAdresseLivraison());
//            stmt.setString(5, bonLivraison.getObservations());
//            stmt.setInt(6, bonLivraison.getIdBonLivraison());
//            stmt.executeUpdate();
//        }
//    }
//
//    @Override
//    public void delete(int id) throws Exception {
//        String sql = "DELETE FROM bon_livraison WHERE idBonLivraison=?";
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setInt(1, id);
//            stmt.executeUpdate();
//        }
//    }
//
//    @Override
//    public BonLivraison findByNumero(String numero) throws Exception {
//        String sql = "SELECT bl.*, c.nom, c.prenom, c.raisonSociale, c.adresse, c.telephone, c.email " +
//                    "FROM bon_livraison bl " +
//                    "JOIN clients c ON bl.idClient = c.idClient " +
//                    "WHERE bl.numero=?";
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setString(1, numero);
//            try (var rs = stmt.executeQuery()) {
//                if (rs.next()) {
//                    return mapResultSetToBonLivraison(rs);
//                }
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public List<BonLivraison> findByStatut(String statut) throws Exception {
//        List<BonLivraison> bonsLivraison = new ArrayList<>();
//        String sql = "SELECT bl.*, c.nom, c.prenom, c.raisonSociale, c.adresse, c.telephone, c.email " +
//                    "FROM bon_livraison bl " +
//                    "JOIN clients c ON bl.idClient = c.idClient " +
//                    "WHERE bl.statut=? ORDER BY bl.dateLivraison DESC";
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setString(1, statut);
//            try (var rs = stmt.executeQuery()) {
//                while (rs.next()) {
//                    bonsLivraison.add(mapResultSetToBonLivraison(rs));
//                }
//            }
//        }
//        return bonsLivraison;
//    }
//
//    @Override
//    public List<BonLivraison> findByClient(int idClient) throws Exception {
//        List<BonLivraison> bonsLivraison = new ArrayList<>();
//        String sql = "SELECT bl.*, c.nom, c.prenom, c.raisonSociale, c.adresse, c.telephone, c.email " +
//                    "FROM bon_livraison bl " +
//                    "JOIN clients c ON bl.idClient = c.idClient " +
//                    "WHERE bl.idClient=? ORDER BY bl.dateLivraison DESC";
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setInt(1, idClient);
//            try (var rs = stmt.executeQuery()) {
//                while (rs.next()) {
//                    bonsLivraison.add(mapResultSetToBonLivraison(rs));
//                }
//            }
//        }
//        return bonsLivraison;
//    }
//
//    private BonLivraison mapResultSetToBonLivraison(java.sql.ResultSet rs) throws Exception {
//        // Créer le client
//        com.stock.model.partenaire.Client client = new com.stock.model.partenaire.Client(
//            rs.getString("nom"),
//            rs.getString("prenom"),
//            rs.getString("raisonSociale"),
//            rs.getString("adresse"),
//            rs.getString("telephone"),
//            rs.getString("email")
//        );
//        client.setIdClient(rs.getInt("idClient"));
//
//        // Créer le bon de livraison
//        BonLivraison bonLivraison = new BonLivraison(
//            rs.getString("numero"),
//            rs.getObject("dateLivraison", java.time.LocalDate.class),
//            client
//        );
//
//        bonLivraison.setIdBonLivraison(rs.getInt("idBonLivraison"));
//        bonLivraison.setStatut(rs.getString("statut"));
//        bonLivraison.setAdresseLivraison(rs.getString("adresseLivraison"));
//        bonLivraison.setObservations(rs.getString("observations"));
//
//        return bonLivraison;
//    }
//}

