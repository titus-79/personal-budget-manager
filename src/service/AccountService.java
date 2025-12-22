package service;

import java.util.List;

import model.Account;
import repository.AccountRepository;

public class AccountService {
    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(String name, double initialBalance, double overdraftLimit) {
        if (accountExists(name)) {
            throw new IllegalArgumentException("Account with the same name already exists");
        } 
        return accountRepository.add(name, initialBalance, overdraftLimit);
        
    }

    public Boolean accountExists(String name) {
        return accountRepository.findAll().stream()
                .anyMatch(acc -> acc.getName().equalsIgnoreCase(name));
    }

    public Account updateAccount(Account account) {
        Account updatedAccount = accountRepository.update(account);
        if (updatedAccount == null) {
            throw new IllegalArgumentException("Account not found");
        }
        return updatedAccount;
    }

    public void deleteAccount(int id) {
        if (!accountRepository.delete(id)) {
            throw new IllegalArgumentException("Account not found");
        }
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(int id) {
        Account account = accountRepository.findById(id);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        return account;
    }

    public Account findAccountByName(String name) {
        return accountRepository.findAll().stream()
                .filter(acc -> acc.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public double getTotalBalance() {
        return accountRepository.findAll().stream()
                .mapToDouble(Account::getBalance)
                .sum();
    }
}
