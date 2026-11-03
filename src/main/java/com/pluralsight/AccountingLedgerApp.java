package com.pluralsight;
import java.io.*;
import java.time.LocalTime;
import java.util.Scanner;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
                default -> System.out.println("Error that is not a option please pick one of the one's listed above");
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
                case "A" -> dispayAll();
                case "D" -> displayDeposits();
                case "P" -> displayPayments();
                case "R" -> runReportsScreen();
                case "H" -> running = false;
                default -> System.out.println("Error that was not a option please pick one of the one's listed above");
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
            System.out.println("0) Back");
            System.out.print("Choose an options: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> displayMonthToDate();
                case "2" -> displayPreviousMonth();
                case "3" -> displayYearToDate();
                case "4" -> displayPreviousYear();
                case "5" -> searchByVendor();
                case "0" -> running = false;
                default -> System.out.println("Error that was not a option please pick one of the one's listed above");
            }
        }
    }

    public static void addDeposit() {
        System.out.println();
        System.out.println("--- Add Deposit ---");

        String description = promptForString("Enter the Description");
        String vendor = promptForString("Enter the Vendor: ");
        double amount = promptForDouble("Enter the Amount: ");
        while (amount <= 0) {
            System.out.println("A payment amount must be more than 0");
            amount = promptForDouble("Enter the Correct Amount");
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
            System.out.println("A payment amount must be more then 0.");
            amount = promptForDouble("Enter the Correct Amount: ");
        }

        Transaction transaction = new Transaction(LocalDate.now(), LocalTime.now(), description, vendor, -amount);
        saveTransaction(transaction);
        System.out.println("Payment Saved.");
        pressEnterToContinue();
    }

    public static void dispayAll() {
        System.out.println("TODO: Display All");
        pressEnterToContinue();
    }

    public static void displayDeposits() {
        System.out.println("TODO: Display Deposits");
        pressEnterToContinue();
    }

    public static void displayPayments() {
        System.out.println("TODO: Display Payments");
        pressEnterToContinue();
    }

    public static void displayMonthToDate() {
        System.out.println("TODO: Display Month to Date");
        pressEnterToContinue();
    }

    public static void displayPreviousMonth() {
        System.out.println("TODO: Display Previous Month");
        pressEnterToContinue();
    }

    public static void displayYearToDate() {
        System.out.println("TODO: Display Year To Date");
        pressEnterToContinue();
    }

    public static void displayPreviousYear() {
        System.out.println("TODO: Display Previous Year");
    }

    public static void searchByVendor() {
        System.out.println("TODO: Searh by Vendor");
        pressEnterToContinue();
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


    public static String promptForString(String prompt) {
        while (true) {
            System.out.println(prompt);
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
            System.out.println(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter a number, like 77.11 ");
            }
        }
    }
}
