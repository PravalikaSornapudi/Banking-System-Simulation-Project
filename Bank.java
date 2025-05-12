import java.io.*;
import java.util.*;

public class Bank {
    private static final String FILE_NAME = "accounts.ser";
    private static Map<String, Account> accounts = new HashMap<>();

    public static void main(String[] args) {
        loadAccounts();

        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- Bank Menu ---");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. View Account");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> createAccount(sc);
                case 2 -> deposit(sc);
                case 3 -> withdraw(sc);
                case 4 -> viewAccount(sc);
                case 5 -> saveAccounts();
                default -> System.out.println("Invalid choice!");
            }
        } while (choice != 5);

        sc.close();
    }

    private static void createAccount(Scanner sc) {
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine();
        System.out.print("Enter Holder Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Initial Balance: ");
        double balance = sc.nextDouble();

        accounts.put(accNo, new Account(accNo, name, balance));
        System.out.println("Account created successfully!");
    }

    private static void deposit(Scanner sc) {
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine();

        Account acc = accounts.get(accNo);
        if (acc != null) {
            System.out.print("Enter amount to deposit: ");
            double amt = sc.nextDouble();
            acc.deposit(amt);
        } else {
            System.out.println("Account not found!");
        }
    }

    private static void withdraw(Scanner sc) {
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine();

        Account acc = accounts.get(accNo);
        if (acc != null) {
            System.out.print("Enter amount to withdraw: ");
            double amt = sc.nextDouble();
            try {
                acc.withdraw(amt);
            } catch (InsufficientFundsException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Account not found!");
        }
    }

    private static void viewAccount(Scanner sc) {
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine();

        Account acc = accounts.get(accNo);
        if (acc != null) {
            acc.displayDetails();
        } else {
            System.out.println("Account not found!");
        }
    }

    private static void saveAccounts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(accounts);
            System.out.println("Accounts saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }

    private static void loadAccounts() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
                accounts = (Map<String, Account>) ois.readObject();
                System.out.println("Accounts loaded from file.");
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error loading accounts: " + e.getMessage());
            }
        }
    }
}
