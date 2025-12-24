import repository.*;
import service.*;
import ui.ConsoleMenu;

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
        
        // Créer et lancer le menu
        ConsoleMenu menu = new ConsoleMenu(categoryService, accountService, budgetService);
        menu.start();
    }
}