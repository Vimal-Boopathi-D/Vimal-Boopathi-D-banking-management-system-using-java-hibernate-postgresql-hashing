package com.bank.dao;

import com.bank.entity.BankTransaction;
import com.bank.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class BankTransactionDAO {

    public void save(BankTransaction tx) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.getTransaction().begin();
            session.persist(tx);
            session.getTransaction().commit();
        } catch (Exception e) {
            throw e;
        }
    }

    public List<BankTransaction> findByAccountId(int accountId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<BankTransaction> q = session.createQuery("FROM BankTransaction t WHERE t.account.accountId = :aid ORDER BY t.dateTime DESC", BankTransaction.class);
            q.setParameter("aid", accountId);
            return q.list();
        }
    }
}
