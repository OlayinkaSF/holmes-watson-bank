/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.holmes.watson.bank.client.view;

import java.math.BigDecimal;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import org.holmes.watson.bank.core.HolmesWatson;
import org.holmes.watson.bank.core.Message;
import org.holmes.watson.bank.core.TransactionService;

/**
 *
 * @author Olayinka
 */
public class Main {

    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(HolmesWatson.PORT);
        TransactionService transactionService = (TransactionService) registry.lookup(TransactionService.SERVICE_NAME);
        Message message = transactionService.depositMoney(null, BigDecimal.ZERO);
    }
}
