# Guide de Configuration de la Base de Données

## Installation de MySQL

### Windows
1. Télécharger MySQL Server depuis https://dev.mysql.com/downloads/mysql/
2. Exécuter l'installateur
3. Suivre les étapes d'installation
4. Configurer le service MySQL
5. Définir le mot de passe root

### Linux (Ubuntu/Debian)
```bash
sudo apt-get install mysql-server
sudo mysql_secure_installation
```

### macOS
```bash
brew install mysql
brew services start mysql
mysql_secure_installation
```

## Création de la Base de Données

### Méthode 1: Utiliser le script SQL fourni
```bash
mysql -u root -p < database/schema.sql
```

### Méthode 2: Manuellement
```sql
-- Créer la base de données
CREATE DATABASE stock_management;

-- Sélectionner la base
USE stock_management;

-- Exécuter les scripts de création des tables
-- Voir database/schema.sql
```

## Configuration de l'Application

### Fichier database.properties
Éditer `src/main/resources/config/database.properties`:

```properties
# Configuration MySQL
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/stock_management
db.user=root
db.password=latifa19.
db.port=3306

# Configuration de l'application
app.name=Système de Gestion de Stock
app.version=1.0.0
app.language=fr_FR
```

## Structure de la Base de Données

### Tables Principales

#### utilisateurs
```sql
- idUtilisateur (INT, PK, AUTO_INCREMENT)
- nom (VARCHAR)
- prenom (VARCHAR)
- email (VARCHAR, UNIQUE)
- motDePasse (VARCHAR)
- role (ENUM)
- dateCreation (DATE)
- actif (BOOLEAN)
```

#### produits
```sql
- idProduit (INT, PK, AUTO_INCREMENT)
- reference (VARCHAR, UNIQUE)
- designation (VARCHAR)
- description (TEXT)
- prixAchat (FLOAT)
- prixVente (FLOAT)
- quantiteStock (INT)
- seuilMin (INT)
- seuilMax (INT)
- categorie (VARCHAR)
- dateAjout (DATE)
```

#### fournisseurs
```sql
- idFournisseur (INT, PK, AUTO_INCREMENT)
- raisonSociale (VARCHAR)
- adresse (VARCHAR)
- telephone (VARCHAR)
- email (VARCHAR)
- contact (VARCHAR)
- conditions (TEXT)
```

#### clients
```sql
- idClient (INT, PK, AUTO_INCREMENT)
- nom (VARCHAR)
- prenom (VARCHAR)
- raisonSociale (VARCHAR)
- adresse (VARCHAR)
- telephone (VARCHAR)
- email (VARCHAR)
- dateInscription (DATE)
```

#### bon_commande
```sql
- idBonCommande (INT, PK, AUTO_INCREMENT)
- numero (VARCHAR, UNIQUE)
- dateCommande (DATE)
- dateLivraisonPrevue (DATE)
- statut (ENUM)
- montantTotal (FLOAT)
- observations (TEXT)
- idFournisseur (INT, FK)
```

#### bon_livraison
```sql
- idBonLivraison (INT, PK, AUTO_INCREMENT)
- numero (VARCHAR, UNIQUE)
- dateLivraison (DATE)
- statut (ENUM)
- adresseLivraison (VARCHAR)
- observations (TEXT)
- idClient (INT, FK)
- idBonSortie (INT, FK)
- idFacture (INT, FK)
```

#### factures
```sql
- idFacture (INT, PK, AUTO_INCREMENT)
- numero (VARCHAR, UNIQUE)
- dateFacture (DATE)
- dateEcheance (DATE)
- montantHT (FLOAT)
- montantTVA (FLOAT)
- montantTTC (FLOAT)
- statut (ENUM)
- idClient (INT, FK)
```

#### bon_sortie
```sql
- idBonSortie (INT, PK, AUTO_INCREMENT)
- numero (VARCHAR, UNIQUE)
- dateSortie (DATE)
- statut (ENUM)
```

#### mouvement_stock
```sql
- idMouvement (INT, PK, AUTO_INCREMENT)
- dateMouvement (DATE)
- typeMouvement (ENUM)
- quantite (INT)
- stockAvant (INT)
- stockApres (INT)
- reference (VARCHAR)
- idProduit (INT, FK)
- idUtilisateur (INT, FK)
```

#### inventaires
```sql
- idInventaire (INT, PK, AUTO_INCREMENT)
- dateInventaire (DATE)
- statut (ENUM)
- observations (TEXT)
```

