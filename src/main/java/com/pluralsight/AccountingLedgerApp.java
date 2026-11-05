package com.pluralsight;
import java.io.*;
import java.time.LocalTime;
import java.util.Scanner;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
public class AccountingLedgerApp {

    private static Scanner scanner = new Scanner(System.in);
    static ArrayList<Transaction> transactions = new ArrayList<>();


    static String fileName = "transactions.csv";
    static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void main(String[] args) {
        loadTransactions();
        runHomeScreen();
    }

    public static void runHomeScreen() {
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("=== HOME ===");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "D" -> addDeposit();
                case "P" -> makePayment();
                case "L" -> runLedgerScreen();
                case "X" -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("That isn't an option. Try again.");
            }
        }
    }

    public static void runLedgerScreen() {
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("=== LEDGER ===");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "A" -> displayAll();
                case "D" -> displayDeposits();
                case "P" -> displayPayments();
                case "R" -> runReportsScreen();
                case "H" -> running = false;
                default -> System.out.println("That isn't an option. Try again.");
            }
        }
    }

    public static void runReportsScreen() {
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("==== REPORTS ====");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Custom Search");
            System.out.println("0) Back");
            System.out.print("Choose an options: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> displayMonthToDate();
                case "2" -> displayPreviousMonth();
                case "3" -> displayYearToDate();
                case "4" -> displayPreviousYear();
                case "5" -> searchByVendor();
                case "6" -> customSearch();
                case "0" -> running = false;
                default -> System.out.println("That isn't an option. Try again.");
            }
        }
    }

    public static void addDeposit() {
        System.out.println();
        System.out.println("--- Add Deposit ---");

        String description = promptForString("Enter the Description: ");
        String vendor = promptForString("Enter the Vendor: ");
        double amount = promptForDouble("Enter the Amount: ");
        while (amount <= 0) {
            System.out.println("A deposit amount must be more than 0");
            amount = promptForDouble("Enter the Correct Amount: ");
        }

        Transaction transaction = new Transaction(LocalDate.now(), LocalTime.now(), description, vendor, amount);
        saveTransaction(transaction);
        System.out.println("Deposit Saved");
        pressEnterToContinue();
    }

    public static void makePayment() {
        System.out.println();
        System.out.println("--- Make Payment ---");

        String description = promptForString("Enter the Description: ");
        String vendor = promptForString("Enter the Vendor: ");
        double amount = promptForDouble("Enter Amount: ");
        while (amount <= 0) {
            System.out.println("A payment amount must be more than 0.");
            amount = promptForDouble("Enter the Correct Amount: ");
        }

        Transaction transaction = new Transaction(LocalDate.now(), LocalTime.now(), description, vendor, -amount);
        saveTransaction(transaction);
        System.out.println("Payment Saved.");
        pressEnterToContinue();
    }

    public static void displayAll() {
        ArrayList<Transaction> results = new ArrayList<>();

        for (Transaction transaction : transactions) {
            results.add(transaction);
        }
        displayResults(results, "--- All Transactions ---");
    }

    public static void displayDeposits() {
        ArrayList<Transaction> results = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                results.add(transaction);
            }
        }
        displayResults(results, "--- Deposits ---");
    }

    public static void displayPayments() {
        ArrayList<Transaction> results = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                results.add(transaction);
            }
        }
        displayResults(results, "--- Payments ---");
    }

    public static void displayMonthToDate() {
        ArrayList<Transaction> results = new ArrayList<>();

        LocalDate today = LocalDate.now();

        for (Transaction transaction : transactions) {
            LocalDate date = transaction.getDate();

            if (date.getMonthValue() == today.getMonthValue() && date.getYear() == today.getYear()) {
                results.add(transaction);
            }
        }
        displayResults(results, "--- Month To Date ---");
    }

    public static void displayPreviousMonth() {
        ArrayList<Transaction> results = new ArrayList<>();

        LocalDate lastMonth = LocalDate.now().minusMonths(1);

        for (Transaction transaction : transactions) {
            LocalDate date = transaction.getDate();

            if (date.getMonthValue() == lastMonth.getMonthValue() && date.getYear() == lastMonth.getYear()) {
                results.add(transaction);
            }
        }
        displayResults(results, "--- Previous Month ---");
    }

    public static void displayYearToDate() {
        ArrayList<Transaction> results = new ArrayList<>();

        int thisYear = LocalDate.now().getYear();

        for (Transaction transaction : transactions) {
            if (transaction.getDate().getYear() == thisYear) {
                results.add(transaction);
            }
        }
        displayResults(results, "--- Year To Date ---");
    }

    public static void displayPreviousYear() {
        ArrayList<Transaction> results = new ArrayList<>();

        int lastYear = LocalDate.now().getYear() - 1;

        for (Transaction transaction : transactions) {
            if (transaction.getDate().getYear() == lastYear) {
                results.add(transaction);
            }
        }
        displayResults(results, "--- Previous Year ---");
    }

    public static void searchByVendor() {
        System.out.println();
        System.out.println("--- Search by Vendor ---");

        String vendor = promptForString("Vendor: ");

        ArrayList<Transaction> results = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (transaction.getVendor().equalsIgnoreCase(vendor)) {
                results.add(transaction);
            }
        }
        displayResults(results, "--- Vendor Search Results ---");

    }

    public static void customSearch() {
        System.out.println();
        System.out.println("--- Custom Search ---");
        System.out.println("Answer only the question you care about. Press Enter to skip one.");

        LocalDate startDate = promptForOptionalDate("Start date (yyyy-MM-dd): ");
        LocalDate endDate = promptForOptionalDate("End date (yyyy-MM-dd): ");

        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            System.out.println("End date can't be before the start date ");
            pressEnterToContinue();
            return;
        }


        System.out.print("Description: ");
        String description = scanner.nextLine().trim();

        System.out.print("Vendor: ");
        String vendor = scanner.nextLine().trim();

        Double amount = promptForOptionalDouble("Amount: ");

        ArrayList<Transaction> results = findTransactions(startDate, endDate, description, vendor, amount);
        displayResults(results, "--- Custom Search Results ---");


    }

    public static ArrayList<Transaction> findTransactions(LocalDate startDate, LocalDate endDate, String description, String vendor, Double amount) {
        ArrayList<Transaction> matches = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (startDate != null && transaction.getDate().isBefore(startDate)) {
                continue;
            }
            if (endDate != null && transaction.getDate().isAfter(endDate)) {
                continue;
            }
            if (!description.isEmpty() && !transaction.getDescription().toLowerCase().contains(description.toLowerCase())) {
                continue;
            }
            if (!vendor.isEmpty() && !transaction.getVendor().toLowerCase().contains(vendor)) {
                continue;
            }
            if (amount != null && Double.compare(transaction.getAmount(), amount) != 0) {
                continue;
            }
            matches.add(transaction);
        }
        return matches;
    }

    public static void displayResults(ArrayList<Transaction> results, String title) {
        System.out.println();
        System.out.println(title);
        printHeader();

        if(results.isEmpty()) {
            System.out.println("No transactions found");
        } else {
            for (Transaction transaction : results) {
                printTransaction(transaction);
            }
        }
        System.out.println();
        pressEnterToContinue();
    }

    public static void printHeader() {
        System.out.printf("%-12s %-10s %-28s %-18s %12s%n", "Date", "Time", "Description", "Vendor", "Amount");
    }

    public static void printTransaction(Transaction transaction) {
        System.out.printf("%-12s %-10s %-28s %-18s %12.2f%n", transaction.getDate(), transaction.getTime().format(timeFormat), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
    }

    public static void loadTransactions () {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            reader.readLine();

            String line;

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] fields = line.split("\\|");

                LocalDate date = LocalDate.parse(fields[0]);
                LocalTime time = LocalTime.parse(fields[1]);
                String description = fields[2];
                String vendor = fields[3];
                double amount = Double.parseDouble(fields[4]);

                Transaction transaction = new Transaction(date, time, description, vendor, amount);
                transactions.add(transaction);
            }
        } catch (IOException e) {
            System.out.println("Could not load transaction");
        }
    }



    public static void saveTransaction(Transaction transaction) {
        String line = transaction.getDate() + "|" + transaction.getTime().format(timeFormat) + "|" + transaction.getDescription() + "|" + transaction.getVendor() + "|" + String.format("%.2f", transaction.getAmount()) + "\n";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true)))

        {
            writer.write(line);
            transactions.add(transaction);
        }catch (IOException e) {
            System.out.println("The transaction could not be saved: " + e.getMessage());
        }
    }

    public static void pressEnterToContinue() {
        System.out.println("Press Enter to continue");
        scanner.nextLine();
    }

    public static LocalDate promptForOptionalDate (String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return null;
            }
            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("Enter a date like 2026-05-05");
            }
        }
    }

    public static Double promptForOptionalDouble (String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return  null;
            }
            try {
                double value = Double.parseDouble(input);
                if (Double.isNaN(value) || Double.isInfinite(value)) {
                    System.out.println("Enter a number, like 77.77, or press Enter to skip");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Enter a number, like 77.77, or press Enter to skip");
            }
        }
    }



    public static String promptForString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("This can't be blank");
            } else if (input.contains("|")) {
                System.out.println("Please don't use the | symbol");
            } else {
                return input;
            }
        }
    }

    public static double promptForDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter a number, like 77.11 ");
            }
        }
    }
}
