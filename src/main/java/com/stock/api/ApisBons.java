package com.stock.api;

import javafx.collections.ObservableList;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Date;
import java.util.EnumMap;
import javafx.collections.ObservableList;
public class ApisBons {
    public static void main(String[] args) {
        System.out.println("VOIlà  MAINTENANT TU FAITS   L'APPELLE AUX APIX  DE  BONS  VERS  LE  BACKEND ");
    }

    // LES  INFORMATIONS  de l'application backend
    private final String url_back = "http://localhost:8080/apis";
    // j'ai besoin d'un  httpclient pour  transport mes requette  vers le backend
    private final HttpClient https = HttpClient.newHttpClient();

    // la  méthode qui permet d'ajouter un bons dans  la base de données
    public boolean ajouterbons(String matricule, LocalDate datecommande, LocalDate datelivraison, ObservableList<String> listfournisseur, ObservableList<String> status, String montantTotal, String description ){
        System.out.println("la récupérartions  de  données  entre  par le chef de ventes dans le formulaire ");
        try {

            // la preparation et l'envoi  de requette
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url_back + "/bons"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "{" +
                                    "\"numero\": " + matricule+ "," +
                                    "\"dateCommande\":" + datecommande + "," +
                                    "\"dateLivraisonPrevue\":" + datelivraison+"," +
                                    "\"statut\":"+ status +"," +
                                    "\"idFournisseur\":"+ listfournisseur +"," +
                                    "\"montantTotal\":" + montantTotal +"," +
                                    "\"observations\": " + description +"," +
                                    "}"
                    ))
                    .build();
            // l'envoi  de la requette
            HttpResponse reponse = https.send(request, HttpResponse.BodyHandlers.ofString());
            //tester
            if (reponse.statusCode() == 201 || reponse.statusCode() == 200) {
                System.out.println("Bon ajouté avec succès");
                return true;
            } else {
                System.out.println("Erreur lors de l'ajout du bon : " + reponse.statusCode());
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
