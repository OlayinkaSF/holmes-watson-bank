/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.holmes.watson.bank.agency.service;

import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.holmes.watson.bank.agency.entity.ClientJpaController;
import org.holmes.watson.bank.core.entity.Client;

/**
 *
 * @author Olayinka
 */
public class BackUpThread implements Runnable {

    BackUpService service;
    ClientJpaController cjc;

    public BackUpThread(BackUpService service, ClientJpaController cjc) {
        this.service = service;
        this.cjc = cjc;
    }

    @Override
    public void run() {
        cjc.getEntityManager().getEntityManagerFactory().getCache().evictAll();
        List<Client> clients = cjc.findClientEntities();
        for (Client client : clients) {
            try {
                service.saveOrUpdateClient(client);
            } catch (RemoteException ex) {
                Logger.getLogger(BackUpThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
