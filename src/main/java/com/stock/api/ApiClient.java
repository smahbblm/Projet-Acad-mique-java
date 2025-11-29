package com.stock.api;

import com.stock.ui.Controller.chef_vents.Clients.clientsController.Client;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.List;
import java.util.ArrayList;

public class ApiClient {
    
    private final String BASE_URL = "http://localhost:8080/apis"; // URL de votre backend
    private final HttpClient httpClient;
    public ApiClient() {
        this.httpClient = HttpClient.newHttpClient();
    }
    
    // GET - Récupérer tous les clients
    public List<Client> getAllClients() {
        try {
            // C'EST   la préparation  de requette HTTP qui va transport notre requette GET()  n'est ce pas
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/clients"))
                .header("Content-Type", "application/json")
                .GET()
                .build();
            
            HttpResponse<String> response = httpClient.send(request, 
                HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                // TODO: Parser le JSON en List<Client>
                // Pour l'instant, retourne une liste vide
                return new ArrayList<>();
            } else {
                System.err.println("Erreur API: " + response.statusCode());
                return new ArrayList<>();
            }
            
        } catch (Exception e) {
            System.err.println("Erreur de connexion au backend: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // POST - Ajouter un client
    public boolean addClient(Client client) {
        try {
            System.out.println(" l'appel de la méthode addclient qui va appler apiclient pour envoiyé la requette vers backend pour ajouter les clients ");
            // TODO: Convertir Client en JSON
            //c'est ici qu'on dois mettre le client  qu'on veux ajoueté
            String jsonBody = "{}"; // Temporaire
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/clients/ajouteclients"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
            
            HttpResponse<String> response = httpClient.send(request, 
                HttpResponse.BodyHandlers.ofString());
            
            return response.statusCode() == 201 || response.statusCode() == 200;
            
        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout: " + e.getMessage());
            return false;
        }
    }
    
    // DELETE - Supprimer un client
    public boolean deleteClient(int clientId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/clients/suppclients" + clientId))
                .DELETE()
                .build();
            
            HttpResponse<String> response = httpClient.send(request, 
                HttpResponse.BodyHandlers.ofString());
            
            return response.statusCode() == 200 || response.statusCode() == 204;
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression: " + e.getMessage());
            return false;
        }
    }
}