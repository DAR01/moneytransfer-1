package com.vlivin.moneytransfer.resources;

import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;

/**
 * Generic resource which provides some accessory
 * for all particular resources.
 *
 * Specifically made for managing JPA's EntityManager.
 *
 * @author Vladimir Livin
 */
public class BaseResource {
    private EntityManager entityManager;

    public BaseResource(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @PreDestroy
    public void release() {
        if (entityManager != null)
            entityManager.close();
    }
}
