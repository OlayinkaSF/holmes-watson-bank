/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.holmes.watson.bank.hq;

import java.rmi.RemoteException;
import javax.persistence.EntityManagerFactory;
import org.holmes.watson.bank.core.AgencyServices;
import org.holmes.watson.bank.core.Message;
import org.holmes.watson.bank.core.Utils;
import org.holmes.watson.bank.core.auth.AuthService;
import org.holmes.watson.bank.core.entity.Client;

/**
 *
 * @author Olayinka
 */
public class AuthServiceImpl implements AuthService {

    final static AuthService AUTH_SERVICE = new AuthServiceImpl();

    @Override
    public Message authenticate(String login, String password) throws RemoteException {
        String agencyId = Utils.getAgencyId(login);
        AgencyServices services = ServiceRepo.getAgencyService(agencyId);
        if (services == null) {
            return Message.builder(false)
                    .message("Service unavailabale")
                    .build();
        }
        Message message = services.getAuthService().authenticate(login, password);
        if (message.getStatus()) {
            return Message.builder(true)
                    .message(message.getMessage())
                    .attachment(message.getAttachment()[0], message.getAttachment()[1], AccountServiceImpl.getHQService().getAccounts((Client) message.getAttachment()[0]))
                    .build();
        }
        return message;
    }

    void setEmf(EntityManagerFactory managerFactory) {
        this.emf = managerFactory;
    }
    private EntityManagerFactory emf;

}
