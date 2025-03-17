package com.garvin.bookstore.controller;

import com.garvin.bookstore.model.*;
import com.garvin.bookstore.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("inventory")
public class InventoryController {
    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);

    @Autowired
    InventoryService inventoryService;

    @PostMapping("/{isbn}")
    public OperationStatusModel addInventory(@PathVariable long isbn, @RequestBody InventoryModel inventoryModel) {
        logger.info("POST /inventory/{isbn} endpoint called");

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.ADD_INVENTORY.name());
        returnValue.setOperationResult(inventoryService.addInventory(isbn, inventoryModel));
        return returnValue;
    }

    @PutMapping("/{isbn}")
    public OperationStatusModel updateInventory(@PathVariable long isbn, @RequestBody InventoryModel inventoryModel) {
        logger.info("PUT /inventory/{isbn} endpoint called");

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.UPDATE_INVENTORY.name());
        returnValue.setOperationResult(inventoryService.updateInventory(isbn, inventoryModel));
        return returnValue;
    }

    @DeleteMapping("/{isbn}")
    public OperationStatusModel deleteInventory(@PathVariable long isbn, @RequestBody InventoryModel inventoryModel) {
        logger.info("DELETE /inventory/{isbn} endpoint called");

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE_INVENTORY.name());
        returnValue.setOperationResult(inventoryService.deleteInventory(isbn, inventoryModel));
        return returnValue;
    }
}
