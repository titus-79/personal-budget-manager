package repository;

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

    public TransactionRepository() {
        this.transactions = new HashMap<>();
        this.nextId = 1;
    }

    public Transaction add(double amount, LocalDate date, String description, Category category, Account account) {
        int id = nextId++;
        Transaction transaction = new Transaction(id, amount, date, description, category, account);
        transactions.put(id, transaction);
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
        return transaction;
    }

    public boolean delete(int id) {
        if (!transactions.containsKey(id)) {
            return false;
        } else {
            transactions.remove(id);
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

}
