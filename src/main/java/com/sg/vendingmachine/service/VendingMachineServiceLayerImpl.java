/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.vendingmachine.service;

import com.sg.vendingmachine.dao.VendingMachineAuditDao;
import com.sg.vendingmachine.dao.VendingMachineDao;
import com.sg.vendingmachine.dao.VendingMachinePersistenceException;
import com.sg.vendingmachine.dto.CoinChange;
import com.sg.vendingmachine.dto.Item;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Daryl del Rosario
 */
public class VendingMachineServiceLayerImpl implements VendingMachineServiceLayer {

    public VendingMachineDao dao;
    private VendingMachineAuditDao auditDao;
    
    public VendingMachineServiceLayerImpl(VendingMachineDao dao, VendingMachineAuditDao auditDao) {
        this.dao = dao;
        this.auditDao = auditDao;
        
    }
    @Override
    public void createItem(Item item) throws VendingMachinePersistenceException, 
            VendingMachineDuplicateIdException, 
            VendingMachineDataValidationException {
        
        String thisItemId = item.getItemId();
        
        if(dao.getItem(thisItemId) != null) {
            throw new VendingMachineDuplicateIdException
                    ("ERROR: Could not add item because ITEM ID: " + thisItemId + " already exists.\n"
                            + "TRY A DIFFERENT ITEM ID.");
        }
        
        validateItemData(item);
        dao.addItem(thisItemId, item);
        auditDao.writeAuditEntry("ADDED", item.getItemName(), item.getItemId());
    }

    @Override
    public List<Item> getAllItems() throws VendingMachinePersistenceException {
        return dao.getAllItems();
    }

    @Override
    public Item getItem(String itemId) throws VendingMachinePersistenceException {
        return dao.getItem(itemId);
    }

    @Override
    public Item removeItem(String itemId) throws VendingMachinePersistenceException {
        Item removedItem = dao.removeItem(itemId);
        auditDao.writeAuditEntry("REMOVED", removedItem.getItemName(), itemId);
        return removedItem;
    }

    @Override
    public Item updateItem(String itemId, Item item) throws VendingMachinePersistenceException,
            VendingMachineDataValidationException {
        Item checkItem = dao.getItem(itemId);
        
        validateItemData(item);

        Item updatedItem = dao.updateItem(itemId, item);
        auditDao.writeAuditEntry("UPDATED", updatedItem.getItemName(), itemId);
        return updatedItem;
    }
    
    @Override
    public List<Item> getAvailableItemsInStock() throws VendingMachinePersistenceException,
            VendingMachineNoItemInStockException {
        Map<String, Item> availableItemsList = new HashMap<>();
        List<Item> zeroStockItemList = dao.getZeroStockItems();
        
        auditDao.writeOutOfStockDate();
        if(zeroStockItemList.size() < 1) {
            auditDao.writeOutOfStockEntry(" all items are IN STOCK");
        }
        
        for(Item i : dao.getAllItems()) {
            if(i.getItemInStock() < 1) {
                auditDao.writeOutOfStockEntry("OUT OF STOCK", i.getItemName(), i.getItemId());
            } else {
                availableItemsList.put(i.getItemId(), i);
            }
        } return new ArrayList<Item>(availableItemsList.values());
    }
    
    @Override
    public Item getChosenItem(String itemId) throws VendingMachinePersistenceException,
            VendingMachineNoItemInStockException {
        validateChosenItemInStock(itemId);
        return dao.getItem(itemId);
    }
    
    @Override
    public void checkSufficientFunds(BigDecimal deposit, Item item) throws VendingMachineInsufficientFundsException {
        BigDecimal itemPrice = item.getItemPrice();
        
        if(deposit.compareTo(itemPrice) < 0) {
            throw new VendingMachineInsufficientFundsException("Error: Not enough funds to buy " + item.getItemName() +
                    " for $" + itemPrice +". You only gave $" + deposit);
        }
    }
    
    @Override
    public CoinChange calculateChange(BigDecimal deposit, Item item) {
        BigDecimal itemPrice = item.getItemPrice();
        BigDecimal change = deposit.subtract(itemPrice);
        
        CoinChange actualChange; 
        
        if(change.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal changeInPennies = change.multiply(new BigDecimal("100"));
            actualChange = new CoinChange(changeInPennies);
        } else {
            BigDecimal depositInPennies = deposit.multiply(new BigDecimal("100"));
            actualChange = new CoinChange(depositInPennies);
        }
        
        return actualChange;
    }
    
    @Override
    public Item updateItemSale(Item item) throws VendingMachinePersistenceException {
        String itemId = item.getItemId();
        String itemName = item.getItemName();
        BigDecimal itemPrice = item.getItemPrice();
        int itemInStock = item.getItemInStock();
        
        Item purchasedItem = new Item(itemId);
        int updatedItemInStock = itemInStock - 1;
        
        purchasedItem.setItemName(itemName);
        purchasedItem.setItemPrice(itemPrice);
        purchasedItem.setItemInStock(updatedItemInStock);
   
        dao.updateItem(itemId, purchasedItem);
        
        if(updatedItemInStock == 0) {
            auditDao.writeAuditEntry("PURCHASED and NOW OUT OF STOCK", itemName, itemId);
            return purchasedItem;
        } else {
            auditDao.writeAuditEntry("PURCHASED", itemName, itemId);
            return purchasedItem;
        }
    }
    
    @Override
    public List<Item> getZeroStockItems() throws VendingMachinePersistenceException {
        return dao.getZeroStockItems();
    }
    
    private void validateItemData(Item item) throws VendingMachineDataValidationException {
        if (item.getItemId() == null
                || item.getItemId().trim().length() == 0
                || item.getItemName() == null
                || item.getItemName().trim().length() == 0) {
            throw new VendingMachineDataValidationException
                ("ERROR: All Fields [Id, Name, Price, Stock] are required.");
        }
    }
    
    private void validateChosenItemInStock(String itemId) throws VendingMachinePersistenceException,
            VendingMachineNoItemInStockException {
        Item checkItem = getItem(itemId);
        
        if (checkItem == null) {
            throw new VendingMachineNoItemInStockException("ERROR: Nothing's There !!! Back to Vending Menu !!!");
        } else if (checkItem.getItemInStock() == 0) {
            throw new VendingMachineNoItemInStockException("ERROR: This item no longer exists");
        }
    }
    
}
