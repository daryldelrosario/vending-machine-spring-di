/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.vendingmachine.view;

import java.math.BigDecimal;

/**
 *
 * @author Daryl del Rosario
 */
public interface UserIO {

    void print (String msg);
    BigDecimal readBigDecimal(String msg);
    BigDecimal readBigDecimal(String msg, BigDecimal min, BigDecimal max);
    double readDouble(String msg);
    double readDouble(String msg, double min, double max);
    float readFloat(String msg);
    float readFloat(String msg, float min, float max);
    int readInt(String msg);
    int readInt(String msg, int min, int max);
    long readLong(String msg);
    long readLong(String msg, long min, long max);
    String readString(String msg);
    
}
