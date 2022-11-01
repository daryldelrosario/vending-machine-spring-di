/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.vendingmachine.dao;

import com.sg.vendingmachine.dto.Item;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 *
 * @author Daryl del Rosario
 */
public class VendingMachineDaoFileImpl implements VendingMachineDao {

    private final String INVENTORY_FILE;
    
    public VendingMachineDaoFileImpl() {
        INVENTORY_FILE = "inventory.txt";
    }

    public VendingMachineDaoFileImpl(String inventoryTextFile) {
        INVENTORY_FILE = inventoryTextFile;
    }
    
    public static final String DELIMITER = "::";
    
    private Map<String, Item> items = new HashMap<>();
    
    @Override
    public Item addItem(String itemId, Item item) throws VendingMachinePersistenceException {
        loadInventory();
        Item newItem = items.put(itemId, item);
        writeInventory();
        return newItem;
    }

    @Override
    public Item getItem(String itemId) throws VendingMachinePersistenceException {
        loadInventory();
        return items.get(itemId);
    }

    @Override
    public List<Item> getAllItems() throws VendingMachinePersistenceException {
        loadInventory();
        return new ArrayList<Item>(items.values());
    }

    @Override
    public Item removeItem(String itemId) throws VendingMachinePersistenceException {
        loadInventory();
        Item removedItem = items.remove(itemId);
        writeInventory();
        return removedItem;
    }

    @Override
    public Item updateItem(String itemId, Item item) throws VendingMachinePersistenceException {
        loadInventory();
        Item updatedItem = items.replace(itemId, item);
        writeInventory();
        return updatedItem;
    }
    
    
    @Override
    public List<Item> getZeroStockItems() throws VendingMachinePersistenceException {
        loadInventory();
        List<Item> itemList = getAllItems();
        List<Item> zeroStockList = itemList.stream()
                .filter((i) -> i.getItemInStock() < 1)
                .collect(Collectors.toList());
        return zeroStockList;
    }
    
    private void loadInventory() throws VendingMachinePersistenceException {
        Scanner sc;
        String thisLine;
        Item thisItem;
        
        try {
            sc = new Scanner(new BufferedReader(new FileReader(INVENTORY_FILE)));
        } catch (FileNotFoundException e) {
            throw new VendingMachinePersistenceException("Could not load inventory data into memory.", e);
        }
        
        while(sc.hasNextLine()) {
            thisLine = sc.nextLine();
            thisItem = unmarshallItem(thisLine);
            items.put(thisItem.getItemId(), thisItem);
        }
        sc.close();
    }
    
    private void writeInventory() throws VendingMachinePersistenceException {
        PrintWriter out;
        
        try {
            out = new PrintWriter(new FileWriter(INVENTORY_FILE));
        } catch (IOException e) {
            throw new VendingMachinePersistenceException("Could not save item data.", e);
        }
        
        String itemAsText;
        List<Item> itemList = this.getAllItems();
        for(Item currentItem : itemList) {
            itemAsText = marshallItem(currentItem);
            out.println(itemAsText);
            out.flush();
        }
        out.close();
    }
    
    private Item unmarshallItem(String itemAsText) {
        String[] itemTokens = itemAsText.split(DELIMITER);
        String itemId = itemTokens[0];
        BigDecimal itemPrice = new BigDecimal(itemTokens[2]);
        int itemInStock = Integer.parseInt(itemTokens[3]);
                
        Item itemFromFile = new Item(itemId);
        itemFromFile.setItemName(itemTokens[1]);
        itemFromFile.setItemPrice(itemPrice);
        itemFromFile.setItemInStock(itemInStock);
        
        return itemFromFile;
    }
    
    private String marshallItem(Item thisItem) {
        String itemPriceStr = thisItem.getItemPrice().toString();
        String itemInStockStr = String.valueOf(thisItem.getItemInStock());
        
        String itemAsText = thisItem.getItemId() + DELIMITER;
        itemAsText += thisItem.getItemName() + DELIMITER;
        itemAsText += itemPriceStr + DELIMITER;
        itemAsText += itemInStockStr;
        
        return itemAsText;
    }

}
