package ui;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
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
                    searchByAccount();
                    break;
                case 4:
                    searchByCategory();
                    break;
                case 5:
                    searchByDateRange();
                    break;
                case 6:
                    updateTransaction();
                    break;
                case 7:
                    deleteTransaction();
                    break;
                case 8:
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
        System.out.println("3. Rechercher par compte");
        System.out.println("4. Rechercher par catégorie");
        System.out.println("5. Rechercher par période");
        System.out.println("6. Modifier une transaction");
        System.out.println("7. Supprimer une transaction");
        System.out.println("8. Retour au menu principal");
        System.out.print("Votre choix : ");
    }

    private void createTransaction() {
        try {
            System.out.println("\n=== CRÉER UNE TRANSACTION ===");
            
            if (accountService.getAllAccounts().isEmpty()) {
                System.out.println("Aucun compte disponible. Créez d'abord un compte !");
                return;
            }
            
            System.out.println("\nComptes disponibles :");
            accountService.getAllAccounts().forEach(acc -> 
                System.out.printf("%d. %s (Solde: %.2f€)\n", 
                    acc.getId(), acc.getName(), acc.getBalance())
            );
            
            System.out.print("\nID du compte : ");
            int accountId = scanner.nextInt();
            scanner.nextLine();
            
            Account account = accountService.getAccountById(accountId);
            
            System.out.print("Montant : ");
            double amount = scanner.nextDouble();
            scanner.nextLine();
            
            System.out.print("Description : ");
            String description = scanner.nextLine();
            
            System.out.print("Date (YYYY-MM-DD) ou Entrée pour aujourd'hui : ");
            String dateStr = scanner.nextLine();
            LocalDate date;
            
            if (dateStr.trim().isEmpty()) {
                date = LocalDate.now();
            } else {
                try {
                    date = LocalDate.parse(dateStr);
                } catch (DateTimeParseException e) {
                    System.out.println("Format de date invalide. Utilisation de la date du jour.");
                    date = LocalDate.now();
                }
            }
            
            Category category = null;
            
            if (!categoryService.getAllCategories().isEmpty()) {
                System.out.print("\nUtiliser une catégorie ? (o/n) : ");
                String useCategory = scanner.nextLine();
                
                if (useCategory.equalsIgnoreCase("o")) {
                    System.out.println("\nCatégories disponibles :");
                    categoryService.getAllCategories().forEach(cat -> 
                        System.out.printf("%d. %s (%s)\n", 
                            cat.getId(), cat.getName(), cat.getType())
                    );
                    
                    System.out.print("\nID de la catégorie : ");
                    int catId = scanner.nextInt();
                    scanner.nextLine();
                    
                    category = categoryService.getAllCategories().stream()
                        .filter(c -> c.getId() == catId)
                        .findFirst()
                        .orElse(null);
                    
                    if (category == null) {
                        System.out.println("Catégorie non trouvée, transaction sans catégorie.");
                    }
                }
            }
            
            Transaction transaction = budgetService.addTransaction(
                amount, date, description, category, account
            );
            
            System.out.printf("\nTransaction créée : %.2f€ (%s) - Nouveau solde : %.2f€\n", 
                transaction.getAmount(), 
                transaction.getType(),
                account.getBalance()
            );
            
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
            scanner.nextLine();
        }
    }

    private void listAllTransactions() {
        System.out.println("\n=== LISTE DES TRANSACTIONS ===");
        
        List<Transaction> transactions = budgetService.getAllTransactions();
        
        if (transactions.isEmpty()) {
            System.out.println("Aucune transaction.");
        } else {
            System.out.printf("\n%-5s %-12s %-10s %-20s %-15s %-20s\n", 
                "ID", "Date", "Montant", "Description", "Type", "Compte");
            System.out.println("─".repeat(90));
            
            for (Transaction t : transactions) {
                System.out.printf("%-5d %-12s %10.2f %-20s %-15s %-20s\n",
                    t.getId(),
                    t.getDate(),
                    t.getAmount(),
                    truncate(t.getDescription(), 20),
                    t.getType(),
                    t.getAccount().getName()
                );
            }
            
            System.out.println("\nTotal : " + transactions.size() + " transactions");
        }
    }

    private void searchByAccount() {
        System.out.println("\n=== RECHERCHE PAR COMPTE ===");
        
        if (accountService.getAllAccounts().isEmpty()) {
            System.out.println("Aucun compte disponible.");
            return;
        }
        
        System.out.println("\nComptes disponibles :");
        accountService.getAllAccounts().forEach(acc -> 
            System.out.println(acc.getId() + ". " + acc.getName())
        );
        
        System.out.print("\nID du compte : ");
        int accountId = scanner.nextInt();
        scanner.nextLine();
        
        try {
            Account account = accountService.getAccountById(accountId);
            List<Transaction> transactions = budgetService.getTransactionsByAccount(account);
            
            if (transactions.isEmpty()) {
                System.out.println("Aucune transaction pour ce compte.");
            } else {
                System.out.println("\n" + transactions.size() + " transaction(s) trouvée(s) :");
                transactions.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private void searchByCategory() {
        System.out.println("\n=== RECHERCHE PAR CATÉGORIE ===");
        
        if (categoryService.getAllCategories().isEmpty()) {
            System.out.println("Aucune catégorie disponible.");
            return;
        }
        
        System.out.println("\nCatégories disponibles :");
        categoryService.getAllCategories().forEach(cat -> 
            System.out.println(cat.getId() + ". " + cat.getName() + " (" + cat.getType() + ")")
        );
        
        System.out.print("\nID de la catégorie : ");
        int catId = scanner.nextInt();
        scanner.nextLine();
        
        Category category = categoryService.getAllCategories().stream()
            .filter(c -> c.getId() == catId)
            .findFirst()
            .orElse(null);
        
        if (category == null) {
            System.out.println("Catégorie non trouvée !");
            return;
        }
        
        List<Transaction> transactions = budgetService.getAllTransactions().stream()
            .filter(t -> t.getCategory() != null && t.getCategory().equals(category))
            .toList();
        
        if (transactions.isEmpty()) {
            System.out.println("Aucune transaction pour cette catégorie.");
        } else {
            System.out.println("\n" + transactions.size() + " transaction(s) trouvée(s) :");
            transactions.forEach(System.out::println);
        }
    }

    private void searchByDateRange() {
        System.out.println("\n=== RECHERCHE PAR PÉRIODE ===");
        
        try {
            System.out.print("Date de début (YYYY-MM-DD) : ");
            String startStr = scanner.nextLine();
            LocalDate startDate = LocalDate.parse(startStr);
            
            System.out.print("Date de fin (YYYY-MM-DD) : ");
            String endStr = scanner.nextLine();
            LocalDate endDate = LocalDate.parse(endStr);
            
            List<Transaction> transactions = budgetService.getTransactionsByDateRange(startDate, endDate);
            
            if (transactions.isEmpty()) {
                System.out.println("Aucune transaction pour cette période.");
            } else {
                System.out.println("\n" + transactions.size() + " transaction(s) trouvée(s) :");
                transactions.forEach(System.out::println);
                
                double total = transactions.stream()
                    .mapToDouble(Transaction::getAmount)
                    .sum();
                System.out.printf("\nMontant total : %.2f€\n", total);
            }
        } catch (DateTimeParseException e) {
            System.out.println("Format de date invalide !");
        }
    }

    private void updateTransaction() {
        System.out.println("\n=== MODIFIER UNE TRANSACTION ===");
        
        System.out.print("ID de la transaction : ");
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
        
        System.out.println("\nTransaction actuelle :");
        System.out.println(transaction);
        
        System.out.print("\nNouvelle description (Entrée pour garder) : ");
        String description = scanner.nextLine();
        
        if (!description.trim().isEmpty()) {
            transaction.setDescription(description);
        }
        
        System.out.print("Changer la catégorie ? (o/n) : ");
        String changeCategory = scanner.nextLine();
        
        if (changeCategory.equalsIgnoreCase("o")) {
            System.out.println("\nCatégories disponibles :");
            categoryService.getAllCategories().forEach(cat -> 
                System.out.println(cat.getId() + ". " + cat.getName())
            );
            
            System.out.print("\nID de la nouvelle catégorie (0 pour aucune) : ");
            int catId = scanner.nextInt();
            scanner.nextLine();
            
            if (catId == 0) {
                transaction.setCategory(null);
            } else {
                Category category = categoryService.getAllCategories().stream()
                    .filter(c -> c.getId() == catId)
                    .findFirst()
                    .orElse(null);
                transaction.setCategory(category);
            }
        }
        
        budgetService.updateTransaction(transaction);
        System.out.println("\nTransaction mise à jour !");
    }

    private void deleteTransaction() {
        System.out.println("\n=== SUPPRIMER UNE TRANSACTION ===");
        
        System.out.print("ID de la transaction : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        try {
            budgetService.deleteTransaction(id);
            System.out.println("Transaction supprimée (solde du compte mis à jour)");
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private String truncate(String str, int maxLength) {
        if (str == null || str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }
}