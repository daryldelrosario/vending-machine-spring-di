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
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Daryl del Rosario
 */
public class VendingMachineServiceLayerImplTest {
    
    private VendingMachineServiceLayer testService;
    
    public VendingMachineServiceLayerImplTest() {
//        VendingMachineDao dao = new VendingMachineDaoStubImpl();
//        VendingMachineAuditDao auditDao = new VendingMachineAuditDaoStubImpl();
//        
//        testService = new VendingMachineServiceLayerImpl(dao, auditDao);
//        CODE ABOVE for MILESTONE 3 ASSESSMENT

        ApplicationContext ctx = new ClassPathXmlApplicationContext("appTestContext.xml");
        testService = ctx.getBean("serviceLayer", VendingMachineServiceLayer.class);
    }
    
    @Test
    public void testCreateValidItem() {
        System.out.println("test: Create Valid Item to add Method");
        // ARRANGE
        Item item = new Item("2");
        item.setItemName("Coke Zero");
        item.setItemPrice(new BigDecimal("2.22"));
        item.setItemInStock(1);
        
        // ACT
        try {
            testService.createItem(item);
        } catch (VendingMachineDuplicateIdException
                | VendingMachineDataValidationException
                | VendingMachinePersistenceException e) {
            // ASSERT
            fail("Student was valid. No exception should have been thrown.");
        }
    }
    
    @Test
    public void testAddDuplicateItemId() {
        System.out.println("test: Throw Duplicate Item Id Exception");
        // ARRANGE
        Item item = new Item("1");
        item.setItemName("Sprite");
        item.setItemPrice(new BigDecimal("2.50"));
        item.setItemInStock(3);
        
        // ACT
        try {
            testService.createItem(item);
            fail("Expected DupeId Exception was not thrown.");
        } catch (VendingMachineDataValidationException
                |VendingMachinePersistenceException e) {
            // ASSERT
            fail("Incorrect exception was thrown.");
        } catch(VendingMachineDuplicateIdException e) {
            return;
        }
    }
    
    @Test
    public void testCreateItemInvalidData() throws Exception {
        System.out.println("test: Throw Invalid Data Exception");
        // ARRANGE
        Item item = new Item("2");
        item.setItemName("");
        item.setItemPrice(new BigDecimal("2.22"));
        item.setItemInStock(0);
        
        // ACT
        try {
            testService.createItem(item);
            fail("Expected ValidationException was not thrown.");
        } catch(VendingMachineDuplicateIdException
                | VendingMachinePersistenceException e) {
            // ASSERT
            fail("Incorrect exception was thrown.");
        } catch (VendingMachineDataValidationException e) {
            return;
        }
    }
    
    @Test
    public void testGetAllItems() throws Exception {
        System.out.println("test: Get All Items from service layer Method");
        // ARRANGE
        Item testItem = new Item("1");
        testItem.setItemName("Sprite");
        testItem.setItemPrice(new BigDecimal("2.50"));
        testItem.setItemInStock(3);
        
        // ACT and ASSERT
        assertEquals(1, testService.getAllItems().size(), "Should only have one (1) item.");
        assertTrue(testService.getAllItems().contains(testItem), "The one item should be Sprite");
    }
    
    @Test
    public void testGetItem() throws Exception {
        System.out.println("test: Get Item from service layer Method");
        // ARRANGE
        Item testItem = new Item("1");
        testItem.setItemName("Sprite");
        testItem.setItemPrice(new BigDecimal("2.50"));
        testItem.setItemInStock(3);
        
        // ACT and ASSERT
        Item shouldBeSprite = testService.getItem("1");
        assertNotNull(shouldBeSprite, "Getting item Id 1 SHOULD NOT be NULL.");
        assertEquals(testItem, shouldBeSprite, "Item stored under item Id 1 SHOULD BE Sprite");
        
        Item shouldBeNull = testService.getItem("42");
        assertNull(shouldBeNull, "Getting item Id 42 SHOULD BE NULL.");
    }
    
