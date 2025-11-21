package com.stock.api;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ApisFactures {
    private String url_back ="http://localhost:8080/apis";
    private HttpClient  trans_http = HttpClient.newHttpClient();

    // LA préparation de la requete vers le backend
    public List<Object> getAllFactures(){
        try{
        System.out.println("je suis la méthode de apis pour définire la requette d'envoie ");
        //la construction  de la requette http  qui  m'a envié ma requettes
        HttpRequest request =  HttpRequest.newBuilder()
        .uri(URI.create(url_back+"/Factures")).GET()
                .header("Content-Type", "application/json")
                .build();
        // l'envoie de la requette vers le backend
        HttpResponse<String> reponse = trans_http.send(request, HttpResponse.BodyHandlers.ofString());

        //test la réponse de la requette
        if(reponse.statusCode()==200){
            System.out.println("la requette de récupération de factures a été bien envoyée");
            System.out.println("l'affichage de rponse de getallFactures"+reponse.body());
            List<Object> factures = new ArrayList<>();
            factures.add(reponse.body());
            return factures;

        }else{
            System.out.println("Erreur HTTP: " + reponse.statusCode());
            return new ArrayList<>();
        }
        }catch(Exception e){
            System.out.println("erreur lors de l'envoie de la requette "+e.getMessage());
            return new ArrayList<>();
        }
    }
}
