/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.holmes.watson.bank.agency.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.holmes.watson.bank.core.Message;
import org.holmes.watson.bank.core.entity.Client;

/**
 *
 * @author Olayinka
 */
public interface BackUpService extends Remote {

    public static final String SERVICE_NAME = "back.up.service";

    public static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public Message saveOrUpdateClient(Client client) throws RemoteException;

}
