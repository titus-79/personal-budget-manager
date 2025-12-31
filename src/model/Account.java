package model;

import java.io.Serializable;

public class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private double balance;
    private double initialBalance;
    private double overdraftLimit;

    public Account(int id, String name, double initialBalance, double overdraftLimit) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID cannot be null or negative");
        } else if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        } else if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        } else if (overdraftLimit < 0) {
            throw new IllegalArgumentException("Overdraft limit cannot be negative");
        }
        this.id = id;
        this.name = name;
        this.initialBalance = initialBalance;
        this.balance = initialBalance;
        this.overdraftLimit = overdraftLimit;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public double getInitialBalance() {
        return initialBalance;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        this.balance += amount;
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        } else if (amount > this.balance + this.overdraftLimit) {
            throw new IllegalArgumentException("Insufficient funds for withdrawal");
        }
        this.balance -= amount;
    }

    @Override
    public String toString() {
        return "Account [id=" + id + ", name=" + name + ", balance=" + balance + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Account other = (Account) obj;
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
