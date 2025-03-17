package com.garvin.bookstore.service;

import com.garvin.bookstore.db.*;
import com.garvin.bookstore.model.InventoryModel;
import com.garvin.bookstore.model.RequestOperationStatus;
import com.garvin.bookstore.properties.BookTypeProperties;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InventoryService {
    @Autowired
    BookRepository bookRepository;

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    BookTypeProperties bookTypeProperties;

    private String saveEntity(Long book_id, InventoryModel inventoryModel) {
        InventoryEntity inventoryEntity = new InventoryEntity();
        BeanUtils.copyProperties(inventoryModel, inventoryEntity);
        inventoryEntity.setBook_id(book_id);
        try {
            inventoryRepository.save(inventoryEntity);
        } catch (Exception e) {
            return RequestOperationStatus.DB_ERROR.name();
        }

        return RequestOperationStatus.SUCCESS.name();
    }

    public String addInventory(long isbn, InventoryModel inventoryModel) {
        String type = inventoryModel.getType();
        if (type == null) {
            return RequestOperationStatus.TYPE_NOT_IN_REQ.name();
        }
        if (!bookTypeProperties.containsType(type)) {
            return RequestOperationStatus.TYPE_NOT_CONFIGURED.name();
        }

        Long book_id = bookRepository.findBook_idByIsbn(isbn);
        if (book_id == null) {
            return RequestOperationStatus.ISBN_NOT_IN_DB.name();
        }

        InventoryEntityPK inventoryEntityPK = new InventoryEntityPK(book_id, type);
        Optional<InventoryEntity> optionalInventoryEntity = inventoryRepository.findById(inventoryEntityPK);
        if (optionalInventoryEntity.isPresent()) {
            return RequestOperationStatus.TYPE_ALREADY_IN_DB.name();
        }

        return saveEntity(book_id, inventoryModel);
    }

    public String updateInventory(long isbn, InventoryModel inventoryModel) {
        String type = inventoryModel.getType();
        if (type == null) {
            return RequestOperationStatus.TYPE_NOT_IN_REQ.name();
        }
        if (!bookTypeProperties.containsType(type)) {
            return RequestOperationStatus.TYPE_NOT_CONFIGURED.name();
        }

        Long book_id = bookRepository.findBook_idByIsbn(isbn);
        if (book_id == null) {
            return RequestOperationStatus.ISBN_NOT_IN_DB.name();
        }

        return saveEntity(book_id, inventoryModel);
    }

    public String deleteInventory(long isbn, InventoryModel inventoryModel) {
        String type = inventoryModel.getType();
        if (type == null) {
            return RequestOperationStatus.TYPE_NOT_IN_REQ.name();
        }

        Long book_id = bookRepository.findBook_idByIsbn(isbn);
        if (book_id == null) {
            return RequestOperationStatus.ISBN_NOT_IN_DB.name();
        }

        InventoryEntityPK inventoryEntityPK = new InventoryEntityPK(book_id, type);
        try {
            inventoryRepository.deleteById(inventoryEntityPK);
        } catch (Exception e) {
            return RequestOperationStatus.DB_ERROR.name();
        }

        return RequestOperationStatus.SUCCESS.name();
    }
}
