package com.stock.Service;
import  java.lang.System;
import java.util.List;

import com.stock.api.ApisFactures;
public class FactureServce {
    public void main(String[] args) {
        //dans cette service on doit mettre le code  de la méthode qui  envois  la requtte vers le backend pour récuper les données
        System.out.println("je suis la méthode pour récupere les facts");
    }
    ApisFactures apisFactures = new ApisFactures();
    public List<Object> allfacture(){
        System.out.println("je suis la méthode pour faire appel à la méthode des apis ");
        return apisFactures.getAllFactures();
    }
}