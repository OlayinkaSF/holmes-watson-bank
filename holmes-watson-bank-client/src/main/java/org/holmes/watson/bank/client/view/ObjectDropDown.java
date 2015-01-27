/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.holmes.watson.bank.client.view;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author Olayinka
 * @param <T>
 */
public class ObjectDropDown<T> implements ComboBoxModel<T> {

    T[] operations;
    T selection;

    public ObjectDropDown(T[] operations) {
        this.operations = operations;
        selection = operations[0];
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selection = (T) anItem;
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
    public T getElementAt(int index) {
        return operations[index];
    }

    @Override
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {
    }
}
