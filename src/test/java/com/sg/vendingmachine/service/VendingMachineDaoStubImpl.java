/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.vendingmachine.service;

import com.sg.vendingmachine.dao.VendingMachineDao;
import com.sg.vendingmachine.dao.VendingMachinePersistenceException;
import com.sg.vendingmachine.dto.Item;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Daryl del Rosario
 */
public class VendingMachineDaoStubImpl implements VendingMachineDao {

    public Item onlyItem;
    public Item zeroStock;
    
    public VendingMachineDaoStubImpl() {
        onlyItem = new Item("1");
        onlyItem.setItemName("Sprite");
        onlyItem.setItemPrice(new BigDecimal("2.50"));
        onlyItem.setItemInStock(3);
        
        zeroStock = new Item("3");
        zeroStock.setItemName("Water Bottle");
        zeroStock.setItemPrice(new BigDecimal("1.11"));
        zeroStock.setItemInStock(0);
    }
    
    public VendingMachineDaoStubImpl(Item testItem) {
        this.onlyItem = testItem;
    }
    
    @Override
    public Item addItem(String itemId, Item item) throws VendingMachinePersistenceException {
        if(itemId.equals(onlyItem.getItemId())) {
            return onlyItem;
        } else {
            return null;
        }
    }

    @Override
    public Item getItem(String itemId) throws VendingMachinePersistenceException {
        if(itemId.equals(onlyItem.getItemId())) {
            return onlyItem;
        } else if(itemId.equals("3")){
            return zeroStock;
        } else {
            return null;
        }
    }

    @Override
    public List<Item> getAllItems() throws VendingMachinePersistenceException {
        List<Item> itemList = new ArrayList<>();
        itemList.add(onlyItem);
        return itemList;
    }

    @Override
    public Item removeItem(String itemId) throws VendingMachinePersistenceException {
        if(itemId.equals(onlyItem.getItemId())) {
            return onlyItem;
        } else {
            return null;
        }
    }

    @Override
    public Item updateItem(String itemId, Item item) throws VendingMachinePersistenceException {
        if(itemId.equals(onlyItem.getItemId())) {
            return item;
        } else {
            return null;
        }
    }

    @Override
    public List<Item> getZeroStockItems() throws VendingMachinePersistenceException {
        List<Item> zeroStockItemList = new ArrayList<>();
        zeroStockItemList.add(zeroStock);
        return zeroStockItemList;
    }
}
