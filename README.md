# Système de Gestion de Stock - Documentation

## Description
Ce projet est un système complet de gestion de stock développé en Java avec JavaFX pour l'interface graphique et MySQL pour la base de données.

## Architecture

### Structure du Projet
```
Projet-Académique-java/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/stock/
│   │   │       ├── model/          (Modèles de données)
│   │   │       ├── dao/            (Accès aux données)
│   │   │       ├── service/        (Logique métier)
│   │   │       ├── controller/     (Contrôleurs GUI)
│   │   │       ├── util/           (Utilitaires)
│   │   │       ├── exception/      (Exceptions personnalisées)
│   │   │       └── Main.java       (Point d'entrée)
│   │   └── resources/
│   │       ├── fxml/               (Fichiers d'interface)
│   │       ├── css/                (Feuilles de style)
│   │       ├── images/             (Images et icônes)
│   │       └── config/             (Fichiers de configuration)
│   └── test/
│       └── java/
├── database/
│   └── schema.sql                  (Schéma de base de données)
├── docs/
│   ├── DiagrammeClasses.png
│   ├── MCD.png
│   └── README.md
├── pom.xml                         (Configuration Maven)
├── .gitignore
└── README.md

```

## Dépendances Principales
- **JavaFX**: Interface graphique
- **MySQL Connector/J**: Connexion à la base de données
- **Maven**: Gestion des dépendances

## Installation

### 1. Prérequis
- Java 11 ou supérieur
- MySQL Server
- Maven 3.6 ou supérieur

### 2. Configuration de la Base de Données
```bash
# Créer la base de données
mysql -u root -p < database/schema.sql
```

### 3. Configuration de l'Application
Éditer `src/main/resources/config/database.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/stock_management
db.user=root
db.password=votre_mot_de_passe
```

### 4. Compilation et Exécution
```bash
# Compiler
mvn clean compile

# Exécuter
mvn javafx:run
```

## Fonctionnalités

### Pour les Administrateurs
- Gestion des utilisateurs
- Visualisation des statistiques
- Génération de rapports
- Configuration des paramètres

### Pour les Responsables d'Approvisionnement
- Gestion des produits
- Gestion des fournisseurs
- Création et suivi des bons de commande
- Consultation de l'état du stock
- Alertes de stock bas

### Pour les Responsables des Ventes
- Gestion des clients
- Création de bons de livraison
- Génération de factures
- Consulter la disponibilité des produits
- Historique des ventes

### Pour les Magasiniers
- Validation des réceptions
- Enregistrement des entrées/sorties de stock
- Gestion des bons de sortie
- Réalisation d'inventaires
- Suivi des mouvements de stock

## Architecture Technique

### Couches de l'Application
1. **Couche de Présentation**: Contrôleurs + FXML
2. **Couche Métier**: Services
3. **Couche d'Accès aux Données**: DAOs
4. **Couche de Modèle**: Classes de domaine
5. **Couche d'Infrastructure**: Utilitaires et Exceptions

### Patterns Utilisés
- **DAO Pattern**: Pour l'accès aux données
- **Service Pattern**: Pour la logique métier
- **Singleton Pattern**: Pour les connexions de base de données
- **MVC Pattern**: Pour l'architecture générale

## Base de Données

### Tables Principales
- `utilisateurs`: Authentification et gestion des rôles
- `produits`: Catalogue de produits
- `fournisseurs`: Données des fournisseurs
- `clients`: Données des clients
- `bon_commande`: Bons de commande
- `bon_livraison`: Bons de livraison
- `factures`: Factures clients
- `mouvement_stock`: Historique des mouvements
- `inventaires`: Gestion des inventaires

## Tests

Les tests unitaires se trouvent dans le dossier `src/test/java/`.

```bash
mvn test
```

## Auteurs et Contributions

Ce projet est développé par une équipe d'étudiants. Les contributions doivent suivre les directives de contribution du projet.

## Licence

Ce projet est sous licence MIT.

## Support

Pour toute question ou problème, merci de contacter l'équipe de développement.

