package com.stock.ui.Controller.chef_vents;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class bonslivrController {

    //La  définition des  elements  fxml  dans notre projet

    @FXML  private Button    btnbonplus;
    @FXML  private Button    btnbonmoins;

    public void initialize() {
        System.out.println("je suis exactement le controller du votre  fenetre de gestion de Clients . ");
    }
    @FXML
    //une méthode  pour  affiché  un petit message plorsque je clic sur une des boutons
    public void ajoutebon(){
        System.out.println("l'ajoute  d'un bon par le chef de ventes ");
    }
    @FXML
    public void supprission(){
        System.out.println(" un bon  à ètè supprimé par le chef de ventes ");

    }
}
