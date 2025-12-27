package com.stock.service;

import com.stock.dao.interfaces.IMouvementStockDAO;
import com.stock.dao.implementation.MouvementStockDAO;
import com.stock.model.stock.MouvementStock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service pour la gestion des mouvements de stock
 */
public class StockService {
    private IMouvementStockDAO mouvementStockDAO;

    public StockService() throws Exception {
        this.mouvementStockDAO = new MouvementStockDAO();
    }

    public void enregistrerEntree(MouvementStock mouvement) throws Exception {
        mouvement.setTypeMouvement("ENTREE");
        mouvementStockDAO.create(mouvement);
    }

    public void enregistrerSortie(MouvementStock mouvement) throws Exception {
        mouvement.setTypeMouvement("SORTIE");
        mouvementStockDAO.create(mouvement);
    }

    public List<MouvementStock> consulterMouvementsProduit(int idProduit) throws Exception {
        return mouvementStockDAO.findByProduit(idProduit);
    }

    public List<MouvementStock> consulterMouvementsParType(String type) throws Exception {
        return mouvementStockDAO.findByType(type);
    }

    public List<MouvementStock> consulterTousMouvements() throws Exception {
        return mouvementStockDAO.readAll();
    }
    public int getNombre_mouvement(){
        int  nbrMouvements=0;
        try {
            List<MouvementStock> list_mouvements = mouvementStockDAO.readAll();
            for (MouvementStock M : list_mouvements) {
                if (M.getDateMouvement() != null && M.getDateMouvement().isEqual(LocalDate.now()))
                    nbrMouvements++;
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return nbrMouvements;
    }
    public List<MouvementStock> getMouvementsEntree() throws Exception{
          return mouvementStockDAO.findByType("ENTREE");
    }
    public List<MouvementStock> getMouvementsSortie() throws Exception{
        return mouvementStockDAO.findByType("Sortie");
    }
    public List<MouvementStock> getMouvementsSemaine() throws Exception {
        List<MouvementStock> tous=mouvementStockDAO.readAll();
        List<MouvementStock>  Mouvements_semaine=new ArrayList<>();
        LocalDate DebutSemaine=LocalDate.now().minusDays(7);
         for(MouvementStock M:tous){
             if(M.getDateMouvement().isAfter(DebutSemaine) || M.getDateMouvement().isEqual(DebutSemaine)){
                    Mouvements_semaine.add(M);
             }
         }
         return Mouvements_semaine;


    }
    public List<MouvementStock> getMouvementsMois() throws Exception {
        List<MouvementStock> tous=mouvementStockDAO.readAll();
        List<MouvementStock>  Mouvements_Mois=new ArrayList<>();
        LocalDate DebutMois=LocalDate.now().minusDays(30);
        for(MouvementStock M:tous){
            if(M.getDateMouvement().isAfter(DebutMois) || M.getDateMouvement().isEqual(DebutMois)){
                Mouvements_Mois.add(M);
            }
        }
        return Mouvements_Mois;

    }
    public List<MouvementStock> getMouvementsAnnee() throws Exception {
        List<MouvementStock> tous=mouvementStockDAO.readAll();
        List<MouvementStock>  Mouvements_Annee=new ArrayList<>();
        LocalDate DebutAnnee=LocalDate.now().minusYears(1);
        for(MouvementStock M:tous){
            if(M.getDateMouvement().isAfter(DebutAnnee) || M.getDateMouvement().isEqual(DebutAnnee)){
                Mouvements_Annee.add(M);
            }
        }
        return  Mouvements_Annee;

    }
    public int getNbrMouvementsEntree() throws  Exception{
        List<MouvementStock> Entres=mouvementStockDAO.findByType("ENTREE");
        List<MouvementStock>  MouvemetsEntreeAjour=new ArrayList<>();

        for (MouvementStock M:Entres){
            if(M.getDateMouvement().isEqual(LocalDate.now())){
                       MouvemetsEntreeAjour.add(M);
            }
        }
        return MouvemetsEntreeAjour.size();
    }
    public int getNbrMouvementsSortie() throws  Exception{
        List<MouvementStock> Sorties=mouvementStockDAO.findByType("SORTIE");
        List<MouvementStock>  MouvemetsSortieAjour=new ArrayList<>();

        for (MouvementStock M:Sorties){
            if(M.getDateMouvement().isEqual(LocalDate.now())){
                MouvemetsSortieAjour.add(M);
            }
        }
        return  MouvemetsSortieAjour.size();
    }


}

