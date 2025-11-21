package com.stock.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FactureDAO {

    public List<Facture> getAllFactures() {
        // Créer une liste de factures fictives
        List<Facture> factures = new ArrayList<>();
        // juste pour  teste car le backend n'est encore finie
        // Ajouter 3-4 factures de test
        factures.add(new Facture(1, "FAC-2024-001", LocalDate.now(), LocalDate.now().plusDays(30), 1000.0f, 200.0f, 1200.0f, "PAYEE", 1));
        factures.add(new Facture(2, "FAC-2024-002", LocalDate.now().minusDays(5), LocalDate.now().plusDays(25), 800.0f, 160.0f, 960.0f, "GENEREE", 2));
        factures.add(new Facture(3, "FAC-2024-003", LocalDate.now().minusDays(10), LocalDate.now().plusDays(20), 1500.0f, 300.0f, 1800.0f, "BROUILLON", 3));
        /*factures.add(new Facture(4, "FAC-2024-004", LocalDate.now().minusDays(2), LocalDate.now().plusDays(28), 650.0f, 130.0f, 780.0f, "ANNULEE", 4));
         */
        return factures;

        // pour la communication avec le backend

    }
}

