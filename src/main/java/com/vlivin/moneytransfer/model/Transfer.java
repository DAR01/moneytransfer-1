package com.vlivin.moneytransfer.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import javax.persistence.Embedded;

public class Transfer {
    private int fromAccount;
    @JsonUnwrapped
    private Money amount;
    private int toAccount;

    private boolean completed;

    public Transfer() {
    }

    public Transfer(int fromAccount, Money amount, int toAccount) {
        this.fromAccount = fromAccount;
        this.amount = amount;
        this.toAccount = toAccount;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public int getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(int fromAccount) {
        this.fromAccount = fromAccount;
    }

    public int getToAccount() {
        return toAccount;
    }

    public void setToAccount(int toAccount) {
        this.toAccount = toAccount;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
