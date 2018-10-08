package com.vlivin.moneytransfer.model;

import com.vlivin.moneytransfer.persistence.PersistenceUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static com.vlivin.moneytransfer.persistence.PersistenceUnit.getEntityManager;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Verifies that persistence works correctly.
 *
 * @author Vladimir Livin
 */
public class AccountPersistenceTest {
    private EntityManager db;

    private static final Money _100USD = new Money(new BigDecimal("100.00"), Currency.getInstance("USD"));

    @Before
    public void setUp() {
        db = getEntityManager();
    }

    @After
    public void tearDown() {
        db.close();
        // This will trigger database reset after test finished.
        PersistenceUnit.getInstance().closeEntityManagerFactory();
    }

    @Test
    public void saveOneAccount() {
        Account account = new Account();
        account.save(db);

        Account foundAccount = db.find(Account.class, account.getAccountNo());
        assertNotNull(foundAccount);
    }

    @Test
    public void accountSavedWithOneEntityManagerShouldAvailableWithAnother() {
        EntityManager anotherEm = PersistenceUnit.getInstance().createEntityManager();
        Account account = new Account();
        account.save(db);

        Account foundAccount = anotherEm.find(Account.class, account.getAccountNo());
        assertNotNull(foundAccount);
    }
    
    @Test
    public void saveTwoAccounts() {
        new Account().save(db);
        new Account().save(db);
        
        List<Account> list = db.createQuery("from Account", Account.class).getResultList();

        assertEquals(2, list.size());
        assertEquals(1, list.get(0).getAccountNo());
        assertEquals(2, list.get(1).getAccountNo());
    }

    @Test
    public void saveAccountWithTransaction() {
        Account account = new Account(Money.of("0.0", "USD"));
        account.getTransactions().add(new Transaction(account, _100USD));

        account.save(db);

        Account reloadedAccount = db.find(Account.class, account.getAccountNo());
        assertEquals(1, reloadedAccount.getTransactions().size());
        assertEquals(_100USD,  reloadedAccount.getTransactions().get(0).getAmount());
    }
}
