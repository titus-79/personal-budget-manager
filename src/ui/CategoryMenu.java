package ui;

import java.util.Scanner;

import model.Category;
import model.TransactionType;
import service.CategoryService;

public class CategoryMenu {

    private Scanner scanner;
    private CategoryService categoryService;

    public CategoryMenu(Scanner scanner, CategoryService categoryService) {
        this.scanner = scanner;
        this.categoryService = categoryService;
    }

    public void display() {
        boolean back = false;
        while (!back) {
            displayCategoryMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createCategory();
                    break;
                case 2:
                    listAllCategories();
                    break;
                case 3:
                    listCategoriesByType();
                    break;
                case 4:
                    updateCategory();
                    break;
                case 5:
                    deleteCategory();
                    break;
                case 6:
                    back = true;
                    break;
                default:
                    System.out.println("Choix invalide !");
            }
        }
    }

    private void displayCategoryMenu() {
        System.out.println("\n=== GESTION DES CATÉGORIES ===");
        System.out.println("1. Créer une catégorie");
        System.out.println("2. Lister toutes les catégories");
        System.out.println("3. Lister par type (INCOME/EXPENSE)");
        System.out.println("4. Modifier une catégorie");
        System.out.println("5. Supprimer une catégorie");
        System.out.println("6. Retour au menu principal");
        System.out.print("Votre choix : ");
    }

    private void createCategory() {
        System.out.print("Nom de la catégorie: ");
        String name = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Type (INCOME/EXPENSE): ");
        String typeStr = scanner.nextLine();
        TransactionType type = TransactionType.valueOf(typeStr.toUpperCase());

        try {
            Category category = categoryService.createCategory(name, description, type);
            System.out.println("Catégorie créée: " + category);
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur: " + e.getMessage());
        }

    }

    private void listAllCategories() {
        System.out.println("\nToutes les catégories:");
        for (Category category : categoryService.getAllCategories()) {
            System.out.println(category);
        }
    }

    private void listCategoriesByType() {
        System.out.print("Type (INCOME/EXPENSE): ");
        String typeStr = scanner.nextLine();
        TransactionType type = TransactionType.valueOf(typeStr.toUpperCase());

        System.out.println("\nCatégories de type " + type + ":");
        for (Category category : (type == TransactionType.INCOME
                ? categoryService.getIncomeCategories()
                : categoryService.getExpenseCategories())) {
            System.out.println(category);
        }
    }

    private void updateCategory() {
        System.out.print("ID de la catégorie à modifier: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Category category = categoryService.getAllCategories().stream()
                .filter(cat -> cat.getId() == id)
                .findFirst()
                .orElse(null);

        if (category == null) {
            System.out.println("Catégorie non trouvée !");
            return;
        }

        System.out.print("Nouveau nom (actuel: " + category.getName() + "): ");
        String name = scanner.nextLine();
        System.out.print("Nouvelle description (actuelle: " + category.getDescription() + "): ");
        String description = scanner.nextLine();
        System.out.print("Nouveau type (INCOME/EXPENSE) (actuel: " + category.getType() + "): ");
        String typeStr = scanner.nextLine();
        TransactionType type = TransactionType.valueOf(typeStr.toUpperCase());

        category.setName(name);
        category.setDescription(description);
        category.setType(type);

        Category updatedCategory = categoryService.updateCategory(category);
        System.out.println("Catégorie mise à jour: " + updatedCategory);
    }

    private void deleteCategory() {
        System.out.print("ID de la catégorie à supprimer: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        try {
            categoryService.deleteCategory(id);
            System.out.println("Catégorie supprimée.");
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

}
