package com.stock.service;

public class AlertService {
    private  CommandeService commandeService;
    private ProduitService produitService;
    private InventaireService inventorService;
    public AlertService() throws Exception{
        commandeService=new CommandeService();
        produitService=new ProduitService();
        inventorService=new InventaireService();
    }
    public int getNombreTotalAlertes() throws Exception {
        int total = 0;

        // Stock critique
        total += produitService.getProduitsStockCritique().size();

        // Écarts inventaire
        total += inventorService.getEcartsInventaire().size();

        // Bons en attente

        total +=  commandeService.getBonsEntreeEnAttenteValidation().size();
        // Péremption proche (si tu l'ajoutes)
        // total += produitService.getProduitPeremptionProche(7).size();

        return total;
    }
    public int getNombreCommandeENAttente_Validation() throws Exception{
        int total=0;
        total +=  commandeService.getBonsEntreeEnAttenteValidation().size();

        return total;
    }
    public int getNombreAlertsCritique() throws Exception{
        int total = 0;

        // Stock critique
        total += produitService.getProduitsStockCritique().size();

        // Écarts inventaire
        total += inventorService.getEcartsInventaire().size();
        return total;
    }

}
