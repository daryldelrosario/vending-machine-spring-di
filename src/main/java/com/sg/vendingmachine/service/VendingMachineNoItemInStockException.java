/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.vendingmachine.service;

/**
 *
 * @author Daryl del Rosario
 */
public class VendingMachineNoItemInStockException extends Exception {

    public VendingMachineNoItemInStockException(String msg) {
        super(msg);
    }
    
    public VendingMachineNoItemInStockException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
