/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.vendingmachine.dao;

import com.sg.vendingmachine.dto.Item;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Daryl del Rosario
 */
public class VendingMachineDaoFileImplTest {
    
    VendingMachineDao testDao;
    
    public VendingMachineDaoFileImplTest() {
    }
    
    @BeforeEach
    public void setUp() throws Exception {
        String testFile = "testInventory.txt";
        new FileWriter(testFile);
        testDao = new VendingMachineDaoFileImpl(testFile);
    }

    @Test
    public void testAddGetItem() throws Exception {
        System.out.println("test: Add and Get Item in inventory Method");
        // ARRANGE
        String itemId = "1";
        Item item = new Item(itemId);
        item.setItemName("Sprite");
        item.setItemPrice(new BigDecimal("2.50"));
        item.setItemInStock(3);
        
        // ACT
        testDao.addItem(itemId, item);
        Item retrievedItem = testDao.getItem(itemId);
        
        // ASSERT
        assertEquals(item.getItemId(), retrievedItem.getItemId(), "Checking Item Id");
        assertEquals(item.getItemName(), retrievedItem.getItemName(), "Checking Item Name");
        assertEquals(item.getItemPrice(), retrievedItem.getItemPrice(), "Checking Item Price");
        assertEquals(item.getItemInStock(), retrievedItem.getItemInStock(), "Checking Item in Stock");
    }
    
    @Test
    public void testAddGetAllItems() throws Exception {
        System.out.println("test: Add and Get All Items in inventory Method");
        // ARRANGE
        Item firstItem = new Item("1");
        firstItem.setItemName("Sprite");
        firstItem.setItemPrice(new BigDecimal("2.50"));
        firstItem.setItemInStock(3);
        
        Item secondItem = new Item("2");
        secondItem.setItemName("Coke Zero");
        secondItem.setItemPrice(new BigDecimal("2.22"));
        secondItem.setItemInStock(1);
        
        // ACT
        testDao.addItem(firstItem.getItemId(), firstItem);
        testDao.addItem(secondItem.getItemId(), secondItem);
        
        List<Item> allItems = testDao.getAllItems();
        
        // ASSERT
        assertNotNull(allItems, "The list of items MUST NOT be NULL");
        assertEquals(2, allItems.size(), "List of items should have 2 items.");
        
        assertTrue(testDao.getAllItems().contains(firstItem), "The list of items should include Sprite");
        assertTrue(testDao.getAllItems().contains(secondItem), "The list of items should include Coke Zero");
    }
    
    @Test
    public void testRemoveItem() throws Exception {
        System.out.println("test: Remove Item from inventory Method");
        // ARRANGE
        Item firstItem = new Item("1");
        firstItem.setItemName("Sprite");
        firstItem.setItemPrice(new BigDecimal("2.50"));
        firstItem.setItemInStock(3);
        
        Item secondItem = new Item("2");
        secondItem.setItemName("Coke Zero");
        secondItem.setItemPrice(new BigDecimal("2.22"));
        secondItem.setItemInStock(1);
        
        // ACT and ASSERT - check correct item removed: First Item
        testDao.addItem(firstItem.getItemId(), firstItem);
        testDao.addItem(secondItem.getItemId(), secondItem);
        Item removedItem = testDao.removeItem(firstItem.getItemId());
        assertEquals(removedItem, firstItem, "The removed item should be Sprite");
        
        // ACT and ASSERT - check contents of list: First Item removed
        List<Item> allItems = testDao.getAllItems();
        assertNotNull(allItems, "All items list SHOULD NOT be NULL");
        assertEquals(1, allItems.size(), "All items should only have one (1) item");
        assertFalse(allItems.contains(firstItem), "All items SHOULD NOT include Sprite");
        assertTrue(allItems.contains(secondItem), "All items SHOULD STILL include Coke Zero");
        
        // ACT and ASSERT - check correct item removed: Second Item
        removedItem = testDao.removeItem(secondItem.getItemId());
        assertEquals(removedItem, secondItem, "The removed item should be Coke Zero");
        
        // ACT and ASSERT - check contents of list: Second Item removed - list now null
        allItems = testDao.getAllItems();
        assertTrue(allItems.isEmpty(), "The retrieved list of items SHOULD BE EMPTY");
        
        Item retrievedItem = testDao.getItem(firstItem.getItemId());
        assertNull(retrievedItem, "Sprite was removed, SHOULD BE NULL");
        
        retrievedItem = testDao.getItem(secondItem.getItemId());
        assertNull(retrievedItem, "Coke Zero was removed, SHOULD BE NULL");
    }
    
