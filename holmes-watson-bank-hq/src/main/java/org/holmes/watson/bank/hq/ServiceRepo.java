/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.holmes.watson.bank.hq;

import java.math.BigInteger;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import org.holmes.watson.bank.agency.entity.AgencyJpaController;
import org.holmes.watson.bank.core.AccountService;
import org.holmes.watson.bank.core.AgencyServices;
import org.holmes.watson.bank.core.HolmesWatson;
import org.holmes.watson.bank.core.TransactionService;
import org.holmes.watson.bank.core.auth.AuthService;
import org.holmes.watson.bank.core.entity.Agency;

/**
 *
 * @author Olayinka
 */
public class ServiceRepo {

    private static final HashMap<String, AgencyServices> servicesMap = new HashMap<>();
    private static AgencyJpaController agencyController;
    public static final AuthService AUTH_SERVICE = new AuthServiceImpl();
    public static final AccountService ACCOUNT_SERVICE = new AccountServiceImpl();
    public static final TransactionService TRANSACTION_SERVICE = new TransactionServiceImpl();

    static AgencyServices getAgencyService(String agencyId) {
        return servicesMap.get(agencyId);
    }

    final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void startPing(EntityManagerFactory managerFactory) {
        agencyController = new AgencyJpaController(managerFactory);
        scheduler.scheduleAtFixedRate(new PingThread(), 0, 1, TimeUnit.MINUTES);
    }

    static HashMap<String, AgencyServices> getServices() {
        return servicesMap;
    }

    private static class PingThread implements Runnable {

        BigInteger round = BigInteger.ONE;

        public PingThread() {
        }

        @Override
        public void run() {
            List<Agency> agencies = agencyController.findAgencyEntities();
            for (Agency agency : agencies) {
                try {
                    Registry registry = LocateRegistry.getRegistry(agency.getAddress(), HolmesWatson.HQ_PORT);
                    AuthService authService = (AuthService) registry.lookup(AuthService.SERVICE_NAME);
                    TransactionService transactionService = (TransactionService) registry.lookup(TransactionService.SERVICE_NAME);
                    AccountService accountService = (AccountService) registry.lookup(AccountService.SERVICE_NAME);
                    AgencyServices agencyServices = new AgencyServices(accountService, transactionService, authService);
                    post(agency.getAgencyid(), agencyServices);
                } catch (RemoteException | NotBoundException ex) {
                    Logger.getLogger(ServiceRepo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            round = round.add(BigInteger.ONE);
            System.out.println("Ping round " + round.toString());
        }

        private synchronized void post(String agencyid, AgencyServices agencyServices) {
            servicesMap.put(agencyid, agencyServices);
        }
    }

}
