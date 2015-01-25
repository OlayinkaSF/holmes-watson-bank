/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.holmes.watson.bank.hq;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import javax.persistence.EntityManagerFactory;
import org.holmes.watson.bank.core.AgencyServices;
import org.holmes.watson.bank.core.Message;
import org.holmes.watson.bank.core.TransactionService;
import org.holmes.watson.bank.core.Utils;
import org.holmes.watson.bank.core.entity.Account;
import org.holmes.watson.bank.core.entity.Transaction;

/**
 *
 * @author Olayinka
 */
public class TransactionServiceImpl implements TransactionService {

    final static TransactionService TRANSACTION_SERVICE = new TransactionServiceImpl();
    private EntityManagerFactory emf;

    @Override
    public Message transferMoney(Account donor, Account recipient, BigDecimal amounut) throws RemoteException {
        Message withdrawMessage;
        Message depositMessage;
        TransactionService withdrawService = ServiceRepo.getAgencyService(Utils.getAgencyId(donor.getAccountnum())).getTransactionService();
        TransactionService depositService = ServiceRepo.getAgencyService(Utils.getAgencyId(recipient.getAccountnum())).getTransactionService();;

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

    @Override
    public Message withdrawMoney(Account donor, BigDecimal amounut, String description) throws RemoteException {
        String agencyId = Utils.getAgencyId(donor.getAccountnum());
        AgencyServices services = ServiceRepo.getAgencyService(agencyId);
        if (services == null) {
            return Message.builder(false)
                    .message("Service unavailabale")
                    .build();
        }
        return services.getTransactionService().withdrawMoney(donor, amounut, description);
    }

    @Override
    public Message depositMoney(Account donor, BigDecimal amounut, String description) throws RemoteException {
        String agencyId = Utils.getAgencyId(donor.getAccountnum());
        AgencyServices services = ServiceRepo.getAgencyService(agencyId);
        if (services == null) {
            return Message.builder(false)
                    .message("Service unavailabale")
                    .build();
        }
        return services.getTransactionService().depositMoney(donor, amounut, description);
    }

    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Message cancelTransaction(Transaction transaction) throws RemoteException {
        String agencyId = Utils.getAgencyId(transaction.getAccountnum().getAccountnum());
        AgencyServices services = ServiceRepo.getAgencyService(agencyId);
        if (services == null) {
            return Message.builder(false)
                    .message("Service unavailabale")
                    .build();
        }
        return services.getTransactionService().cancelTransaction(transaction);
    }

}
