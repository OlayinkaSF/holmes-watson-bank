/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.holmes.watson.bank.core.auth;

import java.rmi.Remote;
import java.rmi.RemoteException;
import org.holmes.watson.bank.core.HolmesWatson;
import org.holmes.watson.bank.core.Message;

/**
 *
 * @author Olayinka
 */
public interface AuthService extends Remote {

    public static String SERVICE_NAME = HolmesWatson.SERVICE_PREFIX + "." + "AuthService";

    public Message authenticate(String login, String password) throws RemoteException;

}
