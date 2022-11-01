/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.vendingmachine.service;

import com.sg.vendingmachine.dao.VendingMachinePersistenceException;
import com.sg.vendingmachine.dto.CoinChange;
import com.sg.vendingmachine.dto.Item;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Daryl del Rosario
 */
public interface VendingMachineServiceLayer {
    
    public void createItem(Item item) throws VendingMachinePersistenceException,
            VendingMachineDuplicateIdException,
            VendingMachineDataValidationException;
    public List<Item> getAllItems() throws VendingMachinePersistenceException;
    public Item getItem(String itemId) throws VendingMachinePersistenceException;
    public Item removeItem(String itemId) throws VendingMachinePersistenceException;
    public Item updateItem(String itemId, Item item) throws VendingMachinePersistenceException,
            VendingMachineDataValidationException;
    
    public List<Item> getAvailableItemsInStock() throws VendingMachinePersistenceException, 
            VendingMachineNoItemInStockException;
    public Item getChosenItem(String itemId) throws VendingMachinePersistenceException,
            VendingMachineNoItemInStockException;
    public void checkSufficientFunds(BigDecimal deposit, Item item) throws VendingMachineInsufficientFundsException;
    public CoinChange calculateChange(BigDecimal deposit, Item item);
    public Item updateItemSale(Item item)throws VendingMachinePersistenceException;
    
    public List<Item> getZeroStockItems() throws VendingMachinePersistenceException;
    

}
