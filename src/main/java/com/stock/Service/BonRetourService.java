package com.stock.service;

import com.stock.dao.implementation.BonRetourDAO;
import com.stock.model.document.BonRetour;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class BonRetourService {

    public boolean creerBonRetour(BonRetour bonRetour) {
        try {
            BonRetourDAO dao = new BonRetourDAO();
            bonRetour.creer();
            dao.create(bonRetour);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ObservableList<BonRetour> getAllBonsRetour() {
        try {
            BonRetourDAO dao = new BonRetourDAO();
            List<BonRetour> bons = dao.readAll();
            return FXCollections.observableArrayList(bons);
        } catch (Exception e) {
            e.printStackTrace();
            return FXCollections.observableArrayList();
        }
    }

    public boolean validerBonRetour(int idBonRetour) {
        try {
            BonRetourDAO dao = new BonRetourDAO();
            BonRetour bonRetour = dao.read(idBonRetour);
            if (bonRetour != null) {
                bonRetour.valider();
                dao.update(bonRetour);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean transmettreBoRetour(int idBonRetour) {
        try {
            BonRetourDAO dao = new BonRetourDAO();
            BonRetour bonRetour = dao.read(idBonRetour);
            if (bonRetour != null) {
                bonRetour.transmettre();
                dao.update(bonRetour);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
