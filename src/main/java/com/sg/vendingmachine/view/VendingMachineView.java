/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.vendingmachine.view;

import com.sg.vendingmachine.dto.Item;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 *
 * @author Daryl del Rosario
 */
public class VendingMachineView {

    private UserIO io;
    
    public static final String INVENTORY_COL_FORM = "%-9s%-18s%-9s%18s\n";
    public static final String VM_COL_FORM = "%-9s%-18s%9s\n";
    
    public VendingMachineView(UserIO io) {
        this.io = io;
    }
    
    public void displayMsg(String msg) {
        io.print(msg);
    }
    
    public void waitForUser(String msg) {
        io.readString(msg);
    }
    
    public int displayInventoryControlAccess() {
        io.print("========================");
        io.print("INVENTORY CONTROL ACCESS");
        io.print("========================");
        io.print("1. Add Item");
        io.print("2. Update Item");
        io.print("3. View Single Item Stats");
        io.print("4. List All Item Stats");
        io.print("5. Display Zero Stock Items");
        io.print("6. Remove Item");
        io.print("7. Return to Vending Machine");
        io.print("8. Exit");


        return io.readInt("Please select an option from above.", 1, 8);
    }
    
    public void displayInventoryHeader() {
        System.out.printf(INVENTORY_COL_FORM, "Id", "Item Name", "Price", "Stock Count");
        io.print("======================================================");
    }
    
    public void displayInventoryItemColumn(String id, String name, String price, Integer count) {
        System.out.printf(INVENTORY_COL_FORM, id, name, price, count);
    }
    
    public void displayBannerAddNewItem() {
        io.print("===========================");
        io.print("ADD NEW ITEM INTO INVENTORY");
        io.print("===========================");
    }
    
    public Item getNewItemInfo(String itemId) {

        String itemName = io.readString("Please enter Item Name");
        
        Item currentItem = new Item(itemId);
        BigDecimal itemPrice = io.readBigDecimal("Please enter Item Price");
        int itemInStock = io.readInt("Please enter Number of Items in Stock");
        
        currentItem.setItemName(itemName);
        currentItem.setItemPrice(itemPrice);
        currentItem.setItemInStock(itemInStock);
        
        return currentItem;
    }
    
    public void displayBannerAddNewItemSuccess() {
        io.print("====================================");
        io.print("NEW ITEM SUCCESSFULLY ADDED TO STOCK");
        io.print("====================================");
        io.readString("Please hit ENTER to continue.");
    }
    public void displayBannerAllItems() {
        io.print("===================================");
        io.print("DISPLAYING ALL ITEMS FROM INVENTORY");
        io.print("===================================");
    }
    public void displayAllItemList(List<Item> itemList) {
        displayInventoryHeader();
        
        for(Item currentItem : itemList) {
            String itemId = currentItem.getItemId();
            String itemName = currentItem.getItemName();
            String itemPrice = "$ " + currentItem.getItemPrice().toString();
            int itemInStock = currentItem.getItemInStock();
            
            displayInventoryItemColumn(itemId, itemName, itemPrice, itemInStock);
        }
    }
    
    public void displayBannerZeroStockItems() {
        io.print("===============================");
        io.print("DISPLAYING ALL ZERO STOCK ITEMS");
        io.print("===============================");
    }
    
    public void displayBannerSingleItem() {
        io.print("=====================================");
        io.print("DISPLAYING SINGLE ITEM FROM INVENTORY");
        io.print("=====================================");
    }
    
    public String getItemId(String forPurpose) {
        return io.readString("Please enter the Item ID to " + forPurpose);
    }
    
    public void displaySingleItem(Item item) { 
        if(item != null) {
            String itemId = item.getItemId();
            String itemName = item.getItemName();
            String itemPrice = "$ " + item.getItemPrice().toString();
            int itemInStock = item.getItemInStock();
            
            displayInventoryHeader();
            displayInventoryItemColumn(itemId, itemName, itemPrice, itemInStock);
        } else {
            io.print("ERROR: That item does not exist.");
        }
        io.readString("Please hit ENTER to continue.");
    }
    
