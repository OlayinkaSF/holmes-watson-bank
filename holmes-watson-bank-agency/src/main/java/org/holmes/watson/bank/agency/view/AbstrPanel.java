/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.holmes.watson.bank.agency.view;

import javax.swing.JPanel;
import org.holmes.watson.bank.agency.service.AccountServiceImpl;
import org.holmes.watson.bank.core.AccountService;

/**
 *
 * @author Olayinka
 */
public abstract class AbstrPanel extends JPanel {

    protected TransactionListener listener;
    protected AccountService accountService = AccountServiceImpl.getLocalService();

    public AbstrPanel(TransactionListener listener) {
        this.listener = listener;
        initComponents();
    }

    public void optionPane(String message) {
        listener.optionPane(message);
    }

    public abstract void initComponents();
}
