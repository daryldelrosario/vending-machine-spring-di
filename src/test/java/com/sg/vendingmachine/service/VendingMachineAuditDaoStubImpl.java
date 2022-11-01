/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.vendingmachine.service;

import com.sg.vendingmachine.dao.VendingMachineAuditDao;
import com.sg.vendingmachine.dao.VendingMachinePersistenceException;

/**
 *
 * @author Daryl del Rosario
 */
public class VendingMachineAuditDaoStubImpl implements VendingMachineAuditDao {

    @Override
    public void writeAuditEntry(String msg) throws VendingMachinePersistenceException {
        // do nothing
    }

    @Override
    public void writeAuditEntry(String procedure, String itemName, String itemId) throws VendingMachinePersistenceException {
        // do nothing
    }

    @Override
    public void writeOutOfStockEntry(String msg) throws VendingMachinePersistenceException {
        // do nothing
    }

    @Override
    public void writeOutOfStockEntry(String procedure, String itemName, String itemId) throws VendingMachinePersistenceException {
        // do nothing
    }

    @Override
    public void writeOutOfStockDate() throws VendingMachinePersistenceException {
        // do nothing
    }

}
