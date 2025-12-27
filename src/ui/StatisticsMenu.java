package ui;

import java.util.Scanner;
import model.Account;
import service.*;

public class StatisticsMenu {

    private Scanner scanner;
    private BudgetService budgetService;
    private AccountService accountService;

    public StatisticsMenu(Scanner scanner, BudgetService budgetService, AccountService accountService) {
        this.scanner = scanner;
        this.budgetService = budgetService;
        this.accountService = accountService;
    }

    public void display() {
        boolean back = false;
        while (!back) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    displayOverview();
                    break;
                case 2:
                    displayAccountBalances();
                    break;
                case 3:
                    displayIncomeExpenses();
                    break;
                case 4:
                    displayTransactionCount();
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
        System.out.println("\n=== STATISTIQUES ===");
        System.out.println("1. Vue d'ensemble");
        System.out.println("2. Soldes des comptes");
        System.out.println("3. Revenus et dépenses");
        System.out.println("4. Nombre de transactions");
        System.out.println("5. Retour au menu principal");
        System.out.print("Votre choix : ");
    }

    private void displayOverview() {
        System.out.println("\n=== VUE D'ENSEMBLE ===");
        System.out.println("Revenus totaux:    " + budgetService.getTotalIncome() + "€");
        System.out.println("Dépenses totales:  " + budgetService.getTotalExpenses() + "€");
        System.out.println("----------------------------------");
        System.out.println("Solde global:      " + budgetService.getBalance() + "€");
        System.out.println("\nSolde des comptes: " + accountService.getTotalBalance() + "€");
        System.out.println("Transactions:      " + budgetService.getAllTransactions().size());
    }

    private void displayAccountBalances() {
        System.out.println("\n=== SOLDES DES COMPTES ===");
        if (accountService.getAllAccounts().isEmpty()) {
            System.out.println("Aucun compte.");
        } else {
            for (Account account : accountService.getAllAccounts()) {
                System.out.println(account.getName() + ": " + account.getBalance() + "€ " +
                        "(Initial: " + account.getInitialBalance() + "€)");
            }
            System.out.println("----------------------------------");
            System.out.println("Total: " + accountService.getTotalBalance() + "€");
        }
    }

    private void displayIncomeExpenses() {
        System.out.println("\n=== REVENUS ET DÉPENSES ===");
        double income = budgetService.getTotalIncome();
        double expenses = budgetService.getTotalExpenses();
        double balance = budgetService.getBalance();

        System.out.printf("Revenus:   %.2f€\n", income);
        System.out.printf("Dépenses:  %.2f€\n", expenses);
        System.out.println("----------------------------------");
        System.out.printf("Solde:     %.2f€\n", balance);

        if (income > 0) {
            double savingsRate = (balance / income) * 100;
            System.out.printf("\nTaux d'épargne: %.1f%%\n", savingsRate);
        }
    }

    private void displayTransactionCount() {
        System.out.println("\n=== NOMBRE DE TRANSACTIONS ===");
        int total = budgetService.getAllTransactions().size();

        long incomeCount = budgetService.getAllTransactions().stream()
                .filter(t -> t.getType() == model.TransactionType.INCOME)
                .count();

        long expenseCount = budgetService.getAllTransactions().stream()
                .filter(t -> t.getType() == model.TransactionType.EXPENSE)
                .count();

        System.out.println("Revenus:   " + incomeCount + " transactions");
        System.out.println("Dépenses:  " + expenseCount + " transactions");
        System.out.println("----------------------------------");
        System.out.println("Total:     " + total + " transactions");
    }
}