package com.vlivin.moneytransfer.services;

import com.vlivin.moneytransfer.model.Account;
import com.vlivin.moneytransfer.model.Transfer;
import com.vlivin.moneytransfer.persistence.PersistenceUnit;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

@Singleton
public class TransferService {
    private PersistenceUnit persistenceUnit;

    @Inject
    public TransferService(PersistenceUnit persistenceUnit) {
        this.persistenceUnit = persistenceUnit;
    }

    public Transfer transfer(Transfer transfer) {
        EntityManager em = persistenceUnit.createEntityManager();
        try {
            em.getTransaction().begin();

            Account fromAccount = em.find(Account.class, transfer.getFromAccount());
            Account toAccount = em.find(Account.class, transfer.getToAccount());

            em.lock(fromAccount, LockModeType.OPTIMISTIC);
            em.lock(toAccount, LockModeType.OPTIMISTIC);
            try {
                fromAccount.transfer(transfer.getAmount(), toAccount);
            } catch (IllegalStateException e) {
                em.getTransaction().rollback();
                throw e;
            }

            em.merge(fromAccount);
            em.merge(toAccount);

            em.getTransaction().commit();
            transfer.setCompleted(true);
        } finally {
            em.close();
        }

        return transfer;
    }
}
