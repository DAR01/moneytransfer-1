package com.vlivin.moneytransfer.model;

import com.vlivin.moneytransfer.persistence.PersistenceUnit;

import javax.persistence.EntityManager;

/**
 * Generic model class which can be reused by model subclasses.
 *
 * The main intention of this class is to allow more transparent
 * persistence.
 *
 * @author Vladimir Livin
 */
public class Model {
    /**
     * Saves entity this given entity manager.
     *
     * @param em an EntityManager.
     */
    public void save(EntityManager em) {
        em.getTransaction().begin();
        if (em.contains(this)) {
            em.merge(this);
        } else {
            em.persist(this);
        }
        em.getTransaction().commit();
    }

    /**
     * Saves entity and requests entity manager form persistence unit.
     */
    public void save() {
        EntityManager em = PersistenceUnit.getInstance().createEntityManager();
        try {
            save(em);
        } finally {
            em.close();
        }
    }
}
