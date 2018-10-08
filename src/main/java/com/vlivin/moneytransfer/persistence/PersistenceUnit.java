package com.vlivin.moneytransfer.persistence;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * PersistanceUnit singleton helper which provides access to
 * entity manager factory and entity manager.
 *
 * Usually we would use CDI and @PersistenceContext annotation, but
 * since we're neither using CDI nor J2EE container we have to provide
 * a way to access our persistence unit. This is what this class does.
 *
 * @author Vladimir Livin
 */
@Singleton
public class PersistenceUnit {
    /**
     * Name of our persistence unit as defined in persistence.xml
     */
    private static final String PERSISTENCE_UNIT = "accounts";

    private static PersistenceUnit ourInstance;
    private EntityManagerFactory entityManagerFactory;
    private String unit;

    private PersistenceUnit(String unit) {
        this.unit = unit;
    }

    public PersistenceUnit() {
        this(PERSISTENCE_UNIT);
        ourInstance = this;
    }

    public static PersistenceUnit getInstance() {
        if (ourInstance == null) {
            return new PersistenceUnit();
        }
        return ourInstance;
    }

    /**
     * Convenience method which provides an instance of EntityManager.
     * @return a fresh new instance of EntityManager for your queries.
     */
    public static EntityManager getEntityManager() {
        return getInstance().createEntityManager();
    }

    public EntityManager createEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    /**
     * Provides EntityManagerFactory for this persistence unit.
     * It lazily creates a single instance of EntityManagerFactory
     * or returns existing one.
     *
     * @return EntityManagerFactory for this persistence unit.
     */
    public synchronized EntityManagerFactory getEntityManagerFactory() {
        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory(unit);
        }
        return entityManagerFactory;
    }

    /**
     * Closing entity manager factory is helpful in unit testing
     * because it can reset database after executing test if appropriate
     * property 'hibernate.hbm2ddl.auto' was set to 'create-drop'.
     */
    public void closeEntityManagerFactory() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
            entityManagerFactory = null;
        }
    }
}
