package com.bank.service;

import com.bank.dao.AccountDAO;
import com.bank.dao.BankTransactionDAO;
import com.bank.entity.Account;
import com.bank.entity.BankTransaction;
import com.bank.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class TransactionService {
    private final AccountDAO accountDAO = new AccountDAO();
    private final BankTransactionDAO txDAO = new BankTransactionDAO();

    public void deposit(String accountNumber, double amount) {
        if (amount <= 0) throw new RuntimeException("Amount must be > 0");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Account acc = accountDAO.findByAccountNumber(accountNumber);
            if (acc == null) throw new RuntimeException("Account not found");

            acc.setBalance(acc.getBalance() + amount);
            session.merge(acc);

            BankTransaction btx = new BankTransaction("DEPOSIT", amount, "Deposit", acc);
            session.persist(btx);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public void withdraw(String accountNumber, double amount) {
        if (amount <= 0) throw new RuntimeException("Amount must be > 0");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Account acc = accountDAO.findByAccountNumber(accountNumber);
            if (acc == null) throw new RuntimeException("Account not found");

            if (acc.getBalance() < amount) throw new RuntimeException("Insufficient balance");

            acc.setBalance(acc.getBalance() - amount);
            session.merge(acc);

            BankTransaction btx = new BankTransaction("WITHDRAW", amount, "Withdraw", acc);
            session.persist(btx);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public void transfer(String fromAccNum, String toAccNum, double amount) {
        if (amount <= 0) throw new RuntimeException("Amount must be > 0");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Account from = accountDAO.findByAccountNumber(fromAccNum);
            Account to = accountDAO.findByAccountNumber(toAccNum);

            if (from == null || to == null) throw new RuntimeException("Account not found");
            if (from.getBalance() < amount) throw new RuntimeException("Insufficient funds in source account");

            from.setBalance(from.getBalance() - amount);
            to.setBalance(to.getBalance() + amount);

            session.merge(from);
            session.merge(to);

            BankTransaction tx1 = new BankTransaction("TRANSFER", amount, "Transfer to " + toAccNum, from);
            BankTransaction tx2 = new BankTransaction("TRANSFER", amount, "Transfer from " + fromAccNum, to);

            session.persist(tx1);
            session.persist(tx2);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public List<BankTransaction> getTransactionsForAccount(int accountId) {
        return txDAO.findByAccountId(accountId);
    }
}
