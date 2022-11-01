/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.vendingmachine.dao;

import com.sg.vendingmachine.dto.Item;
import java.util.List;

/**
 *
 * @author Daryl del Rosario
 */
public interface VendingMachineDao {

    Item addItem(String itemId, Item item) throws VendingMachinePersistenceException ;
    
    Item getItem(String itemId) throws VendingMachinePersistenceException ;
    
    List<Item> getAllItems() throws VendingMachinePersistenceException ;
    
    Item removeItem(String itemId) throws VendingMachinePersistenceException ;
    
    Item updateItem(String itemId, Item item) throws VendingMachinePersistenceException ;
    
    List<Item> getZeroStockItems() throws VendingMachinePersistenceException;
    
}
