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
public class CoinChange {

    private int pennies;
    private int nickels;
    private int dimes;
    private int quarters;

    public CoinChange(BigDecimal inPennies) {
        this.quarters = inPennies.divide(Coins.QUARTERS.coinValue).intValue();
        inPennies = inPennies.remainder(Coins.QUARTERS.coinValue);
        this.dimes = inPennies.divide(Coins.DIMES.coinValue).intValue();
        inPennies = inPennies.remainder(Coins.DIMES.coinValue);
        this.nickels = inPennies.divide(Coins.NICKELS.coinValue).intValue();
        this.pennies = inPennies.remainder(Coins.NICKELS.coinValue).intValue();
        
    }

    public int getPennies() {
        return pennies;
    }

    public int getNickels() {
        return nickels;
    }

    public int getDimes() {
        return dimes;
    }

    public int getQuarters() {
        return quarters;
    }
}
