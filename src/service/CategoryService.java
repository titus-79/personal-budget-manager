package service;

import java.util.List;

import model.Category;
import model.TransactionType;
import repository.CategoryRepository;

public class CategoryService {
    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(String name, String description, TransactionType type) {

        if (categoryRepository.findAll().stream().anyMatch(cat -> cat.getName().equalsIgnoreCase(name))) {
            throw new IllegalArgumentException("Category name already exists");
        }
        return categoryRepository.add(name, description, type);
    }

    public List<Category> getIncomeCategories() {
        return categoryRepository.findByType(TransactionType.INCOME);
    }

    public Category updateCategory(Category category) {
        return categoryRepository.update(category);
    }

    public void deleteCategory(int id) {
        if (!categoryRepository.delete(id)) {
            throw new IllegalArgumentException("Category not found");
        }
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getExpenseCategories() {
        return categoryRepository.findByType(TransactionType.EXPENSE);

    }

    public Category findCategoryByName(String name) {
        return categoryRepository.findAll().stream()
                .filter(cat -> cat.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public boolean categoryExists(String name) {
        return categoryRepository.findAll().stream()
                .anyMatch(cat -> cat.getName().equalsIgnoreCase(name));
    }
}
