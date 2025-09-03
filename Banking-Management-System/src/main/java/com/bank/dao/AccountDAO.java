package com.bank.dao;

import com.bank.entity.Account;
import com.bank.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class AccountDAO {

    public void save(Account account) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(account);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public Account findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Account.class, id);
        }
    }

    public Account findByAccountNumber(String accountNumber) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Account> q = session.createQuery("FROM Account a WHERE a.accountNumber = :accNum", Account.class);
            q.setParameter("accNum", accountNumber);
            return q.uniqueResult();
        }
    }

    public List<Account> findByUserId(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Account> q = session.createQuery("FROM Account a WHERE a.user.id = :uid", Account.class);
            q.setParameter("uid", userId);
            return q.list();
        }
    }

    public void update(Account account) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(account);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public List<Account> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Account> q = session.createQuery("FROM Account", Account.class);
            return q.list();
        }
    }
}
