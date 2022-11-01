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
public class VendingMachinePersistenceException extends Exception {
    
    public VendingMachinePersistenceException(String msg) {
        super(msg);
    }
    
    public VendingMachinePersistenceException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
