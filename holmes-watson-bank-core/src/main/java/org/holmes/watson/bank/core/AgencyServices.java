/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.holmes.watson.bank.core;

import org.holmes.watson.bank.core.auth.AuthService;

/**
 *
 * @author Olayinka
 */
public class AgencyServices {

    AccountService accountService;
    TransactionService transactionService;
    AuthService authService;

    public AgencyServices(AccountService accountService, TransactionService transactionService, AuthService authService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.authService = authService;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public TransactionService getTransactionService() {
        return transactionService;
    }

    public AuthService getAuthService() {
        return authService;
    }

}
