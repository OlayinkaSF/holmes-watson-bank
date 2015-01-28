/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.holmes.watson.bank.client.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.Properties;
import org.holmes.watson.bank.core.AccountService;
import org.holmes.watson.bank.core.AgencyServices;
import org.holmes.watson.bank.core.HolmesWatson;
import org.holmes.watson.bank.core.TransactionService;
import org.holmes.watson.bank.core.auth.AuthService;

/**
 *
 * @author Olayinka
 */
public class Boot {

    public static final String AGENCY_KEY = "agency.key";
    public static final String ADDRESS_KEY = "agency.address";
    public static final String NAME_KEY = "agency.name";

    private static final String DB_USER_NAME = "db.user";
    private static final String DB_PASSWORD = "db.password";

    public static void main(String... args) throws RemoteException, FileNotFoundException, IOException, NotBoundException {
        File propertyFile = new File(HolmesWatson.PROPERTY_FILE_NAME);
        Properties properties;

        try (InputStream is = new FileInputStream(propertyFile)) {
            properties = new Properties();
            properties.load(is);
        }

        String hqHost = properties.getProperty("hq.host", HolmesWatson.HEADQUATERS_ADDRESS);

        if (hqHost == null) {
            System.err.println("Please set properties in root directory");
            return;
        }

        System.out.println(Arrays.toString(args));
        Registry registry = LocateRegistry.getRegistry(hqHost, HolmesWatson.HQ_PORT);
        AuthService authService = (AuthService) registry.lookup(AuthService.SERVICE_NAME);
        TransactionService transactionService = (TransactionService) registry.lookup(TransactionService.SERVICE_NAME);
        AccountService accountService = (AccountService) registry.lookup(AccountService.SERVICE_NAME);

        AgencyServices hqServices = new AgencyServices(accountService, transactionService, authService);
        Context.setHqServices(hqServices);
        new ClientView().setVisible(true);
        Context.startThread();
    }
}