    @Test
    public void testUpdateItem() throws Exception {
        System.out.println("test: Update Item in inventory Method");
        // ARRANGE - create first item
        Item firstItem = new Item("1");
        firstItem.setItemName("Sprite");
        firstItem.setItemPrice(new BigDecimal("2.50"));
        firstItem.setItemInStock(3);
        
        Item updateItem = new Item("1");
        updateItem.setItemName("Water Bottle");
        updateItem.setItemPrice(new BigDecimal("3.33"));
        updateItem.setItemInStock(33);
        
        // ACT - add item to dao and retrieve
        testDao.addItem(firstItem.getItemId(), firstItem);
        Item getFirstItem = testDao.getItem(firstItem.getItemId());
        
        // ASSERT - confirm getFirstItem and firstItem are the same
        assertEquals(firstItem.getItemId(), getFirstItem.getItemId(), "Checking item id");
        assertEquals(firstItem.getItemName(), getFirstItem.getItemName(), "Checking item name");
        assertEquals(firstItem.getItemPrice(), getFirstItem.getItemPrice(), "Checking item name");
        assertEquals(firstItem.getItemInStock(), getFirstItem.getItemInStock(), "Checking item in stock");
        
        // ACT - update item, same itemId as firstItem and retrieve
        testDao.updateItem(updateItem.getItemId(), updateItem);
        Item getUpdateItem = testDao.getItem(updateItem.getItemId());
        
        // ASSERT - confirm list is still only size one
        List<Item> fullItemList = testDao.getAllItems();
        assertEquals(1, fullItemList.size(), "After update, there should still only be one item in list");
        
        // ASSERT - update item and first item have same ID, but different name / price / stock
        assertEquals(firstItem.getItemId(), updateItem.getItemId(), "itemID SHOULD BE EQUAL");
        assertNotEquals(firstItem.getItemName(), updateItem.getItemName(), "Item name has been update SHOULD NOT BE EQUAL");
        assertNotEquals(firstItem.getItemPrice(), updateItem.getItemPrice(), "Price has been updated SHOULD NOT BE EQUAL");
        assertNotEquals(firstItem.getItemInStock(), updateItem.getItemInStock(), "Item in stock has been updated SHOULD NOT BE EQUAL");
    }
    
    @Test
    public void testGetZeroStockItems() throws Exception {
        System.out.println("test: Get Zero Stock Items from inventory Method");
        // ARRANGE - create list with two items: one with itemInStock > 0, another with itemInStock = 0
        Item threeStockItem = new Item("1");
        threeStockItem.setItemName("Spoons");
        threeStockItem.setItemPrice(new BigDecimal("3.33"));
        threeStockItem.setItemInStock(3);
        
        Item zeroStockItem = new Item("2");
        zeroStockItem.setItemName("Knives");
        zeroStockItem.setItemPrice(new BigDecimal("9.99"));
        zeroStockItem.setItemInStock(0);
        
        testDao.addItem(threeStockItem.getItemId(), threeStockItem);
        testDao.addItem(zeroStockItem.getItemId(), zeroStockItem);
        
        // ACT and ASSERT - confirm that zeroStockList has one item
        List<Item> zeroStockList = testDao.getZeroStockItems();
        assertEquals(1, zeroStockList.size(), "Zero Stock List should only be one (1)");
        
        // ACT and ASSERT - confirm that zeroStockList has item ID 2 not item Id 1
        assertTrue(zeroStockList.contains(zeroStockItem), "This list should contain only Knives");
        assertFalse(zeroStockList.contains(threeStockItem), "This list should NOT CONTAIN Spoons");
    }
}
