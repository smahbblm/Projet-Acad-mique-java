package com.stock.dao.interfaces;

import com.stock.model.partenaire.Client;
import java.util.List;

/**
 * Interface pour les opérations DAO sur les clients
 */
public interface IClientDAO {
    void create(Client client) throws Exception;
    Client read(int id) throws Exception;
    List<Client> readAll() throws Exception;
    void update(Client client) throws Exception;
    void delete(int id) throws Exception;
    Client findByEmail(String email) throws Exception;
    List<Client> findByNom(String nom) throws Exception;
}

