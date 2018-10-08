package com.vlivin.moneytransfer.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Vladimir Livin
 */
public class AccountTest {
    private final static Money _100USD = Money.of("100.00", "USD");
    private static final Money _1000USD = Money.of("1000", "USD");

    @Test
    public void depositShouldAddDepositTransaction() {
        Account account = new Account(1);
        account.deposit(_100USD);

        assertEquals(1, account.getTransactions().size());
        Transaction transaction = account.getTransactions().get(0);
        assertEquals(account, transaction.getAccount());
        assertEquals(_100USD, transaction.getAmount());
    }

    @Test
    public void transferShouldCreateDepositAndWithdrawTransaction() {
        Account account1 = new Account(_1000USD);
        Account account2 = new Account(_1000USD);

        account1.transfer(_100USD, account2);

        Transaction withDrawTransaction = account1.getTransactions().get(0);
        assertEquals(_100USD.negate(), withDrawTransaction.getAmount());

        Transaction depositTransaction = account2.getTransactions().get(0);
        assertEquals(_100USD, depositTransaction.getAmount());
    }
}
