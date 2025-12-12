//package com.stock;
//
//import com.stock.util.DatabaseConnection;
//import java.sql.Connection;
//
///**
// * Test de connexion à la base de données
// */
//public class TestConnection {
//    public static void main(String[] args) {
//        System.out.println("═══════════════════════════════════════════════════════════");
//        System.out.println("  TEST DE CONNEXION À LA BASE DE DONNÉES MySQL");
//        System.out.println("═══════════════════════════════════════════════════════════");
//        System.out.println();
//
//        try {
//            System.out.println("⏳ Tentative de connexion à la base de données...");
//            System.out.println();
//
//            DatabaseConnection dbConnection = DatabaseConnection.getInstance();
//            Connection connection = dbConnection.getConnection();
//
//            if (connection != null && !connection.isClosed()) {
//                System.out.println("✅ CONNEXION RÉUSSIE !");
//                System.out.println();
//                System.out.println("📊 Informations de connexion:");
//                System.out.println("   • URL: " + connection.getMetaData().getURL());
//                System.out.println("   • Utilisateur: " + connection.getMetaData().getUserName());
//                System.out.println("   • Base de données: " + connection.getCatalog());
//                System.out.println("   • Driver: " + connection.getMetaData().getDriverName());
//                System.out.println("   • Version driver: " + connection.getMetaData().getDriverVersion());
//                System.out.println();
//
//                // Vérifier si des tables existent
//                var tables = connection.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
//                int tableCount = 0;
//                System.out.println("📋 Tables disponibles:");
//                while (tables.next()) {
//                    tableCount++;
//                    System.out.println("   " + tableCount + ". " + tables.getString("TABLE_NAME"));
//                }
//                System.out.println();
//
//                if (tableCount == 0) {
//                    System.out.println("⚠️  Aucune table trouvée dans la base de données.");
//                    System.out.println("   Vous devez importer le fichier schema.sql dans phpMyAdmin.");
//                } else {
//                    System.out.println("✅ " + tableCount + " tables trouvées dans la base de données.");
//                }
//
//                System.out.println();
//                System.out.println("═══════════════════════════════════════════════════════════");
//                System.out.println("  ✅ LA CONNEXION FONCTIONNE PARFAITEMENT !");
//                System.out.println("═══════════════════════════════════════════════════════════");
//
//            } else {
//                System.out.println("❌ ERREUR: Connexion nulle ou fermée");
//            }
//
//        } catch (Exception e) {
//            System.out.println("❌ ERREUR DE CONNEXION !");
//            System.out.println();
//            System.out.println("Détails de l'erreur:");
//            System.out.println("   Type: " + e.getClass().getSimpleName());
//            System.out.println("   Message: " + e.getMessage());
//            System.out.println();
//            System.out.println("═══════════════════════════════════════════════════════════");
//            System.out.println("  🔧 SOLUTIONS POSSIBLES:");
//            System.out.println("═══════════════════════════════════════════════════════════");
//            System.out.println();
//            System.out.println("1. Vérifier que MySQL/XAMPP/WAMP est LANCÉ");
//            System.out.println("   • Ouvrir le panneau de contrôle XAMPP/WAMP");
//            System.out.println("   • Démarrer le service MySQL");
//            System.out.println();
//            System.out.println("2. Vérifier que la base 'stock_management' EXISTE");
//            System.out.println("   • Ouvrir phpMyAdmin: http://localhost/phpmyadmin");
//            System.out.println("   • Vérifier si 'stock_management' est dans la liste");
//            System.out.println("   • Si absent, importer: database/schema.sql");
//            System.out.println();
//            System.out.println("3. Vérifier les identifiants dans database.properties");
//            System.out.println("   • Fichier: src/main/resources/config/database.properties");
//            System.out.println("   • Vérifier: db.user=root");
//            System.out.println("   • Vérifier: db.password= (vide ou votre mot de passe)");
//            System.out.println();
//            System.out.println("4. Vérifier le port MySQL");
//            System.out.println("   • Par défaut: 3306");
//            System.out.println("   • Si modifié, ajuster dans database.properties");
//            System.out.println();
//            System.out.println("═══════════════════════════════════════════════════════════");
//
//            // Afficher la stack trace complète pour le débogage
//            System.out.println();
//            System.out.println("Stack trace complète:");
//            e.printStackTrace();
//        }
//    }
//}
//
