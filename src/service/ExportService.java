package service;

import model.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExportService {

    private static final String EXPORT_DIR = "exports/";
    private static final String SEPARATOR = ";";

    public String exportTransactions(List<Transaction> transactions) {
        try {
            java.io.File dir = new java.io.File(EXPORT_DIR);
            if (!dir.exists()) {
                dir.mkdir();
            }

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = EXPORT_DIR + "transactions_" + timestamp + ".csv";

            PrintWriter writer = new PrintWriter(new FileWriter(filename));

            writer.println("ID" + SEPARATOR +
                    "Date" + SEPARATOR +
                    "Montant" + SEPARATOR +
                    "Description" + SEPARATOR +
                    "Type" + SEPARATOR +
                    "Categorie" + SEPARATOR +
                    "Compte");

            for (Transaction t : transactions) {
                writer.println(
                        t.getId() + SEPARATOR +
                                t.getDate() + SEPARATOR +
                                String.format("%.2f", t.getAmount()).replace('.', ',') + SEPARATOR + // Format français
                                escapeCsv(t.getDescription()) + SEPARATOR +
                                t.getType() + SEPARATOR +
                                (t.getCategory() != null ? escapeCsv(t.getCategory().getName()) : "") + SEPARATOR +
                                escapeCsv(t.getAccount().getName()));
            }

            writer.close();
            return filename;

        } catch (IOException e) {
            System.err.println("Erreur lors de l'export CSV: " + e.getMessage());
            return null;
        }
    }

    public String exportAccounts(List<Account> accounts) {
        try {
            java.io.File dir = new java.io.File(EXPORT_DIR);
            if (!dir.exists()) {
                dir.mkdir();
            }

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = EXPORT_DIR + "accounts_" + timestamp + ".csv";

            PrintWriter writer = new PrintWriter(new FileWriter(filename));

            writer.println("ID" + SEPARATOR +
                    "Nom" + SEPARATOR +
                    "Solde Initial" + SEPARATOR +
                    "Solde Actuel" + SEPARATOR +
                    "Découvert Autorisé");

            for (Account a : accounts) {
                writer.println(
                        a.getId() + SEPARATOR +
                                escapeCsv(a.getName()) + SEPARATOR +
                                String.format("%.2f", a.getInitialBalance()).replace('.', ',') + SEPARATOR +
                                String.format("%.2f", a.getBalance()).replace('.', ',') + SEPARATOR +
                                String.format("%.2f", a.getOverdraftLimit()).replace('.', ','));
            }

            writer.close();
            return filename;

        } catch (IOException e) {
            System.err.println("Erreur lors de l'export CSV: " + e.getMessage());
            return null;
        }
    }

    public String exportCategories(List<Category> categories) {
        try {
            java.io.File dir = new java.io.File(EXPORT_DIR);
            if (!dir.exists()) {
                dir.mkdir();
            }

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = EXPORT_DIR + "categories_" + timestamp + ".csv";

            PrintWriter writer = new PrintWriter(new FileWriter(filename));

            writer.println("ID" + SEPARATOR +
                    "Nom" + SEPARATOR +
                    "Description" + SEPARATOR +
                    "Type");

            for (Category c : categories) {
                writer.println(
                        c.getId() + SEPARATOR +
                                escapeCsv(c.getName()) + SEPARATOR +
                                escapeCsv(c.getDescription()) + SEPARATOR +
                                c.getType());
            }

            writer.close();
            return filename;

        } catch (IOException e) {
            System.err.println("Erreur lors de l'export CSV: " + e.getMessage());
            return null;
        }
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }

        if (value.contains(SEPARATOR) || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }

        return value;
    }
}