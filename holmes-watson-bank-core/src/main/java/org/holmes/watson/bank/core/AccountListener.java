/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.holmes.watson.bank.core;

import java.math.BigDecimal;
import org.android.json.JSONObject;
import org.holmes.watson.bank.core.entity.Client;

/**
 *
 * @author Olayinka
 */
public interface AccountListener {

    public void createAccount(JSONObject newClient, BigDecimal init);

    public void deleteAccount(Client newClient);

    public void modifyAccount(Client newClient);

}
