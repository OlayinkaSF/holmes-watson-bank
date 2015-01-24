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
import org.holmes.watson.bank.core.entity.Agency;

/**
 *
 * @author Olayinka
 */
public class AgencyJpaController implements Serializable {

    public AgencyJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Agency agency) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(agency);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAgency(agency.getAgencyid()) != null) {
                throw new PreexistingEntityException("Agency " + agency + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Agency agency) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            agency = em.merge(agency);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = agency.getAgencyid();
                if (findAgency(id) == null) {
                    throw new NonexistentEntityException("The agency with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Agency agency;
            try {
                agency = em.getReference(Agency.class, id);
                agency.getAgencyid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The agency with id " + id + " no longer exists.", enfe);
            }
            em.remove(agency);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Agency> findAgencyEntities() {
        return findAgencyEntities(true, -1, -1);
    }

    public List<Agency> findAgencyEntities(int maxResults, int firstResult) {
        return findAgencyEntities(false, maxResults, firstResult);
    }

    private List<Agency> findAgencyEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Agency.class));
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

    public Agency findAgency(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Agency.class, id);
        } finally {
            em.close();
        }
    }

    public int getAgencyCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Agency> rt = cq.from(Agency.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
