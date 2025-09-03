package com.bank.service;

import com.bank.dao.AccountDAO;
import com.bank.dao.UserDAO;
import com.bank.entity.Account;
import com.bank.entity.User;

import java.util.List;

public class AccountService {
    private final AccountDAO accountDAO = new AccountDAO();
    private final UserDAO userDAO = new UserDAO();

    public Account createAccount(String username, String type, double initialDeposit) {
        User user = userDAO.findByUsername(username);
        if (user == null) throw new RuntimeException("User not found: " + username);
        Account account = new Account(type.toUpperCase(), initialDeposit, user);
        accountDAO.save(account);
        return account;
    }

    public List<Account> getAccountsForUser(int userId) {
        return accountDAO.findByUserId(userId);
    }

    public Account getAccountByNumber(String accountNumber) {
        return accountDAO.findByAccountNumber(accountNumber);
    }

    public List<Account> getAllAccounts() {
        return accountDAO.findAll();
    }
}
