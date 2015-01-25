/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.holmes.watson.bank.hq;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.persistence.EntityManagerFactory;
import org.android.json.JSONObject;
import org.holmes.watson.bank.agency.entity.ClientJpaController;
import org.holmes.watson.bank.core.AccountService;
import org.holmes.watson.bank.core.AgencyServices;
import org.holmes.watson.bank.core.Message;
import org.holmes.watson.bank.core.Utils;
import org.holmes.watson.bank.core.entity.Account;
import org.holmes.watson.bank.core.entity.Client;

/**
 *
 * @author Olayinka
 */
public class AccountServiceImpl implements AccountService {

    final static AccountService ACCOUNT_SERVICE = new AccountServiceImpl();
    Random random = new Random();

    public static AccountService getHQService() {
        return ACCOUNT_SERVICE;
    }

    EntityManagerFactory emf;
    private ClientJpaController clientController;

    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
        clientController = new ClientJpaController(emf);
    }

    @Override
    public Message createClient(JSONObject newClient) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Message getClient(Client client) throws RemoteException {
        String agencyId = Utils.getAgencyId(client.getClientid());
        AgencyServices services = ServiceRepo.getAgencyService(agencyId);
        if (services == null) {
            return Message.builder(false)
                    .message("Service unavailabale")
                    .build();
        }
        return services.getAccountService().getClient(client);
    }

    

    @Override
    public Message deleteAccount(Account account) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Message modifyAccount(Account account) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Message deleteClient(Client client) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Account> getAccounts(Client client) throws RemoteException {
        HashMap<String, AgencyServices> allServices = ServiceRepo.getServices();
        List<Account> accounts = new ArrayList<>();
        for (Map.Entry<String, AgencyServices> entry : allServices.entrySet()) {
            accounts.addAll(entry.getValue().getAccountService().getAccounts(client));
        }
        return accounts;
    }

    @Override
    public Message createAccount(Client client, JSONObject account) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
