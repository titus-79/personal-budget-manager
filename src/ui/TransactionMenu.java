package ui;

import java.time.LocalDate;
import java.util.Scanner;
import model.*;
import service.*;

public class TransactionMenu {

    private Scanner scanner;
    private BudgetService budgetService;
    private AccountService accountService;
    private CategoryService categoryService;

    public TransactionMenu(Scanner scanner, BudgetService budgetService,
            AccountService accountService, CategoryService categoryService) {
        this.scanner = scanner;
        this.budgetService = budgetService;
        this.accountService = accountService;
        this.categoryService = categoryService;
    }

    public void display() {
        boolean back = false;
        while (!back) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createTransaction();
                    break;
                case 2:
                    listAllTransactions();
                    break;
                case 3:
                    updateTransaction();
                    break;
                case 4:
                    deleteTransaction();
                    break;
                case 5:
                    back = true;
                    break;
                default:
                    System.out.println("Choix invalide !");
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n=== GESTION DES TRANSACTIONS ===");
        System.out.println("1. Créer une transaction");
        System.out.println("2. Lister toutes les transactions");
        System.out.println("3. Modifier une transaction");
        System.out.println("4. Supprimer une transaction");
        System.out.println("5. Retour au menu principal");
        System.out.print("Votre choix : ");
    }

    private void createTransaction() {
        try {
            System.out.println("\n=== Comptes disponibles ===");
            accountService.getAllAccounts().forEach(
                    acc -> System.out.println(acc.getId() + ". " + acc.getName() + " (" + acc.getBalance() + "Euros)"));
            System.out.print("ID du compte: ");
            int accountId = scanner.nextInt();
            scanner.nextLine();
            Account account = accountService.getAccountById(accountId);

            System.out.print("Montant: ");
            double amount = scanner.nextDouble();
            scanner.nextLine();

            System.out.print("Description: ");
            String description = scanner.nextLine();

            LocalDate date = LocalDate.now();

            System.out.print("Utiliser une catégorie ? (o/n): ");
            String useCategory = scanner.nextLine();
            Category category = null;

            if (useCategory.equalsIgnoreCase("o")) {
                System.out.println("\n=== Catégories disponibles ===");
                categoryService.getAllCategories().forEach(
                        cat -> System.out.println(cat.getId() + ". " + cat.getName() + " (" + cat.getType() + ")"));
                System.out.print("ID de la catégorie: ");
                int catId = scanner.nextInt();
                scanner.nextLine();
                category = categoryService.getAllCategories().stream()
                        .filter(c -> c.getId() == catId)
                        .findFirst()
                        .orElse(null);
            }

            Transaction transaction = budgetService.addTransaction(amount, date, description, category, account);
            System.out.println("Transaction créée: " + transaction.getAmount() + "Euros - " + transaction.getType());

        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    private void listAllTransactions() {
        System.out.println("\n=== LISTE DES TRANSACTIONS ===");
        if (budgetService.getAllTransactions().isEmpty()) {
            System.out.println("Aucune transaction.");
        } else {
            budgetService.getAllTransactions().forEach(System.out::println);
        }
    }

    private void updateTransaction() {
        try {
            System.out.print("ID de la transaction à modifier: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            Transaction transaction = budgetService.getAllTransactions().stream()
                    .filter(t -> t.getId() == id)
                    .findFirst()
                    .orElse(null);

            if (transaction == null) {
                System.out.println("Transaction non trouvée !");
                return;
            }

            System.out.print("Nouvelle description (actuelle: " + transaction.getDescription() + "): ");
            String description = scanner.nextLine();
            transaction.setDescription(description);

            budgetService.updateTransaction(transaction);
            System.out.println("Transaction mise à jour");

        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    private void deleteTransaction() {
        try {
            System.out.print("ID de la transaction à supprimer: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            budgetService.deleteTransaction(id);
            System.out.println("Transaction supprimée");

        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
}