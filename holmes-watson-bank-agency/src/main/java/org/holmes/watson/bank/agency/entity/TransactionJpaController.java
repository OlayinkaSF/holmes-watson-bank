/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.holmes.watson.bank.agency.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.holmes.watson.bank.agency.entity.exceptions.NonexistentEntityException;
import org.holmes.watson.bank.agency.entity.exceptions.PreexistingEntityException;
import org.holmes.watson.bank.core.entity.Account;
import org.holmes.watson.bank.core.entity.Transaction;

/**
 *
 * @author Olayinka
 */
public class TransactionJpaController implements Serializable {

    public TransactionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Transaction transaction) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Account accountnum = transaction.getAccountnum();
            if (accountnum != null) {
                accountnum = em.getReference(accountnum.getClass(), accountnum.getAccountnum());
                transaction.setAccountnum(accountnum);
            }
            em.persist(transaction);
            if (accountnum != null) {
                accountnum.getTransactionList().add(transaction);
                accountnum = em.merge(accountnum);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTransaction(transaction.getTransactionid()) != null) {
                throw new PreexistingEntityException("Transaction " + transaction + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Transaction transaction) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Transaction persistentTransaction = em.find(Transaction.class, transaction.getTransactionid());
            Account accountnumOld = persistentTransaction.getAccountnum();
            Account accountnumNew = transaction.getAccountnum();
            if (accountnumNew != null) {
                accountnumNew = em.getReference(accountnumNew.getClass(), accountnumNew.getAccountnum());
                transaction.setAccountnum(accountnumNew);
            }
            transaction = em.merge(transaction);
            if (accountnumOld != null && !accountnumOld.equals(accountnumNew)) {
                accountnumOld.getTransactionList().remove(transaction);
                accountnumOld = em.merge(accountnumOld);
            }
            if (accountnumNew != null && !accountnumNew.equals(accountnumOld)) {
                accountnumNew.getTransactionList().add(transaction);
                accountnumNew = em.merge(accountnumNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                long id = transaction.getTransactionid();
                if (findTransaction(id) == null) {
                    throw new NonexistentEntityException("The transaction with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Transaction transaction;
            try {
                transaction = em.getReference(Transaction.class, id);
                transaction.getTransactionid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The transaction with id " + id + " no longer exists.", enfe);
            }
            Account accountnum = transaction.getAccountnum();
            if (accountnum != null) {
                accountnum.getTransactionList().remove(transaction);
                accountnum = em.merge(accountnum);
            }
            em.remove(transaction);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Transaction> findTransactionEntities() {
        return findTransactionEntities(true, -1, -1);
    }

    public List<Transaction> findTransactionEntities(int maxResults, int firstResult) {
        return findTransactionEntities(false, maxResults, firstResult);
    }

    private List<Transaction> findTransactionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Transaction.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Transaction findTransaction(long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Transaction.class, id);
        } finally {
            em.close();
        }
    }

    public int getTransactionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Transaction> rt = cq.from(Transaction.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
