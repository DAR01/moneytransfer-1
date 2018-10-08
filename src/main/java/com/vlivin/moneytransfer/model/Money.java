package com.vlivin.moneytransfer.model;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

/**
 * Represents Money with amount and currency.
 *
 * @author Vladimir Livin
 */
@Embeddable
public class Money {
    private BigDecimal amount;
    private Currency currency;

    public static Money of(String amount, String currency) {
        return new Money(new BigDecimal(amount), Currency.getInstance(currency));
    }

    public Money(BigDecimal amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public Money() {
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Money add(Money money) {
        checkCurrencyArgument(money);
        return new Money(getAmount().add(money.getAmount()), getCurrency());
    }

    private void checkCurrencyArgument(Money money) {
        if (!getCurrency().equals(money.getCurrency()))
            throw new IllegalArgumentException("Same currency expected");
    }

    public Money substract(Money money) {
        return new Money(getAmount().subtract(money.getAmount()), getCurrency());
    }

    public Money negate() {
        return new Money(getAmount().negate(), getCurrency());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return getAmount().equals(money.getAmount()) &&
                getCurrency().equals(money.getCurrency());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAmount(), getCurrency());
    }

    @Override
    public String toString() {
        return getAmount() + " " + getCurrency();
    }
}
