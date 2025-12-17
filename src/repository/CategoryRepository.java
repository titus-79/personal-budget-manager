package repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import model.Category;
import model.TransactionType;

public class CategoryRepository {
    private HashMap<Integer, Category> categories;
    private int nextId;

    public CategoryRepository() {
        this.categories = new HashMap<>();
        this.nextId = 1;
    }

    public Category add(String name, String description, TransactionType type) {
        int id = nextId++;
        Category category = new Category(id, name, description, type);
        categories.put(id, category);
        return category;
    }

    public Category findById(int id) {
        if (!categories.containsKey(id)) {
            return null;
        }
        return categories.get(id);

    }

    public List<Category> findAll() {
        return new ArrayList<>(categories.values());
    }

    public List<Category> findByType(TransactionType type) {

        return categories.values().stream()
                .filter(categories -> categories.getType() == type)
                .toList();
    }

    public Category update(Category category) {

        if (!categories.containsKey(category.getId())) {
            return null;
        }
        categories.put(category.getId(), category);
        return category;
    }

    public boolean delete(int id) {

        if (!categories.containsKey(id)) {
            return false;
        } else {
            categories.remove(id);
            return true;
        }
    }
}
