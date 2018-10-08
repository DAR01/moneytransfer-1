package com.vlivin.moneytransfer;

import com.vlivin.moneytransfer.client.MoneyTransferClient;
import com.vlivin.moneytransfer.model.Account;
import com.vlivin.moneytransfer.model.Money;
import com.vlivin.moneytransfer.model.Transaction;
import com.vlivin.moneytransfer.model.Transfer;
import com.vlivin.moneytransfer.persistence.PersistenceUnit;
import com.vlivin.moneytransfer.server.Main;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AccountsIntegrationTest {

    private HttpServer server;
    private MoneyTransferClient api;

    @Before
    public void setUp() {
        // start the server
        server = Main.startServer();
        // create the client
        api = new MoneyTransferClient(Main.BASE_URI);
    }

    @After
    public void tearDown() {
        server.shutdownNow();
        PersistenceUnit.getInstance().closeEntityManagerFactory();
    }

    @Test
    public void createAccountShouldGiveNewAccount() {
        Account account = api.createAccount();
        assertNotNull(account);
        assertEquals(1, account.getAccountNo());
    }

    @Test
    public void getAccountByAccountNo_whenAccountCreated_ShouldGiveAccount() {
        api.createAccount();

        Account account = api.getAccountByNo(1);
        assertNotNull(account);
        assertEquals(1, account.getAccountNo());
    }

    @Test
    public void getAccountByAccountNo_whenThereIsNoSuchAccount_ShouldGiveNotFound() {
        Response response = api.accountByNo(1).get();
        assertEquals(HttpStatus.NOT_FOUND_404.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void transferFromAccountShouldTransferMoney() {
        Account fromAccount = api.createAccount(new Account(Money.of("1000.0", "EUR")));
        Account toAccount = api.createAccount(new Account(Money.of("1000.0", "EUR")));

        Response response = api.transfer(new Transfer(fromAccount.getAccountNo(), Money.of("100.00", "EUR"), toAccount.getAccountNo()));
        assertEquals(HttpStatus.OK_200.getStatusCode(), response.getStatus());

        fromAccount = api.getAccountByNo(fromAccount.getAccountNo());
        toAccount = api.getAccountByNo(toAccount.getAccountNo());

        assertEquals(Money.of("900.0", "EUR"), fromAccount.getBalance());
        assertEquals(Money.of("1100.0", "EUR"), toAccount.getBalance());
    }

    @Test
    public void transferMoreThanAccountBalanceShouldGiveError() {
        Account fromAccount = api.createAccount(new Account(Money.of("1000.0", "EUR")));
        Account toAccount = api.createAccount(new Account(Money.of("1000.0", "EUR")));

        Response response = api.transfer(new Transfer(fromAccount.getAccountNo(), Money.of("2000.00", "EUR"), toAccount.getAccountNo()));

        assertEquals(HttpStatus.BAD_REQUEST_400.getStatusCode(), response.getStatus());

        fromAccount = api.getAccountByNo(fromAccount.getAccountNo());
        assertEquals(Money.of("1000.0", "EUR"), fromAccount.getBalance());
    }

    @Test
    public void accountTransactionShouldProvideListOfTransactions() {
        Account account = api.createAccount(new Account(Money.of("1000.0", "EUR")));
        Account anotherAccount = api.createAccount(new Account(Money.of("1000.0", "EUR")));

        // Do sample transfers
        for (int i = 1; i <= 3; i++)
            api.transfer(new Transfer(account.getAccountNo(), Money.of("50.00", "EUR"), anotherAccount.getAccountNo()));

        List<Transaction> transactions = api.getAccountTransactions(account.getAccountNo());

        assertEquals("Account should have 3 transactions", 3, transactions.size());
        assertEquals("First transaction amount should be -50.0 EUR", Money.of("-50.0", "EUR"), transactions.get(0).getAmount());
    }
}
