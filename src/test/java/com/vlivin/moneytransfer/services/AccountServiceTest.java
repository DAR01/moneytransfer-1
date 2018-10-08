package com.vlivin.moneytransfer.services;

import com.vlivin.moneytransfer.model.Account;
import com.vlivin.moneytransfer.model.Money;
import com.vlivin.moneytransfer.persistence.PersistenceUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Verifies AccountService behavior.
 *
 * @author Vladimir Livin
 */
public class AccountServiceTest {

    private AccountService accountService;

    @Before
    public void setUp() {
        accountService = new AccountService();
    }

    @After
    public void tearDown() {
        PersistenceUnit.getInstance().closeEntityManagerFactory();
    }

    @Test
    public void create_whenNoAccountSpecified_shouldCreateEmptyEuroAccount() {
        Account createdAccount = accountService.create(null);
        assertEquals(Money.of("0.0", "EUR"), createdAccount.getBalance());
    }

    @Test
    public void create_whenAccountSpecified_shouldApplyGivenBalance() {
        Account requested = new Account();
        requested.setBalance(Money.of("2000.0", "EUR"));
        Account createdAccount = accountService.create(requested);
        assertEquals(Money.of("2000.0", "EUR"), createdAccount.getBalance());
    }

    @Test
    public void create_whenUnsupportedCurrencySpecified_shouldThrow() {
        Account requested = new Account();
        requested.setBalance(Money.of("1000.0", "CAD"));
        try {
            accountService.create(requested);
            fail("Create account with unsupported currency should throw");
        } catch (Exception e) {
            assertEquals("Unsupported currency: CAD. Supported currencies are: USD, EUR, RUB.", e.getMessage());
        }
    }
}
