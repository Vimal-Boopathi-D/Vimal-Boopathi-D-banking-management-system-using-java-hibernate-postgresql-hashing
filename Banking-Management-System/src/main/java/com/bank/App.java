package com.bank;

import com.bank.entity.Account;
import com.bank.entity.BankTransaction;
import com.bank.entity.User;
import com.bank.service.AccountService;
import com.bank.service.AuthService;
import com.bank.service.TransactionService;
import com.bank.util.HibernateUtil;

import java.util.List;
import java.util.Scanner;

public class App {
    private static final AuthService authService = new AuthService();
    private static final AccountService accountService = new AccountService();
    private static final TransactionService transactionService = new TransactionService();

    public static void main(String[] args) {
        seedAdmin();

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== Banking Management System ===");
            System.out.println("1. Register (Customer)");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose: ");
            int ch = Integer.parseInt(sc.nextLine());

            switch (ch) {
                case 1 -> doRegister(sc);
                case 2 -> doLogin(sc);
                case 3 -> {
                    System.out.println("Exiting...");
                    HibernateUtil.shutdown();
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private static void seedAdmin() {
        try {
            User admin = authService.login("admin", "admin");
            if (admin == null) {
                authService.register("admin", "admin", "ADMIN");
                System.out.println("Default admin 'admin' created with password 'admin' (please change after first login)");                        
            }
        } catch (Exception e) {
            // if login failed (no admin), register
            try {
                authService.register("admin", "admin", "ADMIN");
                System.out.println("Default admin 'admin' created with password 'admin' (please change after first login)");
            } catch (Exception ex) {
                // ignore if already exists
            }
        }
    }

    private static void doRegister(Scanner sc) {
        try {
            System.out.print("Enter username: ");
            String uname = sc.nextLine();
            System.out.print("Enter password: ");
            String pwd = sc.nextLine();
            authService.register(uname, pwd, "CUSTOMER");
            System.out.println("Registered successfully. Please login.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void doLogin(Scanner sc) {
        try {
            System.out.print("Username: ");
            String uname = sc.nextLine();
            System.out.print("Password: ");
            String pwd = sc.nextLine();

            User user = authService.login(uname, pwd);
            if (user == null) {
                System.out.println("Invalid credentials");
                return;
            }

            System.out.println("Welcome, " + user.getUsername() + " (" + user.getRole() + ")");
            if ("ADMIN".equalsIgnoreCase(user.getRole())) adminMenu(sc);
            else customerMenu(sc, user);

        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
        }
    }

    private static void adminMenu(Scanner sc) {
        while (true) {
            System.out.println("\n=== ADMIN MENU ===");
            System.out.println("1. Create account for user");
            System.out.println("2. View all accounts");
            System.out.println("3. Back");
            System.out.print("Choose: ");
            int c = Integer.parseInt(sc.nextLine());
            switch (c) {
                case 1 -> {
                    try {
                        System.out.print("Enter username: ");
                        String uname = sc.nextLine();
                        System.out.print("Account type (SAVINGS/CURRENT): ");
                        String type = sc.nextLine();
                        System.out.print("Initial deposit: ");
                        double amt = Double.parseDouble(sc.nextLine());
                        Account acc = accountService.createAccount(uname, type, amt);
                        System.out.println("Created: " + acc);
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                case 2 -> {
                    List<Account> list = accountService.getAllAccounts();
                    list.forEach(System.out::println);
                }
                case 3 -> { return; }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private static void customerMenu(Scanner sc, User user) {
        while (true) {
            System.out.println("\n=== CUSTOMER MENU ===");
            System.out.println("1. Create account");
            System.out.println("2. View my accounts");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Transfer");
            System.out.println("6. Transaction history");
            System.out.println("7. Back");
            System.out.print("Choose: ");
            int c = Integer.parseInt(sc.nextLine());
            switch (c) {
                case 1 -> {
                    try {
                        System.out.print("Account type (SAVINGS/CURRENT): ");
                        String type = sc.nextLine();
                        System.out.print("Initial deposit: ");
                        double amt = Double.parseDouble(sc.nextLine());
                        Account acc = accountService.createAccount(user.getUsername(), type, amt);
                        System.out.println("Created: " + acc);
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                case 2 -> {
                    List<Account> accounts = accountService.getAccountsForUser(user.getId());
                    accounts.forEach(System.out::println);
                }
                case 3 -> {
                    try {
                        System.out.print("Account number: ");
                        String accNum = sc.nextLine();
                        System.out.print("Amount to deposit: ");
                        double amt = Double.parseDouble(sc.nextLine());
                        transactionService.deposit(accNum, amt);
                        System.out.println("Deposit successful");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                case 4 -> {
                    try {
                        System.out.print("Account number: ");
                        String accNum = sc.nextLine();
                        System.out.print("Amount to withdraw: ");
                        double amt = Double.parseDouble(sc.nextLine());
                        transactionService.withdraw(accNum, amt);
                        System.out.println("Withdraw successful");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                case 5 -> {
                    try {
                        System.out.print("Source account number: ");
                        String from = sc.nextLine();
                        System.out.print("Destination account number: ");
                        String to = sc.nextLine();
                        System.out.print("Amount to transfer: ");
                        double amt = Double.parseDouble(sc.nextLine());
                        transactionService.transfer(from, to, amt);
                        System.out.println("Transfer successful");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                case 6 -> {
                    try {
                        System.out.print("Enter your account number: ");
                        String accNum = sc.nextLine();
                        Account acc = accountService.getAccountByNumber(accNum);
                        if (acc == null) { System.out.println("Account not found"); break; }
                        List<BankTransaction> txs = transactionService.getTransactionsForAccount(acc.getAccountId());
                        txs.forEach(System.out::println);
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                case 7 -> { return; }
                default -> System.out.println("Invalid choice");
            }
        }
    }
}
