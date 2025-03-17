package com.garvin.bookstore.service;

import com.garvin.bookstore.db.BookRepository;
import com.garvin.bookstore.db.CustomerRepository;
import com.garvin.bookstore.db.InventoryRepository;
import com.garvin.bookstore.model.InventoryModel;
import com.garvin.bookstore.model.RequestOperationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {
    @Autowired
    BookRepository bookRepository;

    @Autowired
    InventoryRepository inventoryRepository;

    public String addInventory(long isbn, InventoryModel inventoryModel) {
        return RequestOperationStatus.SUCCESS.name();
    }

    public String updateInventory(long isbn, InventoryModel inventoryModel) {
        return RequestOperationStatus.SUCCESS.name();
    }

    public String deleteInventory(long isbn, InventoryModel inventoryModel) {
        return RequestOperationStatus.SUCCESS.name();
    }
}
