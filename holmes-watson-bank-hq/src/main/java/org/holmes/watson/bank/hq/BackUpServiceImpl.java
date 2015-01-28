/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.holmes.watson.bank.hq;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import org.holmes.watson.bank.agency.entity.AccountJpaController;
import org.holmes.watson.bank.agency.entity.ClientJpaController;
import org.holmes.watson.bank.agency.entity.TransactionJpaController;
import org.holmes.watson.bank.agency.entity.exceptions.NonexistentEntityException;
import org.holmes.watson.bank.agency.service.BackUpService;
import org.holmes.watson.bank.core.Message;
import org.holmes.watson.bank.core.entity.Account;
import org.holmes.watson.bank.core.entity.Client;
import org.holmes.watson.bank.core.entity.Transaction;

/**
 *
 * @author Olayinka
 */
public class BackUpServiceImpl implements BackUpService {

    public static final BackUpService BACK_UP_SERVICE = new BackUpServiceImpl();
    private EntityManagerFactory managerFactory;

    @Override
    public Message saveOrUpdateClient(Client client) throws RemoteException {
        ClientJpaController cjc = new ClientJpaController(managerFactory);
        Client newClient = new Client(client.getClientid(), client.getFirstname(), client.getLastname(), client.getPassword(), client.getAddress());
        try {
            cjc.create(newClient);
        } catch (Exception ex) {
            Logger.getLogger(BackUpServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            cjc.edit(newClient);
        } catch (Exception ex) {
            Logger.getLogger(BackUpServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (Account account : client.getAccountList()) {
            AccountJpaController ajc = new AccountJpaController(managerFactory);
            Account newAccount = new Account(account.getAccountnum(), account.getAccountbalance(), account.getStatus());
            newAccount.setClientid(newClient);
            try {
                ajc.create(newAccount);
            } catch (Exception ex) {
                Logger.getLogger(BackUpServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                ajc.edit(newAccount);
            } catch (Exception ex) {
                Logger.getLogger(BackUpServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            for (Transaction transaction : account.getTransactionList()) {
                TransactionJpaController tjc = new TransactionJpaController(managerFactory);
                Transaction newTransaction = new Transaction(transaction.getTransactionid(), transaction.getDescription(), transaction.getAmount(), transaction.getTransactiondate(), transaction.getTransactiontype());
                newTransaction.setAccountnum(newAccount);
                try {
                    tjc.create(newTransaction);
                } catch (Exception ex) {
                    Logger.getLogger(BackUpServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    tjc.edit(newTransaction);
                } catch (Exception ex) {
                    Logger.getLogger(BackUpServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return Message.builder(true).build();

    }

    void setEmf(EntityManagerFactory managerFactory) {
        this.managerFactory = managerFactory;
    }

}
