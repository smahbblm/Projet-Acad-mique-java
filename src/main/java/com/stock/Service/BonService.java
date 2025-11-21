package com.stock.Service;

import com.stock.api.ApisBons;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;

public class BonService {
    // je dois connaitre l'aobjet  apis que je dois lui  faire l'appel pour  envoié la requette vers le backend
    private ApisBons apisBons;

    public BonService(ApisBons apisBons) {
        this.apisBons = apisBons;
    }

    public void ajouterbons(String nombons, LocalDate dateCommande, LocalDate dateLivraison , ObservableList<String> listefournisse, ObservableList<String> status , String montant, String notes){
        System.out.println("l'appel de la méthode ajouterbons de BonService ");
        apisBons.ajouterbons(nombons, dateCommande,  dateLivraison, listefournisse, status, montant,notes );

    }
    // la méthode de supprission d'un bon de  bon dans la base de données
    public void ssupprimebon(){
        System.out.println("je suis la fonction de supprission ");
        apisBons.supprimebon();
    }
}
