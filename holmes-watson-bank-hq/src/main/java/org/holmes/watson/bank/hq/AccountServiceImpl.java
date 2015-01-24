/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.holmes.watson.bank.hq;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Random;
import javax.persistence.EntityManagerFactory;
import org.android.json.JSONObject;
import org.holmes.watson.bank.agency.entity.ClientJpaController;
import org.holmes.watson.bank.core.AccountService;
import org.holmes.watson.bank.core.Message;
import org.holmes.watson.bank.core.entity.Account;
import org.holmes.watson.bank.core.entity.Client;

/**
 *
 * @author Olayinka
 */
public class AccountServiceImpl implements AccountService {

    final static AccountService ACCOUNT_SERVICE = new AccountServiceImpl();
    Random random = new Random();

    EntityManagerFactory emf;
    private ClientJpaController clientController;

    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
        clientController = new ClientJpaController(emf);
    }

    @Override
    public Message modifyAccount(Client newClient) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Message createClient(JSONObject newClient) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Message getClient(Client client) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Message createAccount(Client client, JSONObject account, BigDecimal init) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Message deleteAccount(Account account) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
