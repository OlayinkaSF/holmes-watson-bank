/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.holmes.watson.bank.client.view;

import java.awt.CardLayout;
import java.math.BigDecimal;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import org.android.json.JSONObject;
import org.holmes.watson.bank.core.AccountListener;
import org.holmes.watson.bank.core.AccountService;
import org.holmes.watson.bank.core.AgencyServices;
import org.holmes.watson.bank.core.HolmesWatson;
import org.holmes.watson.bank.core.Message;
import org.holmes.watson.bank.core.TransactionListener;
import org.holmes.watson.bank.core.TransactionService;
import org.holmes.watson.bank.core.auth.AuthService;
import org.holmes.watson.bank.core.auth.ClientAuthListener;
import org.holmes.watson.bank.core.entity.Account;
import org.holmes.watson.bank.core.entity.Agency;
import org.holmes.watson.bank.core.entity.Client;

/**
 *
 * @author Olayinka
 */
public class ClientView extends javax.swing.JFrame implements ClientAuthListener, TransactionListener, AccountListener {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);
    private static Client client;

    public static Client getClient() {
        return client;
    }
    private final AgencyServices hqServices;
    private static Agency agency;
    private AgencyServices agencyServices;

    ClientView(AgencyServices hqServices) {
        this.hqServices = hqServices;
        initComponents();
        cards.add(new AuthView(this), AuthView.TAG_NAME);
        cards.add(new HomeView(this, this), HomeView.TAG_NAME);
        cardLayout.show(cards, AuthView.TAG_NAME);
        add(cards);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(960, 540));
        setMinimumSize(new java.awt.Dimension(960, 540));
        setResizable(false);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public void authenticate(String login, String password) {
        try {
            Message message = hqServices.getAuthService().authenticate(login, password);
            if (message.getStatus()) {
                cardLayout.show(cards, HomeView.TAG_NAME);
                client = (Client) message.getAttachment()[0];
                agency = (Agency) message.getAttachment()[1];
                client.getAccountList().addAll((ArrayList<Account>) message.getAttachment()[2]);
            } else {
                System.out.println(message.getMessage());
                return;
            }
            Registry registry = LocateRegistry.getRegistry(agency.getAddress(), HolmesWatson.PORT);
            AuthService authService = (AuthService) registry.lookup(AuthService.SERVICE_NAME);
            TransactionService transactionService = (TransactionService) registry.lookup(TransactionService.SERVICE_NAME);
            AccountService accountService = (AccountService) registry.lookup(AccountService.SERVICE_NAME);

            agencyServices = new AgencyServices(accountService, transactionService, authService);

        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(ClientView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void createAccount(JSONObject newClient, BigDecimal init) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteAccount(Client newClient) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void modifyAccount(Client newClient) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void transferMoney(Client donor, Client recipient, BigDecimal amounut) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void withdrawMoney(Client donor, BigDecimal amounut) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void depositMoney(Client donor, BigDecimal amounut) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void demandLoan(Client client, BigDecimal amount) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void terminateLoan(Client client, int loanId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void payLoan(Client client, Integer loanId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
