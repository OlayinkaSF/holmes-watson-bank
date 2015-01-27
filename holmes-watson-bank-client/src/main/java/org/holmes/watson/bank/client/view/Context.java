/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.holmes.watson.bank.client.view;

import java.util.ArrayList;
import java.util.List;
import org.holmes.watson.bank.core.AgencyServices;
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

    private static List<AuthConfirmedListener> listeners = new ArrayList<>(20);

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

    public static void addListener(AuthConfirmedListener listener) {
        listeners.add(listener);
    }

    public static void onAuthConfirmed() {
        for (AuthConfirmedListener listener : listeners) {
            listener.onAuthConfirmed();
        }
    }

}
