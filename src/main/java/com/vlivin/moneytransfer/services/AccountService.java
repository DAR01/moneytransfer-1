package com.vlivin.moneytransfer.services;

import com.vlivin.moneytransfer.model.Account;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

/**
 * Service that manages accounts.
 *
 * @author Vladimir Livin
 */
@Singleton
public class AccountService {
    /**
     * A list of currencies supported by our system.
     */
    private final static List<String> supportedCurrencies = Arrays.asList(
        "USD", "EUR", "RUB"
    );

    /**
     * Creates new account based on requested account.
     *
     * Requested account can specify desired balance.
     *
     * If no account will be specified it will create default
     * empty account in Euro currency.
     *
     * @param requested an account with properties requested to create
     * @return an actual account created based on original request.
     */
    public Account create(Account requested) {
        Account account = new Account();

        if (requested != null) {
            if (requested.getBalance() != null) {
                verifyCurrencySupported(requested.getBalance().getCurrency());

                account.setBalance(requested.getBalance());
            }
        }

        account.save();
        return account;
    }

    private void verifyCurrencySupported(Currency currency) {
        if (currency == null)
            throw new IllegalArgumentException("Currency should be specified.");

        if (!supportedCurrencies.contains(currency.getCurrencyCode())) {
            throw new IllegalArgumentException(
                String.format("Unsupported currency: %s. Supported currencies are: %s.",
                    currency.getCurrencyCode(),
                    String.join(", ", supportedCurrencies)
                ));
        }
    }
}
