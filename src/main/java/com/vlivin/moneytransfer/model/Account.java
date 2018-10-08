package com.vlivin.moneytransfer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import java.util.ArrayList;
import java.util.List;

/**
 * Account model which has accountNo and balance.
 *
 * It also maintains transactions list.
 *
 * Account supports withdraw and deposit operations
 * which results in addition necessary transactions.
 *
 * It also provide logic to tranfer money to another account.
 *
 * @author Vladimir Livin
 */
@Entity
public class Account extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int accountNo;

    @JsonUnwrapped
    @Embedded
    private Money balance = Money.of("0.0", "EUR");

    @Version
    private Integer version;

    @OneToMany(
            mappedBy = "account",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<Transaction> transactions = new ArrayList<>();

    public Account(int accountNo) {
        setAccountNo(accountNo);
    }

    public Account() {
    }

    public Account(Money balance) {
        this.balance = balance;
    }

    public int getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(int accountNo) {
        this.accountNo = accountNo;
    }

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public void transfer(Money amount, Account toAccount) {
        if (getBalance().getAmount().compareTo(amount.getAmount()) < 0) {
            throw new IllegalStateException("Account " + getAccountNo() + " has insufficient funds.");
        }

        withdraw(amount);
        setBalance(getBalance().substract(amount));

        toAccount.deposit(amount);
        toAccount.setBalance(toAccount.getBalance().add(amount));
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void deposit(Money amount) {
        addTransaction(amount);
    }

    private void withdraw(Money amount) {
        addTransaction(amount.negate());
    }

    private void addTransaction(Money amount) {
        getTransactions().add(new Transaction(this, amount));
    }
}
