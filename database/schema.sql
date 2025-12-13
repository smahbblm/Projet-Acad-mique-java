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

-- Insertion de données de test
-- Utilisateurs de test
INSERT INTO utilisateurs (nom, prenom, email, motDePasse, role, dateCreation, actif)
VALUES
('Admin', 'Test', 'admin@test.com', 'motdepasse123', 'Administrateur', NOW(), TRUE),
('Appro', 'Test', 'appro@test.com', 'motdepasse123', 'Responsable_Approvisionnement', NOW(), TRUE),
('Ventes', 'Test', 'ventes@test.com', 'motdepasse123', 'Responsable_Ventes', NOW(), TRUE),
('Magasin', 'Test', 'magasin@test.com', 'motdepasse123', 'Magasinier', NOW(), TRUE);
ALTER TABLE fournisseurs
    ADD COLUMN actif BOOLEAN NOT NULL DEFAULT TRUE;