package com.stock.util;

import static spark.Spark.*;

public class BackendServer {
    public static void main(String[] args) {

        // Démarrer serveur sur port 8080
        port(8080);

        // Route GET /produits
        get("/produits", (req, res) -> {
            res.type("application/json");
            return "[{\"id\":1,\"nom\":\"Produit A\"}, {\"id\":2,\"nom\":\"Produit B\"}]";
        });

        System.out.println("Backend listening on http://localhost:8080");
    }
}
