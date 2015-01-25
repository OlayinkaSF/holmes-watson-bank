/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.holmes.watson.bank.agency.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import org.android.json.JSONException;
import org.android.json.JSONObject;
import org.holmes.watson.bank.agency.entity.AccountJpaController;
import org.holmes.watson.bank.agency.entity.ClientJpaController;
import org.holmes.watson.bank.agency.entity.exceptions.NonexistentEntityException;
import org.holmes.watson.bank.agency.entity.exceptions.PreexistingEntityException;
import org.holmes.watson.bank.core.AccountService;
import org.holmes.watson.bank.core.Message;
import org.holmes.watson.bank.core.Utils;
import org.holmes.watson.bank.core.entity.Account;
import org.holmes.watson.bank.core.entity.Agency;
import org.holmes.watson.bank.core.entity.Client;

/**
 *
 * @author Olayinka
 */
public class AccountServiceImpl implements AccountService {

    final static AccountService ACCOUNT_SERVICE = new AccountServiceImpl();

    public static AccountService getLocalService() {
        return ACCOUNT_SERVICE;
    }
    AccountService hqService;
    Random random = new Random();

    EntityManagerFactory emf;
    private ClientJpaController clientController;
    private AccountJpaController accountController;

    public void setHqService(AccountService hqService) {
        this.hqService = hqService;
    }

    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
        clientController = new ClientJpaController(emf);
        accountController = new AccountJpaController(emf);
    }

    @Override
    public Message createClient(JSONObject newClient) throws RemoteException {
        try {
            long mainId = Utils.nextLong(random, 1L << 32);
            String clientId = Agency.getAgency().getAgencyid() + "-" + mainId;
            String clientFirstName = newClient.getString("client.first.name");
            String clientLastName = newClient.getString("client.last.name");
            String clientPassword = newClient.getString("client.password");
            String clientAddress = newClient.getString("client.address");
            Client client = new Client(clientId, clientFirstName, clientLastName, clientPassword, clientAddress);
            clientController.create(client);
            return Message.builder(true)
                    .attachment(client)
                    .build();
        } catch (JSONException ex) {
            Logger.getLogger(AccountServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PreexistingEntityException ex) {
            Logger.getLogger(AccountServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return Message.builder(false).message("Can't generate unique id.").toDo("Try again.").build();
        } catch (Exception ex) {
            Logger.getLogger(AccountServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Message.builder(false).message("Service unavailable").build();
    }

    @Override
    public Message deleteAccount(Account account) throws RemoteException {
        try {
            account.setStatus(Account.STATUS_CLOSED);
            accountController.edit(account);
            return Message.builder(true)
                    .message("Account deleted seccessfully.")
                    .attachment(account)
                    .build();
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(AccountServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return Message.builder(false)
                    .message("You have no right to this account.")
                    .build();
        } catch (Exception ex) {
            Logger.getLogger(AccountServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return Message.builder(false)
                    .message(ex.getMessage())
                    .build();
        }
    }

    @Override
    public Message createAccount(Client client, JSONObject accountObject) throws RemoteException {
        Client tempClient = clientController.findClient(client.getClientid());
        if (tempClient == null) {
            Message message = hqService.getClient(client);
            if (message.getStatus()) {
                tempClient = (Client) message.getAttachment()[0];
            }
        }
        if (tempClient != null) {
            try {
                client = tempClient;
                if (!Utils.isMyAgency(client.getClientid())) {
                    clientController.create(client);
                }
                Account account = new Account(accountObject.getString("account.name"), new BigDecimal(accountObject.getString("account.init.balance")), Account.STATUS_OPEN);
                account.setClientid(client);
                accountController.create(account);
                return Message.builder(true)
                        .message("Account created successfully")
                        .attachment(account)
                        .build();
            } catch (Exception ex) {
                Logger.getLogger(AccountServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                return Message.builder(false)
                        .message("Can't create account because " + ex.getMessage())
                        .build();
            }
        } else {
            return Message.builder(false)
                    .message("Client doesn't exist!")
                    .build();
        }
    }

    @Override
    public Message getClient(Client client) throws RemoteException {
        try {
            Client tempClient = clientController.findClient(client.getClientid());
            return Message.builder(true)
                    .message("Account deleted seccessfully.")
                    .attachment(tempClient)
                    .build();
        } catch (Exception ex) {
            Logger.getLogger(AccountServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return Message.builder(false)
                    .message(ex.getMessage())
                    .build();
        }
    }

    @Override
    public Message modifyAccount(Account account) throws RemoteException {
        try {
            accountController.edit(account);
            return Message.builder(true)
                    .message("Account deleted seccessfully.")
                    .attachment(account)
                    .build();
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(AccountServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return Message.builder(false)
                    .message("You have no right to this account.")
                    .build();
        } catch (Exception ex) {
            Logger.getLogger(AccountServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return Message.builder(false)
                    .message(ex.getMessage())
                    .build();
        }
    }

    @Override
    public Message deleteClient(Client client) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Account> getAccounts(Client client) throws RemoteException {
        return client.getAccountList();
    }

}
