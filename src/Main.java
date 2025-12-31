import repository.*;
import service.*;
import ui.ConsoleMenu;
import model.*;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        // Créer les repositories
        CategoryRepository categoryRepo = new CategoryRepository();
        AccountRepository accountRepo = new AccountRepository();
        TransactionRepository transactionRepo = new TransactionRepository();

        // Créer les services
        CategoryService categoryService = new CategoryService(categoryRepo);
        AccountService accountService = new AccountService(accountRepo);
        BudgetService budgetService = new BudgetService(transactionRepo, accountRepo);

        // Initialiser les données de test SEULEMENT si aucune donnée n'existe
        if (accountService.getAllAccounts().isEmpty()) {
            System.out.println("Aucune donnée détectée, initialisation des données de test...\n");
            initializeTestData(categoryService, accountService, budgetService);
        } else {
            System.out.println("Données chargées depuis les fichiers\n");
            displayLoadedDataSummary(categoryService, accountService, budgetService);
        }

        // Créer et lancer le menu
        ConsoleMenu menu = new ConsoleMenu(categoryService, accountService, budgetService);
        menu.start();
    }

    private static void initializeTestData(CategoryService categoryService,
            AccountService accountService,
            BudgetService budgetService) {
        System.out.println("Initialisation des données de test...\n");

        // ========== CRÉER LES COMPTES ==========
        Account compteCourant = accountService.createAccount("Compte Courant", 1500.0, 500.0);
        Account livretA = accountService.createAccount("Livret A", 5000.0, 0.0);
        Account comptePEA = accountService.createAccount("PEA", 2000.0, 0.0);

        System.out.println("3 comptes créés");

        // ========== CRÉER LES CATÉGORIES ==========
        // Revenus
        Category salaire = categoryService.createCategory("Salaire", "Revenu mensuel", TransactionType.INCOME);
        Category freelance = categoryService.createCategory("Freelance", "Missions ponctuelles",
                TransactionType.INCOME);
        Category interets = categoryService.createCategory("Intérêts", "Revenus placements", TransactionType.INCOME);

        // Dépenses
        Category alimentation = categoryService.createCategory("Alimentation", "Courses et restaurants",
                TransactionType.EXPENSE);
        Category logement = categoryService.createCategory("Logement", "Loyer et charges", TransactionType.EXPENSE);
        Category transport = categoryService.createCategory("Transport", "Essence, abonnements",
                TransactionType.EXPENSE);
        Category loisirs = categoryService.createCategory("Loisirs", "Sorties, hobbies", TransactionType.EXPENSE);
        Category sante = categoryService.createCategory("Santé", "Médecin, pharmacie", TransactionType.EXPENSE);

        System.out.println("8 catégories créées (3 revenus, 5 dépenses)");

        // ========== CRÉER LES TRANSACTIONS ==========

        // Salaire du mois (INCOME)
        budgetService.addTransaction(
                2500.0,
                LocalDate.now().minusDays(25),
                "Salaire novembre",
                salaire,
                compteCourant);

        // Freelance (INCOME)
        budgetService.addTransaction(
                800.0,
                LocalDate.now().minusDays(15),
                "Mission développement web",
                freelance,
                compteCourant);

        // Loyer (EXPENSE)
        budgetService.addTransaction(
                850.0,
                LocalDate.now().minusDays(20),
                "Loyer novembre",
                logement,
                compteCourant);

        // Courses (EXPENSE)
        budgetService.addTransaction(
                120.50,
                LocalDate.now().minusDays(18),
                "Carrefour",
                alimentation,
                compteCourant);

        budgetService.addTransaction(
                85.30,
                LocalDate.now().minusDays(10),
                "Leclerc",
                alimentation,
                compteCourant);

        // Transport (EXPENSE)
        budgetService.addTransaction(
                60.0,
                LocalDate.now().minusDays(12),
                "Pass Navigo mensuel",
                transport,
                compteCourant);

        budgetService.addTransaction(
                45.0,
                LocalDate.now().minusDays(5),
                "Essence",
                transport,
                compteCourant);

        // Loisirs (EXPENSE)
        budgetService.addTransaction(
                35.0,
                LocalDate.now().minusDays(8),
                "Cinéma",
                loisirs,
                compteCourant);

        budgetService.addTransaction(
                120.0,
                LocalDate.now().minusDays(3),
                "Restaurant italien",
                loisirs,
                compteCourant);

        // Santé (EXPENSE)
        budgetService.addTransaction(
                25.0,
                LocalDate.now().minusDays(7),
                "Pharmacie - médicaments",
                sante,
                compteCourant);

        // Virement d'épargne vers Livret A (EXPENSE du compte courant)
        budgetService.addTransaction(
                500.0,
                LocalDate.now().minusDays(2),
                "Virement vers Livret A",
                null, // Sans catégorie
                compteCourant);

        // Intérêts Livret A (INCOME)
        budgetService.addTransaction(
                12.50,
                LocalDate.now().minusDays(1),
                "Intérêts mensuels",
                interets,
                livretA);

        System.out.println("12 transactions créées");

        // ========== RÉCAPITULATIF ==========
        System.out.println("\nRÉCAPITULATIF DES DONNÉES DE TEST:");
        System.out.println("   Comptes:       " + accountService.getAllAccounts().size());
        System.out.println("   Catégories:    " + categoryService.getAllCategories().size());
        System.out.println("   Transactions:  " + budgetService.getAllTransactions().size());
        System.out.println("   Solde global:  " + budgetService.getBalance() + " Euros");
        System.out.println("   Total comptes: " + accountService.getTotalBalance() + " Euros");
        System.out.println("\nDonnées de test chargées avec succès !\n");
    }

     private static void displayLoadedDataSummary(CategoryService categoryService,
                                                 AccountService accountService,
                                                 BudgetService budgetService) {
        System.out.println("DONNÉES EXISTANTES:");
        System.out.println("   Comptes:       " + accountService.getAllAccounts().size());
        System.out.println("   Catégories:    " + categoryService.getAllCategories().size());
        System.out.println("   Transactions:  " + budgetService.getAllTransactions().size());
        System.out.println("   Solde global:  " + budgetService.getBalance() + "€");
        System.out.println("   Total comptes: " + accountService.getTotalBalance() + "€\n");
    }

}