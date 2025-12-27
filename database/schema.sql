-- Création de la base de données
CREATE DATABASE IF NOT EXISTS stock_management;
USE stock_management;

-- Table des utilisateurs
CREATE TABLE utilisateurs (
    idUtilisateur INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    motDePasse VARCHAR(255) NOT NULL,
    role ENUM('Administrateur', 'Responsable_Approvisionnement', 'Responsable_Ventes', 'Magasinier') NOT NULL,
    dateCreation DATE NOT NULL,
    actif BOOLEAN DEFAULT TRUE,
    INDEX idx_email (email)
);

-- Table des produits
CREATE TABLE produits (
    idProduit INT PRIMARY KEY AUTO_INCREMENT,
    reference VARCHAR(50) UNIQUE NOT NULL,
    designation VARCHAR(200) NOT NULL,
    description TEXT,
    prixAchat FLOAT NOT NULL,
    prixVente FLOAT NOT NULL,
    quantiteStock INT NOT NULL DEFAULT 0,
    seuilMin INT NOT NULL,
    seuilMax INT NOT NULL,
    categorie VARCHAR(100),
    dateAjout DATE NOT NULL,
    INDEX idx_reference (reference),
    INDEX idx_categorie (categorie)
);

-- Table des fournisseurs
CREATE TABLE fournisseurs (
    idFournisseur INT PRIMARY KEY AUTO_INCREMENT,
    raisonSociale VARCHAR(200) NOT NULL,
    adresse VARCHAR(300),
    telephone VARCHAR(20),
    email VARCHAR(150),
    contact VARCHAR(100),
    conditions TEXT,
    INDEX idx_email (email)
);

-- Table des clients
CREATE TABLE clients (
    idClient INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100),
    prenom VARCHAR(100),
    raisonSociale VARCHAR(200),
    adresse VARCHAR(300),
    telephone VARCHAR(20),
    email VARCHAR(150),
    dateInscription DATE NOT NULL,
    INDEX idx_email (email)
);

-- Table des bons de commande
CREATE TABLE bon_commande (
    idBonCommande INT PRIMARY KEY AUTO_INCREMENT,
    numero VARCHAR(50) UNIQUE NOT NULL,
    dateCommande DATE NOT NULL,
    dateLivraisonPrevue DATE,
    statut ENUM('BROUILLON', 'EN_ATTENTE', 'VALIDEE', 'TRANSMISE', 'RECU', 'ANNULEE') DEFAULT 'BROUILLON',
    montantTotal FLOAT DEFAULT 0,
    observations TEXT,
    idFournisseur INT NOT NULL,
    FOREIGN KEY (idFournisseur) REFERENCES fournisseurs(idFournisseur),
    INDEX idx_numero (numero),
    INDEX idx_statut (statut)
);

-- Table des lignes de commande
CREATE TABLE ligne_commande (
    idLigne INT PRIMARY KEY AUTO_INCREMENT,
    idBonCommande INT NOT NULL,
    idProduit INT NOT NULL,
    quantite INT NOT NULL,
    prixUnitaire FLOAT NOT NULL,
    sousTotal FLOAT,
    FOREIGN KEY (idBonCommande) REFERENCES bon_commande(idBonCommande),
    FOREIGN KEY (idProduit) REFERENCES produits(idProduit)
);

-- Table des bons de livraison
CREATE TABLE bon_livraison (
    idBonLivraison INT PRIMARY KEY AUTO_INCREMENT,
    numero VARCHAR(50) UNIQUE NOT NULL,
    dateLivraison DATE NOT NULL,
    statut ENUM('BROUILLON', 'EN_ATTENTE', 'VALIDEE', 'TRANSMISE', 'LIVREE', 'ANNULEE') DEFAULT 'BROUILLON',
    adresseLivraison VARCHAR(300),
    observations TEXT,
    idClient INT NOT NULL,
    idBonSortie INT,
    idFacture INT,
    FOREIGN KEY (idClient) REFERENCES clients(idClient),
    INDEX idx_numero (numero),
    INDEX idx_statut (statut)
);

-- Table des lignes de livraison
CREATE TABLE ligne_livraison (
    idLigne INT PRIMARY KEY AUTO_INCREMENT,
    idBonLivraison INT NOT NULL,
    idProduit INT NOT NULL,
    quantite INT NOT NULL,
    prixUnitaire FLOAT NOT NULL,
    FOREIGN KEY (idBonLivraison) REFERENCES bon_livraison(idBonLivraison),
    FOREIGN KEY (idProduit) REFERENCES produits(idProduit)
);