    @Test
    public void testRemoveItem() throws Exception {
        System.out.println("test: Remove Item from service layer Method");
        // ARRANGE
        Item testItem = new Item("1");
        testItem.setItemName("Sprite");
        testItem.setItemPrice(new BigDecimal("2.50"));
        testItem.setItemInStock(3);
        
        // ACT and ASSERT
        Item shouldBeSprite = testService.removeItem("1");
        assertNotNull(shouldBeSprite, "Removing item Id 1 SHOULD NOT be NULL");
        assertEquals(testItem, shouldBeSprite, "Item removed from item Id 1 SHOULD BE Sprite.");
        
        try {
            Item shouldBeNull = testService.removeItem("42");
            fail("Expected NullException to be thrown");
        } catch (Exception e) {
            return;
        }
        
        try {
            Item shouldBeNull = testService.removeItem("1");
            fail("Expected NullException to be thrown");
        } catch (Exception e) {
            return;
        }
    }
    
    @Test
    public void testUpdateItem() throws Exception {
        System.out.println("test: Update Item from service layer Method");
        // ARRANGE - change name / price / instock for itemId 1
        Item updatedItem = new Item("1");
        updatedItem.setItemName("Tea");
        updatedItem.setItemPrice(new BigDecimal("1.11"));
        updatedItem.setItemInStock(1);
        
        // ACT and ASSERT - retrieve original item
        Item shouldBeSprite = testService.getItem("1");
        assertEquals("Sprite", shouldBeSprite.getItemName(), "From stub, item should be Sprite");
        
        // ACT and ASSERT - updateItem and check listSize, should be one
        Item shouldBeTea = testService.updateItem(updatedItem.getItemId(), updatedItem);
        List<Item> itemList = testService.getAllItems();
        assertEquals(1, itemList.size(), "There should only be one (1) in list)");
        
        // ASSERT - confirm itemId 1 is itemName Coffee not Sprite
        assertNotEquals(shouldBeSprite, shouldBeTea, "After update, item is now TEA");
        assertEquals("Tea", shouldBeTea.getItemName(), "The item name IS TEA");
        
        // ASSERT - confirm all item properties updated, price, in stock
        assertEquals(updatedItem.getItemPrice(), shouldBeTea.getItemPrice(), "Checking updated price change");
        assertEquals(updatedItem.getItemInStock(), shouldBeTea.getItemInStock(), "Checking updated item in stock");
    }
    
    @Test
    public void testGetAvailableItemsInStock() throws Exception {
        System.out.println("test: Get Availble Items In Stock from service layer Method");
        // ARRANGE - set up available items in stock to match onlyItem in Dao
        Item testItem = new Item ("1");
        testItem.setItemName("Sprite");
        testItem.setItemPrice(new BigDecimal("2.50"));
        testItem.setItemInStock(3);
        
        List<Item> availableInStock = new ArrayList();
        availableInStock.add(testItem);
        
        // ACT and ASSERT - confirm available item in stock
        List<Item> testResult = testService.getAvailableItemsInStock();
        assertEquals(availableInStock, testResult, "List should be the same item");
    }
    
    @Test
    public void testGetChosenItem() throws Exception {
        System.out.println("test: Get Chosen Item from service layer Method");
        // ARRANGE - set up chosenItem to match item in stubDao
        Item chosenItem = new Item ("1");
        chosenItem.setItemName("Sprite");
        chosenItem.setItemPrice(new BigDecimal("2.50"));
        chosenItem.setItemInStock(3);
        
        // ACT and ASSERT - chosenItem same as item in stubDao
        Item testItem = testService.getChosenItem(chosenItem.getItemId());
        assertEquals(testItem, chosenItem, "Chosen item retrieved should be the same");
        
    }
    
    @Test
    public void testInsufficientFundException() throws Exception {
        System.out.println("test: Throw Insufficient Fund Exception");
        // ARRANGE - setup Insufficient Funds
        BigDecimal insufficientFund = new BigDecimal("2");
        Item testItem = testService.getChosenItem("1");

        // ACT and ASSERT - throws exception when checking for Insufficient Funds
        try {
            testService.checkSufficientFunds(insufficientFund, testItem);
            fail("Should throw insufficentFund Exception");
        } catch (VendingMachineInsufficientFundsException e) {
            return;
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }
    }
    
