package repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import model.Category;
import model.TransactionType;

public class CategoryRepository {
    private HashMap<Integer, Category> categories;
    private int nextId;
    private static final String FILE_PATH = "data/categories.dat";

    public CategoryRepository() {
        this.categories = new HashMap<>();
        this.nextId = 1;
        load();
    }

    public Category add(String name, String description, TransactionType type) {
        int id = nextId++;
        Category category = new Category(id, name, description, type);
        categories.put(id, category);
        save();
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
        save();
        return category;
    }

    public boolean delete(int id) {

        if (!categories.containsKey(id)) {
            return false;
        } else {
            categories.remove(id);
            save();
            return true;
        }
    }

    public void save() {
        try {
            File dataDir = new File("data");
            if (!dataDir.exists()) {
                dataDir.mkdir();
            }

            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_PATH));
            out.writeObject(categories);
            out.writeInt(nextId);
            out.close();

        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des catégories: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void load() {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return;
        }

        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_PATH));
            categories = (HashMap<Integer, Category>) in.readObject();
            nextId = in.readInt();
            in.close();

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors du chargement des catégories: " + e.getMessage());
        }
    }
}
