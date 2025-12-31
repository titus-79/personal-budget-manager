package repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.Account;
import model.Transaction;
import model.TransactionType;
import model.Category;

public class TransactionRepository {
    private HashMap<Integer, Transaction> transactions;
    private int nextId;
    private static final String FILE_PATH = "data/transactions.dat";

    public TransactionRepository() {
        this.transactions = new HashMap<>();
        this.nextId = 1;
        load();
    }

    public Transaction add(double amount, LocalDate date, String description, Category category, Account account) {
        int id = nextId++;
        Transaction transaction = new Transaction(id, amount, date, description, category, account);
        transactions.put(id, transaction);
        save();
        return transaction;
    }

    public Transaction findById(int id) {
        if (!transactions.containsKey(id)) {
            return null;
        }
        return transactions.get(id);
    }

    public List<Transaction> findAll() {
        return new ArrayList<>(transactions.values());
    }

    public Transaction update(Transaction transaction) {
        if (!transactions.containsKey(transaction.getId())) {
            return null;
        }
        transactions.replace(transaction.getId(), transaction);
        save();
        return transaction;
    }

    public boolean delete(int id) {
        if (!transactions.containsKey(id)) {
            return false;
        } else {
            transactions.remove(id);
            save();
            return true;
        }
    }

    public List<Transaction> findByAccount(Account account) {
        return transactions.values().stream()
                .filter(transaction -> transaction.getAccount().equals(account))
                .toList();
    }

    public List<Transaction> findByCategory(Category category) {
        return transactions.values().stream()
                .filter(transaction -> transaction.getCategory() != null && transaction.getCategory().equals(category))
                .toList();
    }

    public List<Transaction> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return transactions.values().stream()
                .filter(transaction -> !transaction.getDate().isBefore(startDate)
                        && !transaction.getDate().isAfter(endDate))
                .toList();
    }

    public List<Transaction> findByAmountRange(double minAmount, double maxAmount) {
        return transactions.values().stream()
                .filter(transaction -> transaction.getAmount() >= minAmount && transaction.getAmount() <= maxAmount)
                .toList();
    }

    public List<Transaction> findByDescriptionKeyword(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return transactions.values().stream()
                .filter(transaction -> transaction.getDescription() != null &&
                        transaction.getDescription().toLowerCase().contains(lowerKeyword))
                .toList();
    }

    public List<Transaction> findByCategoryType(TransactionType type) {
        return transactions.values().stream()
                .filter(transaction -> transaction.getCategory() != null &&
                        transaction.getCategory().getType() == type)
                .toList();
    }

    public void save() {
        try {
            File dataDir = new File("data");
            if (!dataDir.exists()) {
                dataDir.mkdir();
            }

            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_PATH));
            out.writeObject(transactions);
            out.writeInt(nextId);
            out.close();

        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des transactions: " + e.getMessage());
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
            transactions = (HashMap<Integer, Transaction>) in.readObject();
            nextId = in.readInt();
            in.close();

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors du chargement des transactions: " + e.getMessage());
        }
    }
}
