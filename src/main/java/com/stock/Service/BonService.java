package com.stock.Service;

import com.stock.api.ApisBons;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;

public class BonService {
    // je dois connaitre l'aobjet  apis que je dois lui  faire l'appel pour  envoié la requette vers le backend
    private ApisBons apisBons;



    public void ajouterbons(String nombons, LocalDate dateCommande, LocalDate dateLivraison , ObservableList<String> listefournisse, ObservableList<String> status , String montant, String notes){
        System.out.println("l'appel de la méthode ajouterbons de BonService ");
        apisBons.ajouterbons(nombons, dateCommande,  dateLivraison, listefournisse, status, montant,notes );
    }
}