-- Table des factures
CREATE TABLE factures (
    idFacture INT PRIMARY KEY AUTO_INCREMENT,
    numero VARCHAR(50) UNIQUE NOT NULL,
    dateFacture DATE NOT NULL,
    dateEcheance DATE,
    montantHT FLOAT NOT NULL,
    montantTVA FLOAT,
    montantTTC FLOAT,
    statut ENUM('BROUILLON', 'GENEREE', 'PAYEE', 'ANNULEE') DEFAULT 'BROUILLON',
    idClient INT NOT NULL,
    FOREIGN KEY (idClient) REFERENCES clients(idClient),
    INDEX idx_numero (numero),
    INDEX idx_statut (statut)
);

-- Table des bons de sortie
CREATE TABLE bon_sortie (
    idBonSortie INT PRIMARY KEY AUTO_INCREMENT,
    numero VARCHAR(50) UNIQUE NOT NULL,
    dateSortie DATE NOT NULL,
    statut ENUM('BROUILLON', 'GENEREE', 'VALIDEE', 'ENREGISTREE', 'ANNULEE') DEFAULT 'BROUILLON',
    INDEX idx_numero (numero),
    INDEX idx_statut (statut)
);

-- Table des lignes de sortie
CREATE TABLE ligne_sortie (
    idLigne INT PRIMARY KEY AUTO_INCREMENT,
    idBonSortie INT NOT NULL,
    idProduit INT NOT NULL,
    quantite INT NOT NULL,
    FOREIGN KEY (idBonSortie) REFERENCES bon_sortie(idBonSortie),
    FOREIGN KEY (idProduit) REFERENCES produits(idProduit)
);

-- Table des mouvements de stock
CREATE TABLE mouvement_stock (
    idMouvement INT PRIMARY KEY AUTO_INCREMENT,
    dateMouvement DATE NOT NULL,
    typeMouvement ENUM('ENTREE', 'SORTIE', 'AJUSTEMENT', 'INVENTAIRE') NOT NULL,
    quantite INT NOT NULL,
    stockAvant INT,
    stockApres INT,
    reference VARCHAR(100),
    idProduit INT NOT NULL,
    idUtilisateur INT,
    FOREIGN KEY (idProduit) REFERENCES produits(idProduit),
    FOREIGN KEY (idUtilisateur) REFERENCES utilisateurs(idUtilisateur),
    INDEX idx_typeMouvement (typeMouvement),
    INDEX idx_dateMouvement (dateMouvement)
);

-- Table des inventaires
CREATE TABLE inventaires (
    idInventaire INT PRIMARY KEY AUTO_INCREMENT,
    dateInventaire DATE NOT NULL,
    statut ENUM('BROUILLON', 'EN_COURS', 'VALIDEE', 'ANNULEE') DEFAULT 'BROUILLON',
    observations TEXT,
    INDEX idx_statut (statut)
);

-- Table des lignes d'inventaire
CREATE TABLE ligne_inventaire (
    idLigne INT PRIMARY KEY AUTO_INCREMENT,
    idInventaire INT NOT NULL,
    idProduit INT NOT NULL,
    quantiteTheorique INT NOT NULL,
    quantiteReelle INT,
    ecart INT,
    FOREIGN KEY (idInventaire) REFERENCES inventaires(idInventaire),
    FOREIGN KEY (idProduit) REFERENCES produits(idProduit)
);

-- Table des rapports
CREATE TABLE rapports (
    idRapport INT PRIMARY KEY AUTO_INCREMENT,
    type VARCHAR(50) NOT NULL,
    dateGeneration DATE NOT NULL,
    dateDe DATE,
    dateA DATE,
    contenu LONGTEXT,
    INDEX idx_type (type)
);

ALTER TABLE fournisseurs
    ADD COLUMN actif BOOLEAN NOT NULL DEFAULT TRUE;

-- Insertion de données de test
-- Utilisateurs de test
INSERT INTO utilisateurs (nom, prenom, email, motDePasse, role, dateCreation, actif)
VALUES
    ('Admin', 'Test', 'admin@test.com', 'motdepasse123', 'Administrateur', CURDATE(), TRUE),
    ('Appro', 'Test', 'appro@test.com', 'motdepasse123', 'Responsable_Approvisionnement', CURDATE(), TRUE),
    ('Ventes', 'Test', 'ventes@test.com', 'motdepasse123', 'Responsable_Ventes', CURDATE(), TRUE),
    ('Magasin', 'Test', 'magasin@test.com', 'motdepasse123', 'Magasinier', CURDATE(), TRUE);


