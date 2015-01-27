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
    private ClientJpaController clientController;
    private AccountJpaController accountController;
    private AgencyJpaController agencyController;
    private TransactionJpaController transactionController;

    public void setHqService(TransactionService hqService) {
        this.hqService = hqService;
    }

    @Override
    public Message transferMoney(Account donor, Account recipient, BigDecimal amounut) throws RemoteException {

        // verify that transfer is on different accounts
        if (donor.getAccountnum().equals(recipient.getAccountnum())) {
            return Message.builder(false)
                    .message("Donor and recipient can't be the same")
                    .build();
        }

        Message withdrawMessage;
        Message depositMessage;
        TransactionService withdrawService = this;
        TransactionService depositService = this;

        //Get the necessary services for execution, preference for local service 
        //to speed up execution
        if (!Utils.isMyAgency(recipient.getAccountnum())) {
            depositService = hqService;
        }
        if (!Utils.isMyAgency(donor.getAccountnum())) {
            withdrawService = hqService;
        }

        //transfer money to receipient
        //this makes the rollback easier
        depositMessage = depositService.depositMoney(recipient, amounut, "Transfer from " + donor.getAccountnum());

        //verify transfer status
        if (depositMessage.getStatus()) {

            //withdraw money from donor
            withdrawMessage = withdrawService.withdrawMoney(donor, amounut, "Transfer to " + recipient.getAccountnum());

            //if withdraw fails, it's time to rollback
            if (!withdrawMessage.getStatus()) {
                //rollback transaction
                depositService.cancelTransaction((Transaction) depositMessage.getAttachment()[0]);
                return Message.builder(false)
                        .message("Couldn't complete transaction")
                        .build();
            }

            return Message.builder(true)
                    .message("Transfer completed successfully")
                    .attachment(withdrawMessage.getAttachment()[2])
                    .build();
        }

        return Message.builder(false)
                .message("Couldn't complete transaction")
                .build();
    }

    void setEmf(EntityManagerFactory managerFactory) {
        this.emf = managerFactory;
        clientController = new ClientJpaController(emf);
        accountController = new AccountJpaController(emf);
        agencyController = new AgencyJpaController(emf);
        transactionController = new TransactionJpaController(emf);
    }

    @Override
    public Message withdrawMoney(Account donor, BigDecimal amounut, String description) throws RemoteException {
        if (donor.getAccountbalance().compareTo(amounut) <= 0) {
            return Message.builder(false)
                    .message("Insufficient balance for transaction.")
                    .build();
        }

        if (!Utils.isMyAgency(donor.getAccountnum())) {
            return hqService.withdrawMoney(donor, amounut, description);
        }

        donor = accountController.findAccount(donor.getAccountnum());

        try {
            Transaction transaction = new Transaction(Long.MIN_VALUE, description, amounut, new Date(), Transaction.WITHDRAW);
            transaction.setAccountnum(donor);
            donor.setAccountbalance(donor.getAccountbalance().subtract(amounut));
            accountController.edit(donor);
            transactionController.create(transaction);
            Client client = clientController.findClient(donor.getClientid().getClientid());
            return Message.builder(true)
                    .message("Transaction completed")
                    .attachment(transaction, donor, client)
                    .build();

        } catch (Exception ex) {
            Logger.getLogger(TransactionServiceImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return Message.builder(false)
                .message("Invalid transaction.")
                .build();
    }

    @Override
    public Message depositMoney(Account account, BigDecimal amounut, String description) throws RemoteException {

        if (!Utils.isMyAgency(account.getAccountnum())) {
            return hqService.withdrawMoney(account, amounut, description);
        }

        account = accountController.findAccount(account.getAccountnum());
        
        System.out.println(account);

        try {
            Transaction transaction = new Transaction(Long.MIN_VALUE, description, amounut, new Date(), Transaction.DEPOSIT);
            transaction.setAccountnum(account);
            account.setAccountbalance(account.getAccountbalance().add(amounut));
            accountController.edit(account);
            transactionController.create(transaction);
            Client client = clientController.findClient(account.getClientid().getClientid());
            return Message.builder(true)
                    .message("Transaction completed")
                    .attachment(transaction, account, client)
                    .build();

        } catch (Exception ex) {
            Logger.getLogger(TransactionServiceImpl.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return Message.builder(false)
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
                    .message("Transaction cancelled successfully")
                    .attachment(transaction.getAccountnum())
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
