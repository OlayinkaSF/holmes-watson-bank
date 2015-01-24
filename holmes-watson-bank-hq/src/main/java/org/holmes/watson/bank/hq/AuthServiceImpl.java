/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.holmes.watson.bank.hq;

import java.rmi.RemoteException;
import javax.persistence.EntityManagerFactory;
import org.holmes.watson.bank.core.Message;
import org.holmes.watson.bank.core.auth.AuthService;

/**
 *
 * @author Olayinka
 */
public class AuthServiceImpl implements AuthService {

    final static AuthService AUTH_SERVICE = new AuthServiceImpl();
    private AuthService hqService;

    public void setHqService(AuthService hqService) {
        this.hqService = hqService;
    }

    @Override
    public Message authenticate(String login, String password) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void setEmf(EntityManagerFactory managerFactory) {
        this.emf = managerFactory;
    }
    private EntityManagerFactory emf;

}
