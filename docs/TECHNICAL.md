# Documentation Technique - Système de Gestion de Stock

## Vue d'ensemble
Ce document décrit l'architecture et les détails techniques du système de gestion de stock.

## 1. Architecture Générale

### Layers
```
┌─────────────────────────────────────┐
│   Présentation (GUI - JavaFX)       │
│   - Controllers                     │
│   - FXML Views                      │
└─────────────────────────────────────┘
           ↓
┌─────────────────────────────────────┐
│   Métier (Services)                 │
│   - AuthService                     │
│   - ProduitService                  │
│   - StockService                    │
│   - CommandeService                 │
│   - VenteService                    │
│   - InventaireService               │
│   - RapportService                  │
└─────────────────────────────────────┘
           ↓
┌─────────────────────────────────────┐
│   Accès aux Données (DAO)           │
│   - Interfaces DAO                  │
│   - Implémentations DAO             │
└─────────────────────────────────────┘
           ↓
┌─────────────────────────────────────┐
│   Modèles (Domain Objects)          │
│   - Classes de domaine              │
└─────────────────────────────────────┘
           ↓
┌─────────────────────────────────────┐
│   Infrastructure (Base de données)  │
│   - MySQL Database                  │
└─────────────────────────────────────┘
```

## 2. Rôles et Permissions

### Administrateur
- Gestion complète des utilisateurs
- Visualisation des statistiques globales
- Génération de tous les types de rapports
- Configuration des paramètres système

### Responsable d'Approvisionnement
- Gestion des produits
- Gestion des fournisseurs
- Création et modification des bons de commande
- Consultation de l'état du stock
- Alertes de stock bas

### Responsable des Ventes
- Gestion des clients
- Création de bons de livraison
- Génération de factures
- Consultation de la disponibilité
- Historique des ventes

### Magasinier
- Validation des réceptions
- Enregistrement des entrées/sorties
- Gestion des bons de sortie
- Réalisation d'inventaires
- Suivi des mouvements

## 3. Flux de Processus

### Processus d'Approvisionnement
```
1. Responsable Appro crée Bon de Commande
2. Responsable Appro soumet au fournisseur
3. Magasinier réceptionne la commande
4. Magasinier valide et enregistre l'entrée stock
5. Magasinier confirme la réception
```

### Processus de Vente
```
1. Responsable Ventes crée Bon de Livraison
2. Responsable Ventes valide la disponibilité
3. Magasinier prépare la commande
4. Magasinier génère Bon de Sortie
5. Responsable Ventes génère Facture
6. Magasinier enregistre la sortie
```

### Processus d'Inventaire
```
1. Responsable Appro crée Inventaire
2. Magasinier réalise l'inventaire
3. Magasinier enregistre les quantités réelles
4. Système calcule les écarts
5. Magasinier valide les ajustements
```

## 4. Sécurité

### Authentification
- Authentification par email/mot de passe
- Hachage des mots de passe avec SHA-256 + salt
- Gestion de sessions avec SessionManager

### Autorisation
- Basée sur les rôles (RBAC)
- Vérification des permissions à chaque action
- Logs d'accès (à implémenter)

## 5. Gestion des Erreurs

### Exceptions Personnalisées
- `DatabaseException`: Erreurs de base de données
- `AuthenticationException`: Erreurs d'authentification
- `ValidationException`: Erreurs de validation
- `StockException`: Erreurs métier liées au stock

## 6. Utilitaires

### DatabaseConnection
- Singleton pour la connexion MySQL
- Utilise le pattern Singleton Thread-Safe

### SessionManager
- Gestion de la session utilisateur
- Stockage du contexte utilisateur

### ValidationUtils
- Validation des emails, téléphones, références

### PasswordHasher
- Hachage sécurisé des mots de passe
- Support du salt pour résister aux rainbow tables

### NavigationManager
- Gestion de la navigation entre les vues
- Support du chargement FXML dynamique

## 7. Configuration Base de Données

### Variables d'Environnement Supportées
```properties
db.url=jdbc:mysql://localhost:3306/stock_management
db.user=root
db.password=
db.driver=com.mysql.cj.jdbc.Driver
```

## 8. Guide de Développement

### Ajouter une nouvelle entité
1. Créer la classe modèle dans `model/`
2. Créer l'interface DAO dans `dao/interfaces/`
3. Implémenter le DAO dans `dao/implementation/`
4. Créer le service dans `service/`
5. Ajouter le contrôleur si nécessaire
6. Créer la vue FXML

### Ajouter un nouveau service
1. Créer la classe service
2. Implémenter les méthodes métier
3. Utiliser les DAOs pour accéder aux données
4. Gérer les exceptions appropriées

## 9. Tests

### Tests Unitaires
- Tests des DAOs
- Tests des services
- Tests des utilitaires

### Tests d'Intégration
- Tests des flux de processus
- Tests de la base de données

## 10. Performance

### Optimisations Implémentées
- Index sur les colonnes fréquemment interrogées
- Requêtes préparées pour éviter les injections SQL
- Connexion pool (à implémenter)
- Pagination (à implémenter)

## 11. Maintenance

### Logs
- À implémenter avec Log4j
- Niveaux: DEBUG, INFO, WARN, ERROR

### Monitoring
- À implémenter
- Suivi des opérations critiques

## 12. Migration de Données

### Script de Migration
- Utiliser le script `database/schema.sql`
- Importer les données initiales si nécessaire

## 13. Déploiement

### Packaging
```bash
mvn clean package
```

### Exécution
```bash
mvn javafx:run
```

### Distribution
- JAR exécutable avec toutes les dépendances
- Installer JRE 11+
- Installer MySQL Server

## 14. Roadmap Futures

- [ ] Intégration d'un système de cache
- [ ] Rapports PDF/Excel
- [ ] Export de données
- [ ] API REST
- [ ] Application mobile
- [ ] Dashboard temps réel
- [ ] Synchronisation multi-utilisateur

