/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.vendingmachine.controller;

import com.sg.vendingmachine.dao.VendingMachinePersistenceException;
import com.sg.vendingmachine.dto.CoinChange;
import com.sg.vendingmachine.dto.Item;
import com.sg.vendingmachine.service.VendingMachineDataValidationException;
import com.sg.vendingmachine.service.VendingMachineDuplicateIdException;
import com.sg.vendingmachine.service.VendingMachineInsufficientFundsException;
import com.sg.vendingmachine.service.VendingMachineNoItemInStockException;
import com.sg.vendingmachine.service.VendingMachineServiceLayer;
import com.sg.vendingmachine.view.VendingMachineView;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Daryl del Rosario
 */
public class VendingMachineController {
    
    private VendingMachineView view;
    private VendingMachineServiceLayer service;
    
    private static final String ACCESS_CODE = "Aug2021JAVA";
    
    public VendingMachineController(VendingMachineServiceLayer service, VendingMachineView view) {
        this.service = service;
        this.view = view;
    }
    
    public void runInventoryControl() {
        boolean keepGoing = true;
        int menuSelect = 0;
        
        try {
            while(keepGoing) {
                service.getAvailableItemsInStock();
                menuSelect = view.displayInventoryControlAccess();
                switch(menuSelect) {
                    case 1:
                        addNewItem();
                        break;
                    case 2:
                        updateItem();
                        break;
                    case 3:
                        viewSingleItem();
                    break;
                    case 4:
                        listAllItems();
                        break;
                    case 5:
                        listZeroStockItems();
                        break;
                    case 6:
                        removeItem();
                        break;
                    case 7:
                        runVendingMachine();
                        break;
                    case 8:
                        keepGoing = false;
                        break;

                    default:
                        view.displayBannerUnknownCommand();
                }
            }
            view.displayBannerExit();
            System.exit(0);
        } catch (VendingMachinePersistenceException | VendingMachineNoItemInStockException
                | VendingMachineDataValidationException e) {
            view.displayErrorMsg(e.getMessage());
        }
    }
    
    private void addNewItem() throws VendingMachinePersistenceException {
        view.displayBannerAddNewItem();
        boolean hasErrors = false;
        
        do {
            String itemId = view.getItemId("add");
            Item checkItem = service.getItem(itemId);
            if(checkItem != null) {
                view.displayMsg("ERROR: Could not add item because ITEM ID: " + itemId + " already exists."
                        + "\nTRY A DIFFERENT ITEM ID.");
                hasErrors = true;
            } else if (itemId.trim().length() == 0) {
                view.displayMsg("ERROR: NO ITEM ID WAS ENTERED.");
                hasErrors = true;
            } else if (itemId.equalsIgnoreCase("exit")) {
                return;
            } else {
                try {
                    Item newItem = view.getNewItemInfo(itemId);
                    service.createItem(newItem);
                    view.displayBannerAddNewItemSuccess();
                    hasErrors = false;
                } catch (VendingMachineDuplicateIdException | VendingMachineDataValidationException e) {
                    hasErrors = true;
                    view.displayErrorMsg(e.getMessage());
                }
            }
        } while (hasErrors);
    }

    private void listAllItems() throws VendingMachinePersistenceException {
        view.displayBannerAllItems();
        List<Item> itemList = service.getAllItems();
        view.displayAllItemList(itemList);
        view.waitForUser("Please hit ENTER to continue.");
    }
    
    private void listZeroStockItems() throws VendingMachinePersistenceException {
        view.displayBannerZeroStockItems();
        List<Item> zeroItemList = service.getZeroStockItems();
        if(zeroItemList.size() > 0) {
            view.displayAllItemList(zeroItemList);
        } else {
            view.displayMsg("All Items are In Stock");
        }
        view.waitForUser("Please hit ENTER to continue.");
    }
    
    private void viewSingleItem() throws VendingMachinePersistenceException {
        view.displayBannerSingleItem();
        String itemId = view.getItemId("view");
        Item item = service.getItem(itemId);
        view.displaySingleItem(item);
    }
    
    private void removeItem() throws VendingMachinePersistenceException {
        view.displayBannerRemoveItem();
        List<Item> itemList = service.getAllItems();
        view.displayAllItemList(itemList);
        String itemId = view.getItemId("remove");
        Item itemToBeRemoved = service.getItem(itemId);
        try {
            String accessCode = view.getConfirmToRemove(itemToBeRemoved);
            if(accessCode.equals(ACCESS_CODE)) {
                service.removeItem(itemId);
                view.displayRemoveResultMsg(itemToBeRemoved);
            } else if(accessCode.equalsIgnoreCase("n")) {
                view.displayMsg("==============");
                view.displayMsg("!!! WHOOPS !!!");
                view.displayMsg("==============");
            } else {
                view.displayMsg("========================================");
                view.displayMsg("!!! WHOOPS !!! INCORRECT ACCESS CODE !!!");
                view.displayMsg("========================================");
                view.waitForUser("Please hit ENTER to continue.");
            }
        } catch(Exception e) {
            view.displayRemoveResultMsg(null);
        }
    }
    
