package com.stock.service;

import com.stock.dao.interfaces.IFournisseurDAO;
import com.stock.dao.implementation.FournisseurDAO;
import com.stock.model.partenaire.Fournisseur;
import java.util.List;

public class FournisseurService {
    private IFournisseurDAO fournisseurDAO;

    public FournisseurService() throws Exception {
        this.fournisseurDAO = new FournisseurDAO();
    }

    public void ajouterFournisseur(Fournisseur fournisseur) throws Exception {
        fournisseurDAO.create(fournisseur);
    }

    public void modifierFournisseur(Fournisseur fournisseur) throws Exception {
        fournisseurDAO.update(fournisseur);
    }

    public void supprimerFournisseur(int idFournisseur) throws Exception {
        fournisseurDAO.delete(idFournisseur);
    }

    public Fournisseur consulterFournisseur(int idFournisseur) throws Exception {
        return fournisseurDAO.read(idFournisseur);
    }

    public List<Fournisseur> consulterTousFournisseurs() throws Exception {
        return fournisseurDAO.readAll();
    }
}
