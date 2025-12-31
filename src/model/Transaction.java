package model;

import java.time.LocalDate;
import java.io.Serializable;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private double amount;
    private LocalDate date;
    private String description;
    private Category category;
    private Account account;

    public Transaction(int id, double amount, LocalDate date, String description, Category category, Account account) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID cannot be null or negative");
        } else if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        } else if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        } else if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.category = category;
        this.account = account;
    }

    public Transaction(int id, double amount, LocalDate date, Account account) {
        this(id, amount, date, "", null, account);
    }

    public TransactionType getType() {
        return category != null ? category.getType() : TransactionType.EXPENSE;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public Account getAccount() {
        return account;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        this.amount = amount;
    }

    public void setDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        this.date = date;
    }

    @Override
    public String toString() {
        return "Transaction [id=" + id + ", amount=" + amount + ", date=" + date + ", description=" + description
                + ", category=" + category + ", account=" + account + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Transaction other = (Transaction) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

}
