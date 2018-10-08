package com.vlivin.moneytransfer.client;

import com.vlivin.moneytransfer.model.Account;
import com.vlivin.moneytransfer.model.Transaction;
import com.vlivin.moneytransfer.model.Transfer;
import com.vlivin.moneytransfer.services.TransferService;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.*;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Money Transfer API Client.
 *
 * @author Vladimir Livin
 */
public class MoneyTransferClient {
    private final WebTarget target;
    
    private static final GenericType<List<Transaction>> TRANSACTIONS_LIST_TYPE = new GenericType<List<Transaction>>() {};

    /**
     * Creates new Money Transfer API Client.
     *
     * @param baseUri API URL, e.g. http://localhost:8080/moneytransfer/.
     */
    public MoneyTransferClient(String baseUri) {
        Client client = ClientBuilder.newClient();
        client.register(TransferService.class);
        client.register(JacksonFeature.class);

        target = client.target(baseUri);
    }

    /**
     * POSTs to /accounts/ to create new account.
     *
     * @return a newly created Account.
     */
    public Account createAccount() {
        return createAccount(null);
    }


    /**
     * POSTs to /accounts/ to create new account.
     *
     * @return a newly created Account.
     */
    public Account createAccount(Account requested) {
        return target.path("accounts").request(MediaType.APPLICATION_JSON).post(Entity.json(requested), Account.class);
    }

    /**
     * Builds /accounts/{accountNo} path.
     *
     * @param accountNo an accountNo.
     * @return request builder to get account by accountNo.
     */
    public Invocation.Builder accountByNo(int accountNo) {
        return target.path("accounts/" + accountNo).request(MediaType.APPLICATION_JSON);
    }

    /**
     * GETs account from /accounts/{accountNo}.
     *
     * @param accountNo an accountNo to get account.
     * @return the Account with given accountNo.
     */
    public Account getAccountByNo(int accountNo) {
        return accountByNo(accountNo).get(Account.class);
    }

    /**
     * POSTs /accounts/{accountNo} with transfer details which transfer
     * money from originating account to account specified in
     * transfer transaction details.
     *
     * @param transfer transfer transaction details.
     * @return an Response.
     */
    public Response transfer(Transfer transfer) {
        return target.path("transfer").request(MediaType.APPLICATION_JSON).post(Entity.json(transfer));
    }

    /**
     * Returns a list of transactions for given account.
     * 
     * @param accountNo an accountNo.
     * @return a list of all transaction for given account.
     */
    public List<Transaction> getAccountTransactions(int accountNo) {
        return target.path("accounts/" + accountNo + "/transactions").request(MediaType.APPLICATION_JSON).get(TRANSACTIONS_LIST_TYPE);
    }
}