    public void displayBannerRemoveItem() {
        io.print("============================");
        io.print("REMOVING ITEM FROM INVENTORY");
        io.print("============================");
    }
    
    public void displayRemoveResultMsg(Item item) {
        if(item != null) {
            io.print("Item successfully removed.");
        } else {
            io.print("ERROR: That item does not exist.");
        }
        io.readString("Please hit ENTER to contine.");
    }
    
    public String getConfirmToRemove(Item item) {
        String itemId = item.getItemId();
        String itemName = item.getItemName();
        io.print("==============================================");
        io.print("REMOVING ITEM ID: " + itemId + " - " + itemName);
        io.print("==============================================");
        
        String userAccessCode = "";
        
        String userChoice = getYesNo("Please confirm: ");
        if(userChoice.equalsIgnoreCase("y")) {
            userAccessCode = io.readString("Enter Access Code to remove item.");
        } else {
            userAccessCode = "n";
        }
        
        return userAccessCode;
    }
    
    public void displayBannerUpdate() {
        io.print("==========================");
        io.print("UPDATING ITEM IN INVENTORY");
        io.print("==========================");
    }
    public void displayBannerUpdateItem(Item item) {
        if (item != null) {
            io.print("=========================================");
            io.print("UPDATING FOR ITEM ID: " + item.getItemId());
            io.print("=========================================");
        }
    }
    
    public Item getInfoToUpdate(Item item) {
        String itemId = item.getItemId();
        
        String oldItemName = item.getItemName();
        BigDecimal oldItemPrice = item.getItemPrice();
        int oldItemInStock = item.getItemInStock();
        
        Item updateItem = new Item(itemId);
        io.print("CURRENT ITEM NAME: " + oldItemName);
        String userChoice = getYesNo("Would you like to update the Item Name? ");
        String updateItemName = "";
        if(userChoice.equalsIgnoreCase("y")) {
            boolean hasErrors = false;
            do {
                updateItemName = io.readString("Please enter Item Name: ");
                if(updateItemName.trim().length() == 0) {
                    io.print("ERROR: Item Name REQUIRED !!!!");
                    hasErrors = true;
                } else {
                    hasErrors = false;
                }
            }while(hasErrors);
        } else {
            updateItemName = oldItemName;
        }
        
        userChoice = getYesNo("Would you like to update the current item price of $" + oldItemPrice + "?");
        BigDecimal updateItemPrice = new BigDecimal("0");
        if(userChoice.equalsIgnoreCase("y")) {
            updateItemPrice = io.readBigDecimal("Please enter item price: ");
        } else {
            updateItemPrice = oldItemPrice;
        }
        
        userChoice = getYesNo("There are " + oldItemInStock + " item(s) in stock. Would you like to update this? ");
        int updateItemInStock = 0;
        if(userChoice.equalsIgnoreCase("y")) {
            updateItemInStock = io.readInt("Please enter number of items in stock: ");
        } else {
            updateItemInStock = oldItemInStock;
        }

        updateItem.setItemName(updateItemName);
        updateItem.setItemPrice(updateItemPrice);
        updateItem.setItemInStock(updateItemInStock);
        
        return updateItem; 
    }
    
    public String getYesNo(String question) {
        String userChoice = "";
        boolean tryAgain = false;
        
        do {
        userChoice = io.readString(question + " [y/n]");
        if(userChoice.equalsIgnoreCase("y")) {
            userChoice = "y";
            tryAgain = false;
        } else if(userChoice.equalsIgnoreCase("n")) {
            userChoice = "n";
            tryAgain = false;
        } else {
            io.print("That's not a valid choice");
            tryAgain = true;
        }
        
        } while(tryAgain);
        
        return userChoice;
    }
    
