# Personal Budget Manager

Application console de gestion de budget personnel développée en Java avec architecture en couches, persistance des données et export CSV.

## Sommaire

- [Description](#description)
- [Fonctionnalités](#fonctionnalités)
  - [Gestion des Comptes](#gestion-des-comptes)
  - [Gestion des Catégories](#gestion-des-catégories)
  - [Gestion des Transactions](#gestion-des-transactions)
  - [Statistiques](#statistiques)
  - [Export de Données](#export-de-données)
  - [Persistance](#persistance)
- [Architecture](#architecture)
  - [Structure en couches](#structure-en-couches)
  - [Principes appliqués](#principes-appliqués)
- [Technologies utilisées](#technologies-utilisées)
- [Structure du projet](#structure-du-projet)
- [Installation et lancement](#installation-et-lancement)
  - [Prérequis](#prérequis)
  - [Compilation et exécution](#compilation-et-exécution)
  - [Première utilisation](#première-utilisation)
- [Utilisation](#utilisation)
  - [Menu principal](#menu-principal)
  - [Exemples d'utilisation](#exemples-dutilisation)
- [Format des exports CSV](#format-des-exports-csv)
- [Évolutions possibles](#évolutions-possibles)
- [Auteur](#auteur)
- [Contexte du projet](#contexte-du-projet)

---

## Description

Personal Budget Manager est une application Java en ligne de commande permettant de gérer efficacement ses finances personnelles. L'application offre un suivi détaillé des comptes bancaires, des catégories de dépenses et revenus, ainsi que des transactions, avec des fonctionnalités avancées de recherche, d'analyse statistique et d'export de données.

Le projet met l'accent sur une architecture logicielle solide, la séparation des responsabilités et les bonnes pratiques de développement.

## Fonctionnalités

### Gestion des Comptes

- Création et gestion de comptes multiples (compte courant, livret d'épargne, PEA, etc.)
- Configuration du découvert autorisé par compte
- Suivi du solde initial et du solde actuel
- Calcul automatique du solde total de tous les comptes
- Modification et suppression de comptes

### Gestion des Catégories

- Création de catégories personnalisées pour les revenus et les dépenses
- Organisation par type (INCOME/EXPENSE)
- Ajout de descriptions détaillées
- Filtrage et consultation par type
- Modification et suppression avec validation

### Gestion des Transactions

- Enregistrement de transactions avec ou sans catégorie
- Mise à jour automatique des soldes de compte
- Recherche multicritères :
  - Par compte
  - Par catégorie
  - Par période (plage de dates)
- Affichage formaté des transactions en tableau
- Modification des transactions (description, catégorie)
- Suppression avec recalcul automatique des soldes

### Statistiques

- Vue d'ensemble financière (revenus totaux, dépenses totales, solde global)
- Détail des soldes par compte
- Calcul du taux d'épargne
- Comptage et répartition des transactions par type
- Analyses temporelles

### Export de Données

- Export des transactions au format CSV
- Export des comptes au format CSV
- Export des catégories au format CSV
- Export global (ensemble des données)
- Horodatage automatique des fichiers exportés
- Format compatible avec Excel (séparateur point-virgule, format décimal français)
- Échappement automatique des caractères spéciaux

### Persistance

- Sauvegarde automatique après chaque opération (ajout, modification, suppression)
- Chargement automatique des données au démarrage de l'application
- Stockage des données via sérialisation Java (fichiers .dat)
- Initialisation intelligente avec jeu de données de test au premier lancement

## Architecture

### Structure en couches

Le projet suit le pattern architectural **Layered Architecture** (architecture en couches) garantissant une séparation claire des responsabilités :
```
┌─────────────────────────────────────┐
│   UI Layer                           │
│   (Interface Utilisateur)            │
│   - ConsoleMenu                      │
│   - CategoryMenu                     │
│   - AccountMenu                      │
│   - TransactionMenu                  │
│   - StatisticsMenu                   │
├─────────────────────────────────────┤
│   Service Layer                      │
│   (Logique Métier)                   │
│   - CategoryService                  │
│   - AccountService                   │
│   - BudgetService                    │
│   - ExportService                    │
├─────────────────────────────────────┤
│   Repository Layer                   │
│   (Accès aux Données)                │
│   - CategoryRepository               │
│   - AccountRepository                │
│   - TransactionRepository            │
├─────────────────────────────────────┤
│   Model Layer                        │
│   (Entités Métier)                   │
│   - Category                         │
│   - Account                          │
│   - Transaction                      │
│   - TransactionType                  │
└─────────────────────────────────────┘
```

**Responsabilités par couche :**

- **UI Layer** : Interaction avec l'utilisateur, affichage des menus, collecte des entrées
- **Service Layer** : Logique métier, validations, orchestration des opérations
- **Repository Layer** : Gestion du stockage et de la récupération des données
- **Model Layer** : Définition des entités métier et de leurs règles de validation

### Principes appliqués

- **Separation of Concerns** : Chaque couche a une responsabilité unique et bien définie
- **Encapsulation** : Les attributs sont privés, l'accès se fait via getters/setters
- **Validation** : Contrôles de cohérence dans les constructeurs et les services
- **DRY** (Don't Repeat Yourself) : Réutilisation du code, évitement des duplications
- **Single Responsibility Principle** : Chaque classe a une seule raison de changer
- **Interoperability** : Export CSV pour intégration avec d'autres systèmes

## Technologies utilisées

- **Java 17+**
- **Java Collections Framework** (HashMap, ArrayList)
- **Java Streams API** (filtrage, transformation, agrégation)
- **Java I/O** (ObjectInputStream/ObjectOutputStream pour la sérialisation)
- **Java Time API** (LocalDate, LocalDateTime, DateTimeFormatter)
- **Git** pour le versioning (commits conventionnels)

## Structure du projet
```
personal-budget-manager/
├── src/
│   ├── model/
│   │   ├── Account.java
│   │   ├── Category.java
│   │   ├── Transaction.java
│   │   └── TransactionType.java
│   ├── repository/
│   │   ├── AccountRepository.java
│   │   ├── CategoryRepository.java
│   │   └── TransactionRepository.java
│   ├── service/
│   │   ├── AccountService.java
│   │   ├── BudgetService.java
│   │   ├── CategoryService.java
│   │   └── ExportService.java
│   ├── ui/
│   │   ├── ConsoleMenu.java
│   │   ├── AccountMenu.java
│   │   ├── CategoryMenu.java
│   │   ├── TransactionMenu.java
│   │   └── StatisticsMenu.java
│   └── Main.java
├── data/                    (généré automatiquement)
│   ├── accounts.dat
│   ├── categories.dat
│   └── transactions.dat
├── exports/                 (généré automatiquement)
│   ├── transactions_YYYYMMDD_HHMMSS.csv
│   ├── accounts_YYYYMMDD_HHMMSS.csv
│   └── categories_YYYYMMDD_HHMMSS.csv
└── README.md
```

## Installation et lancement

### Prérequis

- Java Development Kit (JDK) 17 ou version supérieure
- Un environnement de développement Java (Eclipse, IntelliJ IDEA, VS Code) ou un terminal avec `javac` et `java`

### Compilation et exécution

**Option 1 : Ligne de commande**
```bash
# Cloner le repository
git clone https://github.com/titus-79/personal-budget-manager.git
cd personal-budget-manager

# Compiler
javac -d bin src/**/*.java

# Exécuter
java -cp bin Main
```

**Option 2 : IDE**

1. Importer le projet dans votre IDE
2. Configurer le JDK (version 17 minimum)
3. Exécuter la classe `Main.java`

### Première utilisation

Au premier lancement, l'application détecte l'absence de données et initialise automatiquement un jeu de données de démonstration :

- **3 comptes** : Compte Courant (1500€), Livret A (5000€), PEA (2000€)
- **8 catégories** : 3 revenus (Salaire, Freelance, Intérêts), 5 dépenses (Alimentation, Logement, Transport, Loisirs, Santé)
- **12 transactions** représentant un mois d'activité financière

Les données sont automatiquement sauvegardées dans le répertoire `data/` et rechargées aux lancements ultérieurs.

## Utilisation

### Menu principal
```
=== BUDGET MANAGER ===
1. Gérer les comptes
2. Gérer les catégories
3. Gérer les transactions
4. Voir les statistiques
5. Quitter
Votre choix : 
```

### Exemples d'utilisation

**Créer un compte**

1. Sélectionner "1. Gérer les comptes" dans le menu principal
2. Choisir "1. Créer un compte"
3. Saisir le nom du compte
4. Saisir le solde initial
5. Saisir le montant du découvert autorisé
6. Le compte est créé et sauvegardé automatiquement

**Ajouter une transaction**

1. Sélectionner "3. Gérer les transactions"
2. Choisir "1. Créer une transaction"
3. Sélectionner le compte concerné
4. Saisir le montant
5. Saisir la description
6. Indiquer la date (ou valider pour utiliser la date du jour)
7. Optionnel : associer une catégorie
8. Le solde du compte est automatiquement mis à jour

**Exporter les données en CSV**

1. Sélectionner "4. Voir les statistiques"
2. Choisir "5. Exporter les données en CSV"
3. Sélectionner le type de données à exporter (transactions, comptes, catégories, ou tout)
4. Les fichiers CSV sont générés dans le répertoire `exports/`
5. Ouvrir les fichiers dans Excel, LibreOffice Calc, ou tout autre tableur

**Consulter les statistiques**

1. Sélectionner "4. Voir les statistiques"
2. Choisir parmi :
   - Vue d'ensemble : synthèse complète (revenus, dépenses, solde, taux d'épargne)
   - Soldes des comptes : détail de chaque compte
   - Revenus et dépenses : analyse détaillée avec calcul du taux d'épargne
   - Nombre de transactions : répartition par type

## Format des exports CSV

Les fichiers CSV générés utilisent le format suivant :

- **Séparateur** : point-virgule (`;`) pour compatibilité Excel France
- **Format décimal** : virgule (`,`) pour les nombres
- **Encodage** : UTF-8
- **Nom des fichiers** : `type_YYYYMMDD_HHMMSS.csv` (horodatage automatique)

**Exemple de fichier `transactions_20241231_153042.csv` :**
```csv
ID;Date;Montant;Description;Type;Categorie;Compte
1;2024-12-06;2500,00;Salaire novembre;INCOME;Salaire;Compte Courant
2;2024-12-16;800,00;Mission web;INCOME;Freelance;Compte Courant
3;2024-12-11;850,00;Loyer novembre;EXPENSE;Logement;Compte Courant
```

Les fichiers CSV peuvent être ouverts directement dans Excel, Google Sheets, LibreOffice Calc, ou importés dans des logiciels de comptabilité.

## Évolutions possibles

Le projet actuel constitue une base solide pour des évolutions futures :

- **Visualisations graphiques** : Intégration de graphiques pour les statistiques (camemberts, histogrammes)
- **Budgets prévisionnels** : Définition de budgets mensuels par catégorie avec alertes de dépassement
- **Transactions récurrentes** : Gestion des transactions automatiques (salaires, loyers)
- **Import CSV** : Importation de données depuis des fichiers bancaires
- **Interface graphique** : Développement d'une UI avec JavaFX ou Swing
- **Multi-utilisateurs** : Gestion de plusieurs profils avec authentification
- **Base de données relationnelle** : Migration vers MySQL ou PostgreSQL pour des volumes importants
- **API REST** : Exposition des fonctionnalités via une API pour intégration avec d'autres applications
- **Application mobile** : Client mobile (Android/iOS) connecté à l'application

## Auteur

**Rodolphe**

## Contexte du projet

Ce projet a été réalisé dans le cadre de ma monter en compétences sur Java:

1. **Maîtrise de Java** : Consolidation des fondamentaux du langage (POO, collections, exceptions, I/O)
2. **Architecture logicielle** : Application des principes d'architecture en couches et de séparation des responsabilités
3. **Bonnes pratiques** : Application des conventions de nommage, validation des données, gestion des erreurs
4. **Persistance** : Mise en œuvre de la sérialisation Java pour la sauvegarde des données
5. **Interopérabilité** : Export de données au format CSV pour intégration avec d'autres outils
6. **Versioning** : Utilisation de Git avec des commits conventionnels structurés

Le projet démontre une progression depuis des bases solides en programmation procédurale vers une approche orientée objet professionnelle, avec une architecture scalable et maintenable.

---

**Technologies clés** : Java | Collections | Streams | Serialization | CSV | Architecture en couches