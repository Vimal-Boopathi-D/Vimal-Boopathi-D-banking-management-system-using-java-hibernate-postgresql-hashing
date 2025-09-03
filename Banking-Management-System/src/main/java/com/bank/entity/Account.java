package com.bank.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int accountId;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String accountType; // SAVINGS or CURRENT

    @Column(nullable = false)
    private double balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BankTransaction> transactions = new ArrayList<>();

    public Account() {}

    public Account(String accountType, double initialDeposit, User user) {
        this.accountNumber = generateAccountNumber();
        this.accountType = accountType;
        this.balance = initialDeposit;
        this.user = user;
    }

    private String generateAccountNumber() {
        return "AC" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // getters and setters
    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<BankTransaction> getTransactions() { return transactions; }
    public void setTransactions(List<BankTransaction> transactions) { this.transactions = transactions; }

    @Override
    public String toString() {
        return "Account{" + "accountId=" + accountId + ", accountNumber='" + accountNumber + '\'' + ", accountType='" + accountType + '\'' + ", balance=" + balance + '}';
    }
}
