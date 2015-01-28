/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.holmes.watson.bank.client.view;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.holmes.watson.bank.core.AccountService;
import org.holmes.watson.bank.core.AgencyServices;
import org.holmes.watson.bank.core.HolmesWatson;
import org.holmes.watson.bank.core.Message;
import org.holmes.watson.bank.core.TransactionService;
import org.holmes.watson.bank.core.auth.AuthService;
import org.holmes.watson.bank.core.entity.Account;
import org.holmes.watson.bank.core.entity.Agency;
import org.holmes.watson.bank.core.entity.Client;

/**
 *
 * @author Olayinka
 */
public class Context {

    private static AgencyServices HQ_SERVICES;
    private static Agency AGENCY;
    private static AgencyServices AGENCY_SERVICES;
    private static Client CLIENT;

    private static final List<ContextChangeListener> listeners = new ArrayList<>(20);

    static void setHqServices(AgencyServices hqServices) {
        HQ_SERVICES = hqServices;
    }

    public static AgencyServices qetHqServices() {
        return HQ_SERVICES;
    }

    public static Agency getAgency() {
        return AGENCY;
    }

    static void setAgency(Agency agency) {
        AGENCY = agency;
    }

    public static AgencyServices getAgencyServices() {
        return AGENCY_SERVICES;
    }

    static void setAgencyServices(AgencyServices agencyServices) {
        AGENCY_SERVICES = agencyServices;
    }

    static void setClient(Client client) {
        CLIENT = client;
    }

    public static Client getClient() {
        return CLIENT;
    }

    public static void registerListener(ContextChangeListener listener) {
        listeners.add(listener);
    }

    public static void onContextChanged() {
        System.out.println("Context changed, reloading views");
        for (ContextChangeListener listener : listeners) {
            listener.onAuthConfirmed();
        }
    }

    static void reconnect() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    static boolean reload;
    final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    static void startThread() {
        scheduler.scheduleAtFixedRate(new PingThread(), 0, 10, TimeUnit.SECONDS);
    }

    public static class PingThread implements Runnable {

        @Override
        public void run() {
            try {

                Registry registry = LocateRegistry.getRegistry(HolmesWatson.HEADQUATERS_ADDRESS, HolmesWatson.HQ_PORT);
                AuthService authService = (AuthService) registry.lookup(AuthService.SERVICE_NAME);
                TransactionService transactionService = (TransactionService) registry.lookup(TransactionService.SERVICE_NAME);
                AccountService accountService = (AccountService) registry.lookup(AccountService.SERVICE_NAME);

                AgencyServices hqServices = new AgencyServices(accountService, transactionService, authService);
                synchronized (HQ_SERVICES) {
                    Context.setHqServices(hqServices);
                }
                if (reload) {
                    Message message = hqServices.getAuthService().authenticate(CLIENT.getClientid(), CLIENT.getPassword());
                    if (message.getStatus()) {
                        Client client = (Client) message.getAttachment()[0];
                        Agency agency = (Agency) message.getAttachment()[1];
                        client.setAccountList((ArrayList<Account>) message.getAttachment()[2]);
                        registry = LocateRegistry.getRegistry(agency.getAddress(), HolmesWatson.PORT);
                        authService = (AuthService) registry.lookup(AuthService.SERVICE_NAME);
                        transactionService = (TransactionService) registry.lookup(TransactionService.SERVICE_NAME);
                        accountService = (AccountService) registry.lookup(AccountService.SERVICE_NAME);
                        synchronized (CLIENT) {
                            setClient(client);
                            synchronized (AGENCY) {
                                setAgency(agency);
                            }
                            setAgencyServices(new AgencyServices(accountService, transactionService, authService));
                        }
                        reload = false;
                        onContextChanged();
                    } else {
                        System.out.println(message.getMessage());
                    }
                }
            } catch (RemoteException ex) {
                reload = true;
            } catch (NotBoundException ex) {
                reload = true;
                Logger.getLogger(Context.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