    private void updateItem() throws VendingMachinePersistenceException,
            VendingMachineDataValidationException{
        view.displayBannerUpdate();
        List<Item> itemList = service.getAllItems();
        view.displayAllItemList(itemList);
        String itemId = view.getItemId("update");
        Item itemToUpdate = service.getItem(itemId);
        try {
            view.displayBannerUpdateItem(itemToUpdate);
            Item updateItem = view.getInfoToUpdate(itemToUpdate);
            service.updateItem(itemId, updateItem);
            view.displayBannerUpdateResultMsg(updateItem);
        } catch (Exception e) {
            view.displayErrorMsg("ERROR: Nothing's Entered ! Return to Menu !!!");
            view.waitForUser("Please hit ENTER to continue.");
        }
    }
    
    public void runVendingMachine() {
        
        boolean vmOnOff = true;
        int vmSelect = 0;
        
        try {
            while(vmOnOff) {
                view.displayVMWelcomeHeader();
                getVMMenuList();
                
            
                vmSelect = view.displayVMSelect();
                switch(vmSelect) {
                    case 1:
                        vmPurchaseItem();
                        vmOnOff = true;
                        break;
                    case 2:
                        accessInventoryControl();
                        vmOnOff = false;
                        break;
                    case 3:
                        view.displayVMExitMessage();
                        System.exit(0);
                    default:
                        view.displayBannerUnknownCommand();
                }
            } 
        } catch (VendingMachinePersistenceException |
                VendingMachineNoItemInStockException |
                VendingMachineInsufficientFundsException e) {
                    view.displayMsg(e.getMessage());
        }
    }
    
    private void getVMMenuList() throws VendingMachinePersistenceException,
            VendingMachineNoItemInStockException {
        List<Item> itemList = service.getAvailableItemsInStock();
        if(itemList.size() > 0) {
            view.displayVMMenuHeader();
            view.displayAvxItemsInStock(itemList);
        } else {
            view.displayMsg("There are no item(s) in stock.");
            view.displayMsg("Must enter inventory control panel in order to add new items.");
            accessInventoryControl();
        }
    }
    
    private void vmPurchaseItem() throws VendingMachinePersistenceException,
            VendingMachineNoItemInStockException,
            VendingMachineInsufficientFundsException {
        view.displayBannerPurchaseItem();
        getVMMenuList();
        
        BigDecimal amount = view.getMoneyDeposit();
        String itemId = view.getItemChoice();
        
        try {
            Item chosenItem = service.getChosenItem(itemId);
            String chosenItemName = chosenItem.getItemName();
            BigDecimal chosenPrice = chosenItem.getItemPrice();
            
            service.checkSufficientFunds(amount, chosenItem);
            
            CoinChange inPennies = service.calculateChange(amount, chosenItem);
            BigDecimal actualChange = view.getReturnChange(amount, chosenPrice);
            
            int quarters = inPennies.getQuarters();
            int dimes = inPennies.getDimes();
            int nickels = inPennies.getNickels();
            int pennies = inPennies.getPennies();
            
            view.displayBannerItemDispense(chosenItemName);
            view.displayMsg("You gave $" + amount + " for this $" + chosenPrice + " " + chosenItemName);
            
            view.displayBannerYourChange(actualChange);
            view.displayMsg("Quarters: " + quarters);
            view.displayMsg("Dime: " + dimes);
            view.displayMsg("Nickels: " + nickels);
            view.displayMsg("Pennies: " + pennies);
            
            service.updateItemSale(chosenItem);
            
            view.waitForUser("Please hit ENTER to continue.");
        } catch (VendingMachineNoItemInStockException | VendingMachineInsufficientFundsException e) {
            view.displayErrorMsg(e.getMessage());
            Item chosenItem = service.getChosenItem(itemId);
            String chosenItemName = chosenItem.getItemName();
            BigDecimal chosenPrice = chosenItem.getItemPrice();
            
            CoinChange inPennies = service.calculateChange(amount, chosenItem);
            BigDecimal actualChange = view.getReturnChange(amount, new BigDecimal("0"));
            
            int quarters = inPennies.getQuarters();
            int dimes = inPennies.getDimes();
            int nickels = inPennies.getNickels();
            int pennies = inPennies.getPennies();
            
            view.displayBannerMoneyBack(actualChange);
            view.displayMsg("Quarters: " + quarters);
            view.displayMsg("Dime: " + dimes);
            view.displayMsg("Nickels: " + nickels);
            view.displayMsg("Pennies: " + pennies);
            
            view.waitForUser("Please hit ENTER to continue.");
        } 
    }
    
    private void accessInventoryControl() {
        int attempts = 3;
        
        do {
            view.displayBannerEnterAccessCode(attempts);
            String accessCode = view.getAccessCode();
            if(ACCESS_CODE.equals(accessCode)) {
                runInventoryControl();
            } else {
                attempts = attempts - 1;
                view.displayBannerAccessCodeFail(attempts);
            }
        } while(attempts > 0);
    }
}
