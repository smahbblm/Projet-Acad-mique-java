package com.stock.Service;
import com.stock.model.DaoBon;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.util.List;
import com.stock.Service.BonService;

public class BonService {

    public int ajouterbons(String nombons , LocalDate dateLivraison, String status  , String notes){
        System.out.println("l'appel de la méthode ajouterbons avec  paramètres du formulaire ");
        DaoBon daoBon = new DaoBon();
        int res = daoBon.ajouterbon(nombons, dateLivraison,status, notes);
        return res;

    }
    // la méthode de supprission d'un bon de  bon dans la base de données
    public void supprimebon(){
        System.out.println("je suis la fonction de supprission ");
    }
}
