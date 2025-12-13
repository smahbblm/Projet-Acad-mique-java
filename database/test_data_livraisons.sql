-- Script pour ajouter des données de test dans les tables de livraison
USE stock_management;

-- Insertion de bons de livraison de test
INSERT INTO bon_livraison (numero, dateLivraison, statut, adresseLivraison, observations, idClient) VALUES
('BL-2024-001', '2024-01-15', 'LIVREE', '123 Rue de la Paix, Paris', 'Livraison rapide', 1),
('BL-2024-002', '2024-01-20', 'LIVREE', '456 Avenue des Champs, Lyon', 'Livraison standard', 2),
('BL-2024-003', '2024-02-05', 'LIVREE', '789 Boulevard Central, Marseille', 'Livraison express', 3),
('BL-2024-004', '2024-02-10', 'LIVREE', '321 Rue du Commerce, Toulouse', 'Livraison normale', 1),
('BL-2024-005', '2024-02-15', 'LIVREE', '654 Place de la République, Nice', 'Livraison urgente', 2);

-- Insertion de lignes de livraison de test
INSERT INTO ligne_livraison (idBonLivraison, idProduit, quantite, prixUnitaire) VALUES
-- Pour BL-2024-001
(1, 1, 2, 800.00),  -- 2 Ordinateurs Dell à 800€
(1, 2, 5, 25.00),   -- 5 Souris Logitech à 25€

-- Pour BL-2024-002
(2, 3, 3, 45.00),   -- 3 Claviers à 45€
(2, 1, 1, 800.00),  -- 1 Ordinateur Dell à 800€

-- Pour BL-2024-003
(3, 2, 10, 25.00),  -- 10 Souris Logitech à 25€
(3, 3, 2, 45.00),   -- 2 Claviers à 45€

-- Pour BL-2024-004
(4, 1, 1, 800.00),  -- 1 Ordinateur Dell à 800€
(4, 2, 3, 25.00),   -- 3 Souris Logitech à 25€
(4, 3, 1, 45.00),   -- 1 Clavier à 45€

-- Pour BL-2024-005
(5, 2, 8, 25.00),   -- 8 Souris Logitech à 25€
(5, 3, 4, 45.00);   -- 4 Claviers à 45€

-- Vérification des données insérées
SELECT 'Bons de livraison créés:' as Info;
SELECT * FROM bon_livraison;

SELECT 'Lignes de livraison créées:' as Info;
SELECT * FROM ligne_livraison;

SELECT 'Historique des ventes (test):' as Info;
SELECT bl.numero, bl.dateLivraison, 
       CONCAT(c.nom, ' ', COALESCE(c.prenom, '')) as client,
       p.designation as produit,
       ll.quantite,
       ll.prixUnitaire,
       (ll.quantite * ll.prixUnitaire) as montantTotal
FROM bon_livraison bl 
JOIN clients c ON bl.idClient = c.idClient 
JOIN ligne_livraison ll ON bl.idBonLivraison = ll.idBonLivraison 
JOIN produits p ON ll.idProduit = p.idProduit 
ORDER BY bl.dateLivraison DESC;