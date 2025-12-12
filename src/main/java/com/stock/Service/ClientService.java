  package com.stock.service;

  import com.stock.dao.implementation.ClientDAO;
  import com.stock.model.partenaire.Client;
  //import com.stock.api.ApiClient;
  import java.util.List;

  public class ClientService {
         //l'attribut d'apisclient

      public ClientService() {
          System.out.println(" l'appel de la méthode clientservice qui va appler apiclient pour envoiyé la requette vers backend pour récupérer les clients ");
          //this.apiClient = new ApiClient();
      }

         //Récupérer tous les clients depuis le backend
      public List<Client> getAllClients() {
          System.out.println(" l'appel de la méthode getallclients qui va appler apiclient pour envoiyé la requette vers backend pour récupérer les clients ");
          try {
                ClientDAO client_dao = new ClientDAO();
                return client_dao.readAll();
          } catch (Exception e) {
                e.printStackTrace();
                return new java.util.ArrayList<>();
          }
      }


      public boolean addClient(Client client) {
          System.out.println(" l'appel de la méthode addclient qui va appler apiclient pour envoiyé la requette vers backend pour ajouter les clients ");
          return false;
      }

       //  Supprimer un client via le backend
      public boolean deleteClient(int clientId) {
          System.out.println(" l'appel de la méthode deleteclient qui va appler apiclient pour envoiyé la requette vers backend pour supprimer les clients ");
          return false;
      }

      /*
         Modifier un client via le backend
      public boolean updateClient(Client client) {
          System.out.println(" l'appel de la méthode updateclient qui va appler apiclient pour envoiyé la requette vers backend pour modifier les clients ");
          return apiClient.updateClient(client);
      }
      */

      //   Rechercher des clients (pour l'instant, récupère tous et filtre localement)
      public List<Client> searchClients(String searchTerm) {
          List<Client> allClients = getAllClients();
          return allClients.stream()
              .filter(client ->
                  client.getNom().toLowerCase().contains(searchTerm.toLowerCase()) ||
                  client.getPrenom().toLowerCase().contains(searchTerm.toLowerCase()) ||
                  client.getEmail().toLowerCase().contains(searchTerm.toLowerCase())
              )
              .toList();
      }
  }