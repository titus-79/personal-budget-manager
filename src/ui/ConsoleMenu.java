package ui;

import service.*;
import model.*;
import java.util.Scanner;
import java.time.LocalDate;

public class ConsoleMenu {
    private Scanner scanner;
    private CategoryMenu categoryMenu;
    private AccountMenu accountMenu;
    private TransactionMenu transactionMenu;
    private StatisticsMenu statisticsMenu;

    public ConsoleMenu(CategoryService categoryService,
            AccountService accountService,
            BudgetService budgetService) {
        this.scanner = new Scanner(System.in);
        this.categoryMenu = new CategoryMenu(scanner, categoryService);
        this.accountMenu = new AccountMenu(scanner, accountService);
        this.transactionMenu = new TransactionMenu(scanner, budgetService, accountService, categoryService);
        this.statisticsMenu = new StatisticsMenu(scanner, budgetService, accountService);
    }

    public void start() {
        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    accountMenu.display();
                    break;
                case 2:
                    categoryMenu.display();
                    break;
                case 3:
                    transactionMenu.display();
                    break;
                case 4:
                    statisticsMenu.display();
                    break;
                case 5:
                    running = false;
                    System.out.println("Au revoir !");
                    break;
                default:
                    System.out.println("Choix invalide !");
            }
        }
    }

    private void displayMainMenu() {
        System.out.println("\n=== BUDGET MANAGER ===");
        System.out.println("1. Gérer les comptes");
        System.out.println("2. Gérer les catégories");
        System.out.println("3. Gérer les transactions");
        System.out.println("4. Voir les statistiques");
        System.out.println("5. Quitter");
        System.out.print("Votre choix : ");
    }

}