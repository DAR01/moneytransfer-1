package com.vlivin.moneytransfer.resources;

import com.vlivin.moneytransfer.model.Account;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Account resource.
 *
 * @author Vladimir Livin
 */
public class AccountResource extends BaseResource {
    private final Account account;
    private final int accountNo;

    public AccountResource(int accountNo, EntityManager entityManager) {
        super(entityManager);
        this.accountNo = accountNo;
        this.account = getEntityManager().find(Account.class, accountNo);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Account getAccount() {
        if (account == null)
            throw new NotFoundException(String.format("Account %d doesn't exist", accountNo));
        return account;
    }
    
    @Path("transactions")
    public TransactionsResource getTransactions() {
        return new TransactionsResource(this, getEntityManager());
    }

    @Override
    public String toString() {
        return String.valueOf(getAccount().getAccountNo());
    }
}