    @Test
    public void testSufficientFunds() throws Exception {
        System.out.println("test: Pass Sufficient Funds Check");
        // ARRANGE - setup sufficient funds for item in Dao
        BigDecimal sufficientFund = new BigDecimal("3");
        Item testItem = testService.getChosenItem("1");
        
        // ACT and ASSERT - funds are valid should run smoothly
        try {
            testService.checkSufficientFunds(sufficientFund, testItem);
        } catch (VendingMachineInsufficientFundsException e) {
            fail("Sufficient funds were deposited");
        } catch (Exception e) {
            fail("No errors should exist");
        }
    }
    
    @Test
    public void testCalculateChange() throws Exception {
        System.out.println("test: Calculate Change Method");
        // ARRANGE - item is $2.50, let's return 1 Quarter, 1 Dime, 1 Nickel, 1 Penny
        BigDecimal depositAmount = new BigDecimal("2.91");
        Item testItem = testService.getChosenItem("1");
        
        // ACT and ASSERT - make change and confirm 1 of each QUARTER, DIME, NICKE, PENNIES
        CoinChange testChange = testService.calculateChange(depositAmount, testItem);
        assertEquals(1, testChange.getQuarters(), "Check number of quarters");
        assertEquals(1, testChange.getDimes(), "Check number of dimes");
        assertEquals(1, testChange.getNickels(), "Check number of nickels");
        assertEquals(1, testChange.getPennies(), "Check number of pennies");
    }
    
    @Test
    public void testNoItemInStockException() throws Exception {
        System.out.println("test: Throw No Item In Stock Exception");
        // ARRANGE - create new item with 0 stock
        Item testItem = new Item("3");
        testItem.setItemName("Water Bottle");
        testItem.setItemPrice(new BigDecimal ("1.11"));
        testItem.setItemInStock(0);
        
        // ACT and ASSERT - confirm equality for stub Dao Item Id 3
        Item retrievedZeroStock = testService.getItem("3");
        assertEquals(testItem, retrievedZeroStock, "confirm equlaity for test item with zero stock");
        
        // ACT and ASSERT - confirm call for zero stock throws NoItemInStock exception
        try {
            Item zeroStock = testService.getChosenItem(testItem.getItemId());
            fail("No Item In Stock Exception should have been thrown");
        } catch(VendingMachineNoItemInStockException e) {
            return;
        } catch(Exception e) {
            fail("Wrong exception thrown");
        }
    }
    
    @Test
    public void testUpdateItemFromSale() throws Exception {
        System.out.println("test: Update Item In Stock from service layer Method");
        // ARRANGE - set up item with 3 items in stock
        Item threeStockItem = new Item("1");
        threeStockItem.setItemName("Sprite");
        threeStockItem.setItemPrice(new BigDecimal("2.50"));
        threeStockItem.setItemInStock(3);
        
        // ACT and ASSERT - confirm equality of testItem with item in stub Dao
        Item retrievedItem = testService.getItem("1");
        assertEquals(threeStockItem, retrievedItem, "Confirm threeStockItem is the same item as stub Dao");
        assertEquals(3, threeStockItem.getItemInStock(), "Confirm threeStockItem has 3 items in stock");
        
        // ACT and ASSERT - updateItemFromSale and confirm that item in stock dropped by one (1)
        Item twoStockItem = testService.updateItemSale(threeStockItem);
        assertEquals(2, twoStockItem.getItemInStock(), "Item In Stock should have dropped from 3 to 2");
    }
    
    @Test
    public void testGetZeroStockItems() throws Exception {
        System.out.println("test: Get Zero Items In Stock from service layer Method");
        // ARRANGE - set up zeroStock item
        Item testItem = new Item("3");
        testItem.setItemName("Water Bottle");
        testItem.setItemPrice(new BigDecimal("1.11"));
        testItem.setItemInStock(0);
        
        // ACT and ASSERT - confirm method pulls the one item with zero stock
        assertEquals(1, testService.getZeroStockItems().size(), "Pull should only be one item");
        assertTrue(testService.getZeroStockItems().contains(testItem), "Item in list should be Water Bottle");
        
        
        
    }
}
