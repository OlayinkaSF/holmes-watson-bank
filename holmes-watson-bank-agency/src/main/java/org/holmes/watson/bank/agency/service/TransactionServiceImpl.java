/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.holmes.watson.bank.agency.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import javax.persistence.EntityManagerFactory;
import org.holmes.watson.bank.core.Message;
import org.holmes.watson.bank.core.TransactionService;
import org.holmes.watson.bank.core.entity.Client;

/**
 *
 * @author Olayinka
 */
public class TransactionServiceImpl implements TransactionService {

    final static TransactionService TRANSACTION_SERVICE = new TransactionServiceImpl();
    TransactionService hqService;
    private EntityManagerFactory emf;

    public void setHqService(TransactionService hqService) {
        this.hqService = hqService;
    }

    @Override
    public Message transferMoney(Client donor, Client recipient, BigDecimal amounut) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Message withdrawMoney(Client donor, BigDecimal amounut) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Message depositMoney(Client donor, BigDecimal amounut) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Message demandLoan(Client client, BigDecimal amount) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Message terminateLoan(Client client, int loanId) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Message payLoan(Client client, Integer loanId) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void setEmf(EntityManagerFactory managerFactory) {
        this.emf = managerFactory;
    }

}
