/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.vendingmachine.dto;

import java.math.BigDecimal;

/**
 *
 * @author Daryl del Rosario
 */
public enum Coins {
    PENNIES(new BigDecimal("1")), 
    NICKELS(new BigDecimal("5")), 
    DIMES(new BigDecimal("10")), 
    QUARTERS(new BigDecimal("25"));
    
    final BigDecimal coinValue;
    
    Coins (BigDecimal coinValue) {
        this.coinValue = coinValue;
    }
}
