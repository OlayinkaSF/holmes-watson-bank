/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.holmes.watson.bank.agency.service;

import java.rmi.RemoteException;
import javax.persistence.EntityManagerFactory;
import org.holmes.watson.bank.agency.entity.ClientJpaController;
import static org.holmes.watson.bank.agency.service.TransactionServiceImpl.TRANSACTION_SERVICE;
import org.holmes.watson.bank.core.Message;
import org.holmes.watson.bank.core.TransactionService;
import org.holmes.watson.bank.core.auth.AuthService;
import org.holmes.watson.bank.core.entity.Agency;
import org.holmes.watson.bank.core.entity.Client;

/**
 *
 * @author Olayinka
 */
public class AuthServiceImpl implements AuthService {

    final static AuthService AUTH_SERVICE = new AuthServiceImpl();
    private AuthService hqService;

    ClientJpaController clientController;

    public void setHqService(AuthService hqService) {
        this.hqService = hqService;
    }

    @Override
    public Message authenticate(String login, String password) throws RemoteException {
        Client client = new Client(login);
        client = clientController.findClient(client.getClientid());
        return Message.builder(client != null)
                .attachment(client, Agency.getAgency())
                .build();
    }

    void setEmf(EntityManagerFactory managerFactory) {
        this.emf = managerFactory;
    }
    private EntityManagerFactory emf;

    public static AuthService getLocalService() {
        return AUTH_SERVICE;
    }
}
