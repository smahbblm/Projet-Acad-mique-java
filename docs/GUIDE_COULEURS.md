# 🎨 Guide des Couleurs - Système de Gestion de Stock

## Palette de Couleurs Principale

### 🔵 Couleur Primaire (Bleu)
- **Principal**: `#3b82f6` - Boutons principaux, liens, accents
- **Foncé**: `#2563eb` - Hover, états actifs
- **Clair**: `#60a5fa` - Backgrounds légers
- **Très clair**: `#dbeafe` - Highlights, sélections

### 🟣 Couleur Secondaire (Violet)
- **Principal**: `#8b5cf6` - Éléments secondaires
- **Foncé**: `#7c3aed` - Hover
- **Clair**: `#a78bfa` - Backgrounds
- **Très clair**: `#e0e7ff` - Highlights

### 🟢 Couleur Accent (Vert)
- **Principal**: `#10b981` - Succès, validations
- **Foncé**: `#059669` - Hover
- **Clair**: `#34d399` - Backgrounds
- **Très clair**: `#d1fae5` - Highlights

## Couleurs de Fond

- **Fond principal**: `#ffffff` (Blanc)
- **Fond secondaire**: `#f8f9fa` (Gris très clair)
- **Fond tertiaire**: `#f1f5f9` (Gris clair)

## Couleurs de Texte

- **Texte principal**: `#0f172a` (Noir doux)
- **Texte secondaire**: `#64748b` (Gris moyen)
- **Texte tertiaire**: `#94a3b8` (Gris clair)

## Couleurs d'État

### ✅ Succès
- **Couleur**: `#10b981`
- **Background**: `#d1fae5`
- **Usage**: Validations, confirmations, statuts positifs

### ⚠️ Avertissement
- **Couleur**: `#f59e0b`
- **Background**: `#fef3c7`
- **Usage**: Alertes, stocks faibles, actions à faire

### ❌ Erreur
- **Couleur**: `#ef4444`
- **Background**: `#fee2e2`
- **Usage**: Erreurs, suppressions, statuts négatifs

### ℹ️ Information
- **Couleur**: `#3b82f6`
- **Background**: `#dbeafe`
- **Usage**: Informations, notifications

## Bordures et Ombres

- **Bordure**: `#e2e8f0`
- **Rayon**: `12px` (cartes), `8px` (inputs), `10px` (boutons)
- **Ombre légère**: `dropshadow(gaussian, rgba(0,0,0,0.04), 8, 0, 0, 2)`
- **Ombre moyenne**: `dropshadow(gaussian, rgba(0,0,0,0.08), 12, 0, 0, 4)`

## Sidebar (Menu latéral)

- **Background**: `#1e293b` (Bleu-gris foncé)
- **Texte**: `#cbd5e1` (Gris clair)
- **Texte hover**: `#ffffff` (Blanc)
- **Bouton actif**: `#3b82f6` (Bleu primaire)

## Application des Couleurs par Section

### 📊 Dashboard
- KPIs: Dégradés avec couleurs primaires
- Graphiques: Couleurs primaires et accents
- Cartes: Fond blanc avec ombres légères

### 📄 Factures
- En-tête: Bleu primaire `#3b82f6`
- Statut "Payée": Vert `#10b981`
- Statut "En attente": Orange `#f59e0b`
- Statut "Annulée": Rouge `#ef4444`

### 👥 Clients
- Bouton "Ajouter": Bleu primaire `#3b82f6`
- Cartes clients: Fond blanc
- Hover: Fond `#f8f9fa`

### 📦 Produits
- Stock OK: Vert `#10b981`
- Stock faible: Orange `#f59e0b`
- Rupture: Rouge `#ef4444`

### 🚚 Livraisons
- En cours: Bleu `#3b82f6`
- Livrée: Vert `#10b981`
- Annulée: Rouge `#ef4444`

## Classes CSS à Utiliser

```css
/* Boutons */
.button-primary      /* Bleu primaire */
.button-secondary    /* Blanc avec bordure */
.button-success      /* Vert */
.button-danger       /* Rouge */

/* Cartes */
.card                /* Carte blanche avec ombre */
.card-header         /* Titre de carte */

/* Badges */
.badge-success       /* Badge vert */
.badge-warning       /* Badge orange */
.badge-error         /* Badge rouge */
.badge-info          /* Badge bleu */

/* Textes */
.title-large         /* 24px, bold */
.title-medium        /* 18px, bold */
.title-small         /* 16px, bold */
.label-primary       /* Texte principal */
.label-secondary     /* Texte secondaire */
```

## Comment Appliquer

### Dans FXML:
```xml
<Button text="Ajouter" styleClass="button-primary"/>
<VBox styleClass="card">
    <Label text="Titre" styleClass="card-header"/>
</VBox>
```

### Dans le code Java:
```java
button.getStyleClass().add("button-primary");
label.getStyleClass().add("badge-success");
```

### Charger le CSS global:
```java
scene.getStylesheets().add(getClass().getResource("/css/global-styles.css").toExternalForm());
```