    public void displayBannerUpdateResultMsg(Item item) {
        if(item != null) {
            io.print("Item successfully updated.");
        } else {
            io.print("ERROR: That item does not exist.");
        }
        io.readString("Please hit ENTER to continue.");
    }
    
    public void displayErrorMsg(String errorMsg) {
        io.print("==============");
        io.print("!!! WHOOPS !!!");
        io.print("==============");
        io.print(errorMsg);
    }
    
    public void displayBannerExit() {
        io.print("=============================");
        io.print("GOOD BYE !!! HAVE A GREAT DAY");
        io.print("=============================");
    }
    
    public void displayBannerUnknownCommand() {
        io.print("===================");
        io.print("UNKNOWN COMMAND !!!");
        io.print("===================");
    }

    public void displayVMWelcomeHeader() {
        io.print("==================================================");
        io.print("WELCOME TO M3 VENDING MACHINE by DARYL DEL ROSARIO");
        io.print("==================================================");
    }
    
    public void displayVMMenuHeader() {
        System.out.printf(VM_COL_FORM, "Id", "Item Name", "Price");
        io.print("====================================");
    }
    
    public void displayVMItemColumn(String itemId, String itemName, String itemPrice) {
        System.out.printf(VM_COL_FORM, itemId, itemName, itemPrice);
    }
    
    public int displayVMSelect() {
        io.print("");
        io.print("What would you like to do today?");
        io.print("1. Purchase an Item");
        io.print("2. Access Inventory Control");
        io.print("3. Exit");
            
        return io.readInt("Please select an option from above.", 1, 3);
    }
    
    public void displayAvxItemsInStock(List<Item> itemList) {
        for(Item currentItem : itemList) {
            String itemId = currentItem.getItemId();
            String itemName = currentItem.getItemName();
            String itemPrice = "$ " + currentItem.getItemPrice().toString();
            
            displayVMItemColumn(itemId, itemName, itemPrice);
        }
    }
    
    public void displayBannerPurchaseItem() {
        io.print("==================");
        io.print("PURCHASING AN ITEM");
        io.print("==================");
    }
    
    public BigDecimal getMoneyDeposit() {
        BigDecimal depositAmount = io.readBigDecimal("Please deposit your money: ");
        return depositAmount;
    }
    
    public String getItemChoice() {
        String itemId = io.readString("Which item would you like to purchase? (Enter Item Id): ");
        return itemId;
    }

    public BigDecimal getReturnChange(BigDecimal deposit, BigDecimal itemPrice) {
        BigDecimal actualChange = deposit.subtract(itemPrice);
        return actualChange;
    }
    
    public void displayBannerEnterAccessCode(int attempts) {
        io.print("===============================");
        io.print("WARNING: ONLY " + attempts + " ATTEMPT(S) LEFT");
        io.print("===============================");
    }
    
    public void displayBannerAccessCodeFail(int attempts) {
        if(attempts > 0) {
            io.print("INCORRECT ACCESS CODE. " + attempts + " ATTEMPT(S) LEFT");
        } else {
            io.print("YOU ARE FIRED. GOOD BYE !!!");
            System.exit(0);
        }
    }
    
    public String getAccessCode() {
        return io.readString("Please enter the access code: ");
    }
    
    public void displayVMExitMessage() {
        io.print("========================");
        io.print("THANK YOU !!! COME AGAIN");
        io.print("========================");
    }
    
    public void displayBannerItemDispense(String itemName) {
        io.print("=============================");
        io.print("HERE IS YOUR " + itemName);
        io.print("=============================");
    }
    
    public void displayBannerYourChange(BigDecimal itemPrice) {
        io.print("=========================");
        io.print("YOUR CHANGE $" + itemPrice);
        io.print("=========================");
    }
    
    public void displayBannerMoneyBack(BigDecimal deposit) {
        io.print("===================================");
        io.print("HERE IS YOUR MONEY BACK $" + deposit);
        io.print("===================================");
    }
}
