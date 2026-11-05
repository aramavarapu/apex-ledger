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
        System.out.println();
        System.out.println("--- All Transactions ---");
        printHeader();

        for (Transaction transaction : transactions) {
            printTransaction(transaction);
        }
        System.out.println();
        pressEnterToContinue();
    }

    public static void displayDeposits() {
        System.out.println();
        System.out.println("--- Deposits ---");
        printHeader();

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                printTransaction(transaction);
            }
        }
        System.out.println();
        pressEnterToContinue();
    }

    public static void displayPayments() {
        System.out.println();
        System.out.println("--- Payments ---");
        printHeader();

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                printTransaction(transaction);
            }
        }
        System.out.println();
        pressEnterToContinue();
    }

    public static void displayMonthToDate() {
        System.out.println();
        System.out.println("--- Month To Date ---");
        printHeader();

        LocalDate today = LocalDate.now();
        boolean found = false;

        for (Transaction transaction : transactions) {
            LocalDate date = transaction.getDate();
            if (date.getMonthValue() == today.getMonthValue() && date.getYear() == today.getYear()) {
                printTransaction(transaction);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No transactions found");
        }
        System.out.println();
        pressEnterToContinue();
    }

    public static void displayPreviousMonth() {
        System.out.println();
        System.out.println("--- Previous Month ---");
        printHeader();

        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        boolean found = false;

        for (Transaction transaction : transactions) {
            LocalDate date = transaction.getDate();
            if (date.getMonthValue() == lastMonth.getMonthValue() && date.getYear() == lastMonth.getYear()) {
                printTransaction(transaction);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No transactions found");
        }
        System.out.println();
        pressEnterToContinue();
    }

    public static void displayYearToDate() {
        System.out.println();
        System.out.println("--- Year To Date ---");
        printHeader();

        int thisYear = LocalDate.now().getYear();
        boolean found = false;

        for (Transaction transaction : transactions) {
            if (transaction.getDate().getYear() == thisYear) {
                printTransaction(transaction);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No transactions found");
        }
        System.out.println();
        pressEnterToContinue();
    }

    public static void displayPreviousYear() {
        System.out.println();
        System.out.println("--- Previous Year ---");
        printHeader();

        int lastYear = LocalDate.now().getYear() - 1;
        boolean found = false;

        for (Transaction transaction : transactions) {
            if (transaction.getDate().getYear() == lastYear) {
                printTransaction(transaction);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No transaction found");
        }
        System.out.println();
        pressEnterToContinue();
    }

    public static void searchByVendor() {
        System.out.println();
        System.out.println("--- Search by Vendor ---");

        String vendor = promptForString("Vendor: ");
        System.out.println();
        printHeader();

        boolean found = false;

        for (Transaction transaction : transactions) {
            if (transaction.getVendor().equalsIgnoreCase(vendor)) {
                printTransaction(transaction);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No transactions found");
        }
        System.out.println();
        pressEnterToContinue();
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

        System.out.println();
        printHeader();

        boolean found = false;

        for (Transaction transaction : transactions) {
            boolean matches = true;

            if (startDate != null && transaction.getDate().isBefore(startDate)) {
                matches = false;
            }

            if (endDate != null && transaction.getDate().isAfter(endDate)) {
                matches = false;
            }

            if (!description.isEmpty() && !transaction.getDescription().toLowerCase().contains(description.toLowerCase())) {
                matches = false;
            }

            if (!vendor.isEmpty() && !transaction.getVendor().equalsIgnoreCase(vendor)) {
                matches = false;
            }

            if (amount!= null && Double.compare(transaction.getAmount(), amount) != 0) {
                matches = false;
            }

            if (matches) {
                printTransaction(transaction);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No transactions found");
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