-- Fournisseurs de test
INSERT INTO fournisseurs (raisonSociale, adresse, telephone, email, contact, conditions, actif)
VALUES
    ('Fournisseur A', '123 Rue A, Ville', '0102030405', 'fournisseurA@test.com', 'Alice', 'Paiement à 30 jours', TRUE),
    ('Fournisseur B', '456 Rue B, Ville', '0607080910', 'fournisseurB@test.com', 'Bob', 'Paiement à 15 jours', TRUE);

-- Clients de test
INSERT INTO clients (nom, prenom, raisonSociale, adresse, telephone, email, dateInscription)
VALUES
    ('Client', 'Test', 'Client SARL', '789 Rue C, Ville', '0112233445', 'client@test.com', CURDATE()),
    ('Client2', 'Test', 'Client2 SARL', '321 Rue D, Ville', '0556677889', 'client2@test.com', CURDATE());

-- Produits
INSERT INTO produits (reference, designation, description, prixAchat, prixVente, quantiteStock, seuilMin, seuilMax, categorie, dateAjout)
VALUES
    ('PROD001', 'Produit 1', 'Description produit 1', 10.0, 15.0, 50, 10, 100, 'Catégorie A', CURDATE()),
    ('PROD002', 'Produit 2', 'Description produit 2', 20.0, 30.0, 30, 5, 50, 'Catégorie B', CURDATE()),
    ('PROD003', 'Produit 3', 'Description produit 3', 5.0, 8.0, 100, 20, 200, 'Catégorie A', CURDATE());

-- Mouvements de stock
INSERT INTO mouvement_stock (dateMouvement, typeMouvement, quantite, stockAvant, stockApres, reference, idProduit, idUtilisateur)
VALUES
    (CURDATE(), 'ENTREE', 50, 0, 50, 'ENT001', 1, 2),
    (CURDATE(), 'SORTIE', 10, 50, 40, 'SORT001', 1, 4),
    (CURDATE(), 'ENTREE', 30, 0, 30, 'ENT002', 2, 2),
    (CURDATE(), 'SORTIE', 5, 30, 25, 'SORT002', 2, 4);

-- Bons de commande
INSERT INTO bon_commande (numero, dateCommande, dateLivraisonPrevue, statut, montantTotal, observations, idFournisseur)
VALUES
    ('BC001', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 7 DAY), 'BROUILLON', 0, 'Commande test 1', 1),
    ('BC002', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 10 DAY), 'EN_ATTENTE', 0, 'Commande test 2', 2);

-- Lignes de commande de test
INSERT INTO ligne_commande (idBonCommande, idProduit, quantite, prixUnitaire, sousTotal)
VALUES
    (1, 1, 20, 10.0, 200.0),
    (1, 2, 10, 20.0, 200.0),
    (2, 3, 50, 5.0, 250.0);

-- rapports de test
INSERT INTO rapports (type, dateGeneration, dateDe, dateA, contenu)
VALUES
    (
        'STOCK_GLOBAL',
        CURDATE(),
        '2025-01-01',
        CURDATE(),
        'Rapport global des stocks :
        - Total produits : 3
        - Quantité totale en stock : 175
        - Produits en alerte : 1
        - Produits au-dessus du seuil max : 0'
    ),
    (
        'MOUVEMENTS_STOCK',
        CURDATE(),
        '2025-12-01',
        CURDATE(),
        'Rapport des mouvements de stock :
        - Total entrées : 80 unités
        - Total sorties : 15 unités
        - Stock final : 65 unités
        - Mouvements validés : oui'
    ),
    (
        'UTILISATEURS',
        CURDATE(),
        '2025-01-01',
        CURDATE(),
        'Rapport utilisateurs :
        - Total utilisateurs : 4
        - Administrateurs : 1
        - Responsable Approvisionnement : 1
        - Responsable Ventes : 1
        - Magasiniers : 1
        - Comptes actifs : 4'
    ),
    (
        'VENTES',
        CURDATE(),
        '2025-12-01',
        CURDATE(),
        'Rapport des ventes :
        - Clients servis : 2
        - Produits vendus : 15
        - Montant total HT : 375.00
        - TVA : 75.00
        - Montant TTC : 450.00'
    ),
    (
        'ALERTES_STOCK',
        CURDATE(),
        '2025-12-01',
        CURDATE(),
        'Rapport des alertes stock :
        - Produits sous seuil minimum : 1
        - Produits en rupture : 0
        - Actions recommandées : réapprovisionnement'
    );

