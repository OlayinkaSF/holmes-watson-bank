/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.holmes.watson.bank.core;

import java.math.BigDecimal;
import java.rmi.Remote;
import java.rmi.RemoteException;
import org.holmes.watson.bank.core.entity.Account;
import org.holmes.watson.bank.core.entity.Transaction;

/**
 *
 * @author Olayinka
 */
public interface TransactionService extends Remote {

    public static String SERVICE_NAME = HolmesWatson.SERVICE_PREFIX + "." + "TransactionService";

    public Message transferMoney(Account donor, Account recipient, BigDecimal amounut) throws RemoteException;

    public Message withdrawMoney(Account donor, BigDecimal amounut, String description) throws RemoteException;

    public Message depositMoney(Account donor, BigDecimal amounut, String description) throws RemoteException;

    public Message cancelTransaction(Transaction transaction) throws RemoteException;

}
