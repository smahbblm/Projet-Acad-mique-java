# Guide Utilisateur - Système de Gestion de Stock

##  Table des matières
1. [Introduction](#introduction)
2. [Installation](#installation)
3. [Connexion](#connexion)
4. [Fonctionnalités par rôle](#fonctionnalités-par-rôle)
5. [Support](#support)

---

##  Introduction

Bienvenue dans le Système de Gestion de Stock. Cette application vous permet de gérer efficacement vos stocks, commandes, livraisons et factures.

---

##  Installation

### Prérequis
- Java 21 ou supérieur installé sur votre ordinateur
- MySQL Server installé et en cours d'exécution

### Étapes d'installation

1. **Télécharger l'application**
   - Récupérez le dossier du projet

2. **Configurer la base de données**
   ```bash
   # Ouvrir MySQL et exécuter le fichier schema.sql
   mysql -u root -p < database/schema.sql
   ```

3. **Configurer la connexion**
   - Ouvrir le fichier : `src/main/resources/config/database.properties`
   - Modifier avec vos informations :
   ```properties
   db.url=jdbc:mysql://localhost:3306/stock_management
   db.user=votre_utilisateur
   db.password=votre_mot_de_passe
   ```

4. **Lancer l'application**
   ```bash
   mvn javafx:run
   ```
   Ou double-cliquer sur le fichier JAR si fourni.

---

##  Connexion

1. Lancer l'application
2. Entrer votre **email** et **mot de passe**
3. Cliquer sur **Se connecter**

> **Note** : Contactez votre administrateur pour obtenir vos identifiants.

---

## 👥 Fonctionnalités par rôle

### 🔧 Administrateur

**Accès au tableau de bord :**
- Vue d'ensemble du système
- Statistiques globales

**Gestion des utilisateurs :**
- Créer de nouveaux comptes
- Modifier les informations utilisateurs
- Désactiver/Activer des comptes
- Gérer les rôles et permissions

**Statistiques et Analyses :**
- Consulter les statistiques globales
- Analyser les mouvements de stock
- Visualiser les graphiques de performance

**Rapports :**
- Générer des rapports personnalisés
- Consulter l'historique des rapports
- Exporter les données

**Système :**
- Gérer la base de données
- Sauvegarder les données
- Configurer les paramètres système

---

###  Responsable d'Approvisionnement

**Gestion des produits :**
- Ajouter de nouveaux produits
- Modifier les informations produits
- Consulter le catalogue

**Gestion des fournisseurs :**
- Ajouter des fournisseurs
- Modifier les coordonnées
- Consulter la liste des fournisseurs

**Bons de commande :**
- Créer des bons de commande
- Suivre l'état des commandes
- Valider les réceptions

**Alertes stock :**
- Recevoir des notifications de stock bas
- Consulter les seuils d'alerte
- Planifier les réapprovisionnements

---

###  Responsable des Ventes

**Gestion des clients :**
- Ajouter de nouveaux clients
- Modifier les informations clients
- Consulter la liste des clients

**Bons de livraison :**
- Créer des bons de livraison
- Suivre les livraisons
- Imprimer les bons

**Factures :**
- Générer des factures
- Consulter l'historique des factures
- Exporter les factures (PDF)

**Disponibilité produits et Historique de vents :**
- Vérifier le stock disponible
- Consulter les prix
- Historique des ventes

---

###  Magasinier

**Réceptions :**
- Valider les réceptions de marchandises
- Enregistrer les entrées de stock
- Vérifier les quantités

**Mouvements de stock :**
- Enregistrer les entrées
- Enregistrer les sorties
- Consulter l'historique des mouvements

**Bons de sortie :**
- Créer des bons de sortie
- Valider les sorties
- Imprimer les bons

**Inventaires :**
- Réaliser des inventaires
- Corriger les écarts
- Générer des rapports d'inventaire

---

##  Support

### Problèmes courants

**Je ne peux pas me connecter**
- Vérifiez votre email et mot de passe
- Contactez votre administrateur si le compte est désactivé

**L'application ne démarre pas**
- Vérifiez que Java est installé : `java -version`
- Vérifiez que MySQL est en cours d'exécution

**Erreur de connexion à la base de données**
- Vérifiez le fichier `database.properties  et le fichier DatabaseConnection  qui se trouve dans le package com.stock.util `
- Assurez-vous que MySQL est démarré
- Vérifiez vos identifiants MySQL

### utilisateurs par default

Administrateur: 
- **Email** :smahan@gmail.com
- **mot de pass** : yfurd6sdrc68s
- 
Responsable de vents:
- **Email** : anas.ventes@stock.ma
- **Téléphone** : pass123
- 
Magasinier:
- **Email** : karim.magasin@stock.ma
- **mot de pass**:pass123
- 
- RESPONSABLE_APPROVISIONNEMENT:
- **Email**:sami.appro@stock.ma
- **mdp**:pass123
---

## 📝 Conseils d'utilisation

✅ **Bonnes pratiques :**
- Déconnectez-vous après chaque session
- Sauvegardez régulièrement vos données
- Vérifiez les stocks avant de créer des commandes
- Mettez à jour les informations en temps réel

⚠️ **À éviter :**
- Ne partagez pas vos identifiants
- Ne supprimez pas de données sans confirmation
- N'oubliez pas de valider vos saisies

---

## 📱 Navigation

- Utilisez le **menu latéral** pour accéder aux différentes sections
- Cliquez sur **Déconnexion** pour quitter l'application
- Les sous-menus s'affichent au survol de la souris

---

**Version** : 1.0  
**Dernière mise à jour** : 28/122025

---

*Merci d'utiliser notre Système de Gestion de Stock !* 🚀