#### rapports
```sql
- idRapport (INT, PK, AUTO_INCREMENT)
- type (VARCHAR)
- dateGeneration (DATE)
- dateDe (DATE)
- dateA (DATE)
- contenu (LONGTEXT)
```

## Données de Test

### Utilisateurs par défaut
```
Admin:
  Email: admin@test.com
  Mot de passe: motdepasse123
  Rôle: ADMINISTRATEUR

Responsable Approvisionnement:
  Email: appro@test.com
  Mot de passe: motdepasse123
  Rôle: RESPONSABLE_APPROVISIONNEMENT

Responsable Ventes:
  Email: ventes@test.com
  Mot de passe: motdepasse123
  Rôle: RESPONSABLE_VENTES

Magasinier:
  Email: magasin@test.com
  Mot de passe: motdepasse123
  Rôle: MAGASINIER
```

## Sauvegarde et Restauration

### Sauvegarder la base de données
```bash
# Sauvegarde complète
mysqldump -u root -p stock_management > backup_stock_management.sql

# Sauvegarde compressée
mysqldump -u root -p stock_management | gzip > backup_stock_management.sql.gz
```

### Restaurer la base de données
```bash
# Restauration simple
mysql -u root -p stock_management < backup_stock_management.sql

# Restauration depuis une sauvegarde compressée
gunzip < backup_stock_management.sql.gz | mysql -u root -p stock_management
```

## Maintenance de la Base de Données

### Optimisation des tables
```sql
USE stock_management;
OPTIMIZE TABLE utilisateurs;
OPTIMIZE TABLE produits;
OPTIMIZE TABLE bon_commande;
OPTIMIZE TABLE bon_livraison;
OPTIMIZE TABLE factures;
OPTIMIZE TABLE mouvement_stock;
OPTIMIZE TABLE inventaires;
```

### Vérification des tables
```sql
CHECK TABLE utilisateurs;
CHECK TABLE produits;
CHECK TABLE bon_commande;
```

### Réparation des tables corrompues
```sql
REPAIR TABLE utilisateurs;
```

## Requêtes Courantes

### Consulter les stocks bas
```sql
SELECT * FROM produits 
WHERE quantiteStock < seuilMin;
```

### Historique des mouvements d'un produit
```sql
SELECT * FROM mouvement_stock 
WHERE idProduit = ? 
ORDER BY dateMouvement DESC;
```

### Factures impayées
```sql
SELECT * FROM factures 
WHERE statut IN ('GENEREE')
ORDER BY dateEcheance ASC;
```

### Chiffre d'affaires par période
```sql
SELECT 
    DATE_FORMAT(dateFacture, '%Y-%m') AS periode,
    SUM(montantTTC) AS chiffre_affaires
FROM factures
WHERE statut = 'PAYEE'
GROUP BY DATE_FORMAT(dateFacture, '%Y-%m')
ORDER BY dateFacture DESC;
```

## Performance et Indexes

### Indexes actuellement définis
```sql
INDEX idx_email ON utilisateurs(email);
INDEX idx_reference ON produits(reference);
INDEX idx_categorie ON produits(categorie);
INDEX idx_numero ON bon_commande(numero);
INDEX idx_statut ON bon_commande(statut);
INDEX idx_typeMouvement ON mouvement_stock(typeMouvement);
INDEX idx_dateMouvement ON mouvement_stock(dateMouvement);
```

### Recommandations supplémentaires
- Ajouter des indexes sur les colonnes de filtrage fréquent
- Monitorer les performances des requêtes
- Implémenter une pagination pour les grosses listes

## Troubleshooting

### Problème: Connexion refusée
- Vérifier que MySQL Server est lancé
- Vérifier les identifiants dans database.properties
- Vérifier le firewall

### Problème: Impossible de créer la base de données
- Vérifier les permissions de l'utilisateur
- Utiliser un utilisateur avec les privilèges d'administration
- Vérifier que le port MySQL est accessible

### Problème: Tables introuvables
- Vérifier que le script schema.sql a été exécuté
- Vérifier que la base de données est sélectionnée (USE stock_management)
- Vérifier les erreurs dans le script d'exécution

## Améliorations Futures

- [ ] Implémenter des triggers pour l'audit
- [ ] Ajouter des vues pour faciliter les requêtes
- [ ] Configurer la réplication MySQL
- [ ] Mettre en place des procédures stockées
- [ ] Implémenter des partitions pour les grosses tables

