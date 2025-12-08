package com.stock.service;

import com.stock.dao.interfaces.IRapportDAO;
import com.stock.dao.implementation.RapportDAO;
import com.stock.model.systeme.Rapport;
import java.time.LocalDate;
import java.util.List;

/**
 * Service pour la gestion des rapports
 */
public class RapportService {
    private final IRapportDAO rapportDAO;

    public RapportService() {
        this.rapportDAO = new RapportDAO();
    }

    /**
     * Génère un rapport à partir du type et d'une période et le persiste.
     * @param type Type de rapport (ANALYSE_GLOBAL, ACHATS, VENTES, INVENTAIRE)
     * @param dateDe Date début (incluse)
     * @param dateA Date fin (incluse)
     * @return Rapport généré et persisté
     * @throws Exception si erreur DB
     */
    public Rapport genererRapport(String type, LocalDate dateDe, LocalDate dateA) throws Exception {
        // Utilise la logique de construction contenue dans RapportDAO
        return ((RapportDAO)rapportDAO).genererRapport(type, dateDe, dateA);
    }

    /**
     * Enregistre un rapport déjà rempli (contenu défini ailleurs).
     */
    public void enregistrerRapportManuel(Rapport rapport) throws Exception {
        rapportDAO.create(rapport);
    }

    public void supprimerRapport(int idRapport) throws Exception {
        rapportDAO.delete(idRapport);
    }

    public Rapport consulterRapport(int idRapport) throws Exception {
        return rapportDAO.read(idRapport);
    }

    public List<Rapport> consulterTousLesRapports() throws Exception {
        return rapportDAO.readAll();
    }

    public List<Rapport> consulterRapportsParType(String type) throws Exception {
        return rapportDAO.findByType(type);
    }

    public void mettreAJourRapport(Rapport rapport) throws Exception {
        rapportDAO.update(rapport);
    }

    // Les méthodes exporter/imprimer restent placeholders pour évolution future.
    public void exporterRapport(Rapport rapport) {
        rapport.exporter();
    }

    public void imprimerRapport(Rapport rapport) {
        rapport.imprimer();
    }
}
