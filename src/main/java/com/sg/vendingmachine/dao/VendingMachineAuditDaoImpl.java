/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.vendingmachine.dao;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 *
 * @author Daryl del Rosario
 */
public class VendingMachineAuditDaoImpl implements VendingMachineAuditDao {

//    public LocalDateTime rightNow = LocalDateTime.now();
//    public String dateStamp = rightNow.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));
//    public String timeStamp = rightNow.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
//    public String dateTimeForm = dateStamp + " @ " + timeStamp;
    
    public static final String AUDIT_FILE = "auditInventory.txt";
    public static final String OUTOFSTOCK_FILE = "outOfStock.txt";
    
    @Override
    public void writeAuditEntry(String msg) throws VendingMachinePersistenceException {
        PrintWriter out;
        
        try {
            out = new PrintWriter(new FileWriter(AUDIT_FILE, true));
        } catch (IOException e) {
            throw new VendingMachinePersistenceException("Could not persist audit information.", e);
        }
        LocalDateTime rightNow = LocalDateTime.now();
        String dateStamp = rightNow.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));
        String timeStamp = rightNow.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String dateTimeForm = dateStamp + " @ " + timeStamp;
        
        out.println(dateTimeForm + " " + msg);
        out.flush();
    }
    @Override
    public void writeAuditEntry(String procedure, String itemName, String itemId) throws VendingMachinePersistenceException {
        PrintWriter out;
        
        try {
            out = new PrintWriter(new FileWriter(AUDIT_FILE, true));
        } catch (IOException e) {
            throw new VendingMachinePersistenceException("Could not persist audit information.", e);
        }
        LocalDateTime rightNow = LocalDateTime.now();
        String dateStamp = rightNow.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));
        String timeStamp = rightNow.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String dateTimeForm = dateStamp + " @ " + timeStamp;
        
        out.println(dateTimeForm + " " + procedure + " ITEM ID: " + itemId + " - " + itemName);
        out.flush();
    }
    
    @Override
    public void writeOutOfStockEntry(String msg) throws VendingMachinePersistenceException {
        PrintWriter out;
        
        try {
            out = new PrintWriter(new FileWriter(OUTOFSTOCK_FILE, true));
        } catch (IOException e) {
            throw new VendingMachinePersistenceException("Could not persist out of stock information.", e);
        }
        LocalDateTime rightNow = LocalDateTime.now();
        String dateTimeForm = rightNow.format(DateTimeFormatter.ofPattern("YYYY-MM-dd @ HH:mm:ss"));
        
        out.println(dateTimeForm + " " + msg);
        out.flush();
    }
    
    @Override
    public void writeOutOfStockEntry(String procedure, String itemName, String itemId) throws VendingMachinePersistenceException {
        PrintWriter out;
        
        try {
            out = new PrintWriter(new FileWriter(OUTOFSTOCK_FILE, true));
        } catch(IOException e) {
            throw new VendingMachinePersistenceException("Could not persist out of stock information.", e);
        }
        LocalDateTime rightNow = LocalDateTime.now();
        String dateTimeForm = rightNow.format(DateTimeFormatter.ofPattern("YYYY-MM-dd @ HH:mm:ss"));
        
        out.println(dateTimeForm + " " + procedure + " ITEM ID: " + itemId + " - " + itemName);
        out.flush();
    }
    
    @Override
    public void writeOutOfStockDate() throws VendingMachinePersistenceException {
        PrintWriter out;
        
        try {
            out = new PrintWriter(new FileWriter(OUTOFSTOCK_FILE, false));
        } catch (IOException e) {
            throw new VendingMachinePersistenceException("Could not persist out of stock date.", e);
        }
        LocalDateTime rightNow = LocalDateTime.now();
        String dateTimeForm = rightNow.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));
        
        out.println("LAST UPDATED: " + dateTimeForm);
        out.flush();
    }
}
