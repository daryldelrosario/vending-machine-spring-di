/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.vendingmachine.dao;

/**
 *
 * @author Daryl del Rosario
 */
public interface VendingMachineAuditDao {

    public void writeAuditEntry(String msg) throws VendingMachinePersistenceException;
    
    public void writeAuditEntry(String procedure, String itemName, String itemId) throws VendingMachinePersistenceException;
    
    public void writeOutOfStockEntry(String msg) throws VendingMachinePersistenceException;
    
    public void writeOutOfStockEntry(String procedure, String itemName, String itemId) throws VendingMachinePersistenceException;
    
    public void writeOutOfStockDate() throws VendingMachinePersistenceException;
}
