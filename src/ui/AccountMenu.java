package ui;

import java.util.Scanner;
import model.Account;
import service.AccountService;

public class AccountMenu {
    
    private Scanner scanner;
    private AccountService accountService;
    
    public AccountMenu(Scanner scanner, AccountService accountService) {
        this.scanner = scanner;
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
                    createAccount();
                    break;
                case 2:
                    listAllAccounts();
                    break;
                case 3:
                    updateAccount();
                    break;
                case 4:
                    deleteAccount();
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
        System.out.println("\n=== GESTION DES COMPTES ===");
        System.out.println("1. Créer un compte");
        System.out.println("2. Lister tous les comptes");
        System.out.println("3. Modifier un compte");
        System.out.println("4. Supprimer un compte");
        System.out.println("5. Retour au menu principal");
        System.out.print("Votre choix : ");
    }
    
    private void createAccount() {
        System.out.print("Nom du compte: ");
        String name = scanner.nextLine();
        System.out.print("Solde initial: ");
        double initialBalance = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Découvert autorisé: ");
        double overdraftLimit = scanner.nextDouble();
        scanner.nextLine();
        
        try {
            Account account = accountService.createAccount(name, initialBalance, overdraftLimit);
            System.out.println("✓ Compte créé: " + account.getName());
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Erreur: " + e.getMessage());
        }
    }
    
    private void listAllAccounts() {
        System.out.println("\n=== LISTE DES COMPTES ===");
        for (Account account : accountService.getAllAccounts()) {
            System.out.println(account);
        }
        System.out.println("\nSolde total: " + accountService.getTotalBalance() + "€");
    }
    
    private void updateAccount() {
        System.out.print("ID du compte à modifier: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Account account = accountService.getAccountById(id);
        
        System.out.print("Nouveau nom (actuel: " + account.getName() + "): ");
        String name = scanner.nextLine();
        
        account.setName(name);
        accountService.updateAccount(account);
        System.out.println("✓ Compte mis à jour");
    }
    
    private void deleteAccount() {
        System.out.print("ID du compte à supprimer: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        try {
            accountService.deleteAccount(id);
            System.out.println("✓ Compte supprimé");
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Erreur: " + e.getMessage());
        }
    }
}