/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.holmes.watson.bank.core;

import java.math.BigDecimal;
import org.holmes.watson.bank.core.entity.Client;

/**
 *
 * @author Olayinka
 */
public interface TransactionListener {

    public void transferMoney(Client donor, Client recipient, BigDecimal amounut);

    public void withdrawMoney(Client donor, BigDecimal amounut);

    public void depositMoney(Client donor, BigDecimal amounut);

    public void demandLoan(Client client, BigDecimal amount);

    public void terminateLoan(Client client, int loanId);

    public void payLoan(Client client, Integer loanId);

}
