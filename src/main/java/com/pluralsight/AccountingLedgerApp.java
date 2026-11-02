package com.pluralsight;
import java.util.Scanner;
public class AccountingLedgerApp {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
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
                case "D" -> System.out.println("TODO: Add Deposit");
                case "P" -> System.out.println("TODO: Make Payment");
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

        while(running) {
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
                case "A" -> System.out.println("TODO: All");
                case "D" -> System.out.println("TODO: Deposits");
                case "P" -> System.out.println("TODO: Payments");
                //case "R" -> runReportsScreen();
                case "H" -> running = false;
                default -> System.out.println("Error that was not a option please pick one of the one's listed above");
            }
        }
    }
}
