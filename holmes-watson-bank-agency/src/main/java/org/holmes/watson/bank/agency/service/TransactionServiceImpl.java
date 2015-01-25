/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.holmes.watson.bank.agency.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import org.holmes.watson.bank.agency.entity.AccountJpaController;
import org.holmes.watson.bank.agency.entity.AgencyJpaController;
import org.holmes.watson.bank.agency.entity.ClientJpaController;
import org.holmes.watson.bank.agency.entity.TransactionJpaController;
import org.holmes.watson.bank.agency.entity.exceptions.NonexistentEntityException;
import static org.holmes.watson.bank.agency.service.AccountServiceImpl.ACCOUNT_SERVICE;
import org.holmes.watson.bank.core.AccountService;
import org.holmes.watson.bank.core.Message;
import org.holmes.watson.bank.core.TransactionService;
import org.holmes.watson.bank.core.Utils;
import org.holmes.watson.bank.core.entity.Account;
import org.holmes.watson.bank.core.entity.Client;
import org.holmes.watson.bank.core.entity.Transaction;

/**
 *
 * @author Olayinka
 */
public class TransactionServiceImpl implements TransactionService {

    final static TransactionService TRANSACTION_SERVICE = new TransactionServiceImpl();
    TransactionService hqService;
    private EntityManagerFactory emf;

    ClientJpaController clientController;
    AccountJpaController accountController;
    AgencyJpaController agencyController;
    TransactionJpaController transactionController;

    public void setHqService(TransactionService hqService) {
        this.hqService = hqService;
    }

    @Override
    public Message transferMoney(Account donor, Account recipient, BigDecimal amounut) throws RemoteException {
        Message withdrawMessage;
        Message depositMessage;
        TransactionService withdrawService = this;
        TransactionService depositService = this;

        if (!Utils.isMyAgency(recipient.getAccountnum())) {
            depositService = hqService;
        }
        if (!Utils.isMyAgency(donor.getAccountnum())) {
            withdrawService = hqService;
        }
        depositMessage = depositService.depositMoney(recipient, amounut, "Transfer from " + donor.getAccountnum());
        if (depositMessage.getStatus()) {
            withdrawMessage = withdrawService.depositMoney(donor, amounut, "Transfer to " + recipient.getAccountnum());
            if (!withdrawMessage.getStatus()) {
                depositService.cancelTransaction((Transaction) depositMessage.getAttachment()[0]);
                return Message.builder(false)
                        .message("Invalid transaction")
                        .build();
            }
            return Message.builder(true)
                    .message("Transfer completed successfully")
                    .build();
        }
        return Message.builder(false)
                .message("Invalid transaction")
                .build();
    }

    void setEmf(EntityManagerFactory managerFactory) {
        this.emf = managerFactory;
    }

    @Override
    public Message withdrawMoney(Account account, BigDecimal amounut, String description) throws RemoteException {
        try {
            Transaction transaction = new Transaction(Long.MIN_VALUE, description, amounut, new Date(), Transaction.WITHDRAW);
            transaction.setAccountnum(account);
            transactionController.create(transaction);
            return Message.builder(true)
                    .message("Transaction completed")
                    .attachment(transaction)
                    .build();

        } catch (Exception ex) {
            Logger.getLogger(TransactionServiceImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return Message.builder(true)
                .message("Invalid transaction.")
                .build();
    }

    @Override
    public Message depositMoney(Account account, BigDecimal amounut, String description) throws RemoteException {
        try {
            Transaction transaction = new Transaction(Long.MIN_VALUE, description, amounut, new Date(), Transaction.DEPOSIT);
            transaction.setAccountnum(account);
            transactionController.create(transaction);
            return Message.builder(true)
                    .message("Transaction completed")
                    .attachment(transaction)
                    .build();

        } catch (Exception ex) {
            Logger.getLogger(TransactionServiceImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return Message.builder(true)
                .message("Invalid transaction.")
                .build();
    }

    @Override
    public Message cancelTransaction(Transaction transaction) throws RemoteException {
        try {
            switch (transaction.getTransactiontype()) {
                case Transaction.WITHDRAW:
                    transaction.getAccountnum().setAccountbalance(transaction.getAccountnum().getAccountbalance().add(transaction.getAmount()));
                    break;
                case Transaction.DEPOSIT:
                    transaction.getAccountnum().setAccountbalance(transaction.getAccountnum().getAccountbalance().subtract(transaction.getAmount()));
                    break;
            }
            transactionController.destroy(transaction.getTransactionid());
            return Message.builder(false)
                    .message("You've no right to this request.")
                    .attachment(accountController.findAccount(transaction.getAccountnum().getAccountnum()))
                    .build();
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(TransactionServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return Message.builder(false)
                    .message("You've no right to this request.")
                    .build();
        }
    }

     public static TransactionService getLocalService() {
        return TRANSACTION_SERVICE;
    }
}
