/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.holmes.watson.bank.core;

import java.math.BigDecimal;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import org.android.json.JSONObject;
import org.holmes.watson.bank.core.entity.Account;
import org.holmes.watson.bank.core.entity.Client;

/**
 *
 * @author Olayinka
 */
public interface AccountService extends Remote {

    public static String SERVICE_NAME = HolmesWatson.SERVICE_PREFIX + "." + "AccountService";

    public Message createClient(JSONObject newClient) throws RemoteException;

    public Message getClient(Client client) throws RemoteException;

    public Message createAccount(Client client, JSONObject account) throws RemoteException;

    public Message deleteAccount(Account account) throws RemoteException;

    public Message modifyAccount(Account account) throws RemoteException;

    public Message deleteClient(Client client) throws RemoteException;

    public List<Account> getAccounts(Client client) throws RemoteException;

}
