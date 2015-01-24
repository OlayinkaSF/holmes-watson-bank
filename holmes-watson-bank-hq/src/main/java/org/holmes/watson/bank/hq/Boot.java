/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.holmes.watson.bank.hq;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.holmes.watson.bank.agency.view.AgencyView;
import org.holmes.watson.bank.core.AccountService;
import org.holmes.watson.bank.core.HolmesWatson;
import org.holmes.watson.bank.core.TransactionService;
import org.holmes.watson.bank.core.auth.AuthService;
import org.holmes.watson.bank.core.entity.Agency;
import static org.holmes.watson.bank.hq.AccountServiceImpl.ACCOUNT_SERVICE;
import static org.holmes.watson.bank.hq.AuthServiceImpl.AUTH_SERVICE;
import static org.holmes.watson.bank.hq.TransactionServiceImpl.TRANSACTION_SERVICE;

/**
 *
 * @author Olayinka
 */
public class Boot {

    private static boolean GUI = true;

    public static final String AGENCY_KEY = "agency.key";
    public static final String ADDRESS_KEY = "agency.address";
    public static final String NAME_KEY = "agency.name";

    private static final String DB_USER_NAME = "db.user";
    private static final String DB_PASSWORD = "db.password";

    public static void main(String... args) throws RemoteException, FileNotFoundException, IOException, NotBoundException {
        for (String arg : args) {
            switch (arg) {
                case "-nogui":
                    GUI = false;
                    break;
                case "-gui":
                    GUI = true;
                    break;
                default:
                    System.out.println("Usage with -gui xor gui");
                    return;
            }
        }
        String envPath = System.getenv("HOLMESWATSON");
        if (envPath == null) {
            System.err.println("Please set environment variable and create property file for bank agency.");
            return;
        }
        File propertyFile = new File(envPath + File.separator + HolmesWatson.PROPERTY_FILE_NAME);
        Properties properties;

        try (InputStream is = new FileInputStream(propertyFile)) {
            properties = new Properties();
            properties.load(is);
        }

        String agencyKey = properties.getProperty(AGENCY_KEY);
        String agencyAddress = properties.getProperty(ADDRESS_KEY);
        String agencyName = properties.getProperty(NAME_KEY);

        Agency agency = new Agency(agencyKey, agencyAddress);
        Agency.setAgency(agency);

        Registry registry = LocateRegistry.createRegistry(HolmesWatson.HQ_PORT);

        EntityManagerFactory managerFactory = Persistence.createEntityManagerFactory("HOLMESWATSONHQ");

        ((AuthServiceImpl) AUTH_SERVICE).setEmf(managerFactory);
        ((TransactionServiceImpl) TRANSACTION_SERVICE).setEmf(managerFactory);
        ((AccountServiceImpl) ACCOUNT_SERVICE).setEmf(managerFactory);

        registry.rebind(AuthService.SERVICE_NAME, UnicastRemoteObject.exportObject(AUTH_SERVICE, HolmesWatson.HQ_PORT));
        registry.rebind(AccountService.SERVICE_NAME, UnicastRemoteObject.exportObject(ACCOUNT_SERVICE, HolmesWatson.HQ_PORT));
        registry.rebind(TransactionService.SERVICE_NAME, UnicastRemoteObject.exportObject(TRANSACTION_SERVICE, HolmesWatson.HQ_PORT));
        if (GUI) {
            new AgencyView().setVisible(GUI);
        }
        //org.holmes.watson.bank.agency.service.Boot.main("-gui");
    }
}
