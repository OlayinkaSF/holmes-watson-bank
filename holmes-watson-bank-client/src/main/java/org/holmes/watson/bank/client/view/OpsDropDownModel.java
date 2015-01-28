/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.holmes.watson.bank.client.view;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import org.holmes.watson.bank.core.Operation;
import static org.holmes.watson.bank.core.Operation.CONSULT;
import static org.holmes.watson.bank.core.Operation.TRANSFER;

/**
 *
 * @author Olayinka
 */
public class OpsDropDownModel implements ComboBoxModel<Operation> {

    Operation[] operations = new Operation[]{CONSULT, TRANSFER};
    Operation selection = operations[0];

    @Override
    public void setSelectedItem(Object anItem) {
        selection = (Operation) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selection;
    }

    @Override
    public int getSize() {
        return operations.length;
    }

    @Override
    public Operation getElementAt(int index) {
        return operations[index];
    }

    @Override
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {
    }
}
