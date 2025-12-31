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

import model.Account;

public class AccountRepository {

    private HashMap<Integer, Account> accounts;
    private int nextId;
    private static final String FILE_PATH = "data/accounts.dat";

    public AccountRepository() {
        this.accounts = new HashMap<>();
        this.nextId = 1;
        load();
    }

    public Account add(String name, double initialBalance, double overdraftLimit) {
        int id = nextId++;
        Account account = new Account(id, name, initialBalance, overdraftLimit);
        accounts.put(id, account);
        save();
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
        save();
        return account;
    }

    public boolean delete(int id) {
        if (!accounts.containsKey(id)) {
            return false;
        } else {
            accounts.remove(id);
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
            out.writeObject(accounts);
            out.writeInt(nextId);
            out.close();

        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des comptes: " + e.getMessage());
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
            accounts = (HashMap<Integer, Account>) in.readObject();
            nextId = in.readInt();
            in.close();

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors du chargement des comptes: " + e.getMessage());
        }
    }

}
