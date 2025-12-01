# Guide des Vues FXML et Styles CSS

## Organisation des Fichiers FXML

```
src/main/resources/fxml/
├── Login.fxml                    # Écran de connexion
├── MainLayout.fxml               # Menu principal
├── admin/
│   ├── DashboardAdmin.fxml       # Dashboard administrateur
│   ├── GestionUtilisateurs.fxml  # Gestion des utilisateurs
│   ├── Statistiques.fxml         # Visualisation des statistiques
│   └── Rapports.fxml             # Gestion des rapports
├── appro/
│   ├── DashboardAppro.fxml       # Dashboard approvisionnement
│   ├── GestionProduits.fxml      # Gestion des produits
│   ├── GestionFournisseurs.fxml  # Gestion des fournisseurs
│   └── GestionCommandes.fxml     # Gestion des bons de commande
├── ventes/
│   ├── DashboardVentes.fxml      # Dashboard ventes
│   ├── GestionClients.fxml       # Gestion des clients
│   ├── GestionLivraisons.fxml    # Gestion des livraisons
│   └── GestionFactures.fxml      # Gestion des factures
└── magasinier/
    ├── DashboardMagasinier.fxml  # Dashboard magasinier
    ├── Receptions.fxml           # Gestion des réceptions
    ├── Sorties.fxml              # Gestion des sorties
    └── Inventaires.fxml          # Gestion des inventaires
```

## Hiérarchie des Contrôleurs FXML

### LoginController
- Responsable: Authentification utilisateur
- Points d'entrée:
  - Email field: saisie de l'email
  - Password field: saisie du mot de passe
  - Login button: validation de la connexion

### MainController
- Responsable: Navigation principale
- Actions:
  - Déconnexion
  - Navigation vers les dashboards

### Dashboards Spécifiques
Chaque dashboard contient des boutons pour accéder aux sous-menus :

#### AdministrateurController
- Boutons:
  - Gestion Utilisateurs
  - Statistiques
  - Rapports
  - Configuration

#### ApprovisionnementController
- Boutons:
  - Gestion Produits
  - Gestion Fournisseurs
  - Création Bons Commande
  - Suivi Commandes

#### VentesController
- Boutons:
  - Gestion Clients
  - Bons de Livraison
  - Facturation
  - Historique Ventes

#### MagasinierController
- Boutons:
  - Réceptions
  - Sorties de Stock
  - Inventaires
  - Mouvements de Stock

## Organisation des Styles CSS

```
src/main/resources/css/
├── style.css      # Styles génériques
└── theme.css      # Thème personnalisé
```

### style.css
Contient les styles pour :
- Boutons (.button)
- Champs de texte (.text-field, .password-field)
- Labels (.label)
- Tableaux (.table-view)
- Containers (.vbox, .hbox)

### theme.css
Contient les définitions :
- Couleurs principales
- Boutons thématisés (primary, success, danger)
- Headers
- Animations

## Conventions de Nommage

### Fichiers FXML
- Format: `NomEcranCamelCase.fxml`
- Exemple: `GestionProduits.fxml`

### Identifiants fx:id
- Format: `nomTypeLowerCamelCase`
- Exemple: `emailField`, `loginButton`, `userTable`

### Contrôleurs
- Format: `NomEcranController.java`
- Doit être dans le même package que les modèles

### Événements
- Format: `handleNomAction()`
- Exemple: `handleLogin()`, `handleAddUser()`

## Implémentation de Nouvelles Vues

### Étape 1: Créer le fichier FXML
```xml
<?xml version="1.0" encoding="UTF-8"?>
<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>

<VBox spacing="10" style="-fx-padding: 20;" 
      xmlns="http://javafx.com/javafx" 
      xmlns:fx="http://javafx.com/fxml" 
      fx:controller="com.stock.controller.MaVueController">
    <!-- Contenu -->
</VBox>
```

### Étape 2: Créer le Contrôleur
```java
package com.stock.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MaVueController {
    @FXML
    private Button monBouton;
    
    @FXML
    public void initialize() {
        // Initialiser les composants
    }
    
    @FXML
    private void handleAction() {
        // Gérer l'action
    }
}
```

### Étape 3: Enregistrer la Navigation
```java
NavigationManager.getInstance().navigate("/fxml/path/MaVue.fxml", "Titre");
```

## Bonnes Pratiques

### Responsive Design
- Utiliser des layouts VBox/HBox plutôt que des coordonnées fixes
- Définir des largeurs/hauteurs minimales préférées
- Utiliser `style="-fx-padding"` pour les espaces

### Accessibilité
- Ajouter des labels descriptifs
- Utiliser des mnémoniques pour les boutons importants
- Gérer la navigation au clavier

### Performance
- Ne pas surcharger les vues avec trop d'éléments
- Utiliser la pagination pour les grandes listes
- Charger les données de manière asynchrone si possible

### Maintenabilité
- Respecter la séparation des responsabilités
- Un contrôleur par vue FXML
- Utiliser les services pour la logique métier
- Documenter les interactions complexes

## Troubleshooting

### Problème: Vue ne charge pas
- Vérifier le chemin dans `navigate()`
- Vérifier que le fx:controller existe et est dans le bon package
- Vérifier l'absence d'erreurs dans le console

### Problème: Bouton/Champ non accessible depuis le contrôleur
- Vérifier que fx:id est défini dans FXML
- Vérifier que @FXML est appliqué à la variable
- Vérifier que le nom correspond exactement

### Problème: Styles CSS non appliqués
- Vérifier le chemin du fichier CSS
- Vérifier que les sélecteurs CSS sont corrects
- Vérifier que les styles ne sont pas surchargés

## Améliorations Futures

- [ ] Ajouter des animations de transition
- [ ] Implémenter des formulaires réactifs
- [ ] Ajouter des validations en temps réel
- [ ] Créer des composants réutilisables
- [ ] Améliorer le responsive design

