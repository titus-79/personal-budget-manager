package repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.Account;

public class AccountRepository {

    private HashMap<Integer, Account> accounts;
    private int nextId;

    public AccountRepository() {
        this.accounts = new HashMap<>();
        this.nextId = 1;
    }

    public Account add(String name, double initialBalance, double overdraftLimit) {
        int id = nextId++;
        Account account = new Account(id, name, initialBalance, overdraftLimit);
        accounts.put(id, account);
        return account;
    }

    public Account findById(int id) {
        if (!accounts.containsKey(id)) {
            return null;
        }
        return accounts.get(id);
    }

    public List<Account> findAll() {
        return new ArrayList<>(accounts.values());
    }

    public Account update(Account account) {
        if (!accounts.containsKey(account.getId())) {
            return null;
        }
        accounts.put(account.getId(), account);
        return account;
    }

    public boolean delete(int id) {
        if (!accounts.containsKey(id)) {
            return false;
        } else {
            accounts.remove(id);
            return true;
        }
    }

}
