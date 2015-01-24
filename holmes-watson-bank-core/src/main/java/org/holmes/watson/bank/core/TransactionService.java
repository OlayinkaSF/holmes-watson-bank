/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.holmes.watson.bank.core;

import java.math.BigDecimal;
import java.rmi.Remote;
import java.rmi.RemoteException;
import org.holmes.watson.bank.core.entity.Client;

/**
 *
 * @author Olayinka
 */
public interface TransactionService extends Remote {

    public static String SERVICE_NAME = HolmesWatson.SERVICE_PREFIX + "." + "TransactionService";

    public Message transferMoney(Client donor, Client recipient, BigDecimal amounut) throws RemoteException;

    public Message withdrawMoney(Client donor, BigDecimal amounut) throws RemoteException;

    public Message depositMoney(Client donor, BigDecimal amounut) throws RemoteException;

    public Message demandLoan(Client client, BigDecimal amount) throws RemoteException;

    public Message terminateLoan(Client client, int loanId) throws RemoteException;

    public Message payLoan(Client client, Integer loanId) throws RemoteException;

}
