package com.vlivin.moneytransfer.model;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Verifies Money behavior.
 *
 * @author Vladimir Livin
 */
public class MoneyTest {
    private static final Money _100USD = Money.of("100", "USD");
    private static final Money _50USD = Money.of("50", "USD");

    @Test
    public void createMoneyWithAmountAndCurrency() {
        Money money = Money.of("100.00", "USD");
        assertEquals(new BigDecimal("100.00"), money.getAmount());
        assertEquals(Currency.getInstance("USD"), money.getCurrency());
    }

    @Test
    public void moneyWithSameAmountAndCurrencyShouldBeEqual() {
        assertEquals(
            Money.of("100", "USD"),
            Money.of("100", "USD"));
    }

    @Test
    public void addMoneyWithSameCurrencyGivesMoneyWithAmountAdded() {
        Money _150USD = Money.of("150", "USD");
        assertEquals(_150USD, _100USD.add(_50USD));
    }

    @Test
    public void addMoneyWithOtherCurrencyShouldThrow() {
        Money _50RUB = new Money(new BigDecimal("50"), Currency.getInstance("RUB"));
        try {
            _100USD.add(_50RUB);
            fail("Adding different currencies should throw");
        } catch (IllegalArgumentException e) {
            assertEquals("Same currency expected", e.getMessage());
        }
    }
}
