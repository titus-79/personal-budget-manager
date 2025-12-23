package service;

import model.*;
import repository.*;
import java.time.LocalDate;
import java.util.List;

public class BudgetService {

    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;

    public BudgetService(TransactionRepository transactionRepository,
            AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public Transaction addTransaction(double amount, LocalDate date, String description,
            Category category, Account account) {

        Transaction transaction = transactionRepository.add(amount, date, description, category, account);

        if (transaction.getType() == TransactionType.INCOME) {
            account.deposit(amount);
        } else {
            account.withdraw(amount);
        }

        accountRepository.update(account);

        return transaction;
    }

    public void deleteTransaction(int id) {
        Transaction transaction = transactionRepository.findById(id);
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction not found");
        }

        Account account = transaction.getAccount();

        if (transaction.getType() == TransactionType.INCOME) {
            account.withdraw(transaction.getAmount());
        } else {
            account.deposit(transaction.getAmount());
        }

        accountRepository.update(account);

        transactionRepository.delete(id);
    }

    public Transaction updateTransaction(Transaction transaction) {
        Transaction existing = transactionRepository.findById(transaction.getId());
        if (existing == null) {
            throw new IllegalArgumentException("Transaction not found");
        }

        return transactionRepository.update(transaction);
    }

    public double getTotalIncome() {
        return transactionRepository.findAll().stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getTotalExpenses() {
        return transactionRepository.findAll().stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getBalance() {
        return getTotalIncome() - getTotalExpenses();
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsByAccount(Account account) {
        return transactionRepository.findByAccount(account);
    }

    public List<Transaction> getTransactionsByDateRange(LocalDate start, LocalDate end) {
        return transactionRepository.findByDateRange(start, end);
    }

}