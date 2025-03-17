package com.garvin.bookstore.service;

import com.garvin.bookstore.db.*;
import com.garvin.bookstore.model.BookModel;
import com.garvin.bookstore.model.InventoryModel;
import com.garvin.bookstore.model.RequestOperationStatus;
import com.garvin.bookstore.properties.BookTypeProperties;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
public class InventoryService {
    @Autowired
    BookRepository bookRepository;

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    BookTypeProperties bookTypeProperties;

    public String addInventory(long isbn, InventoryModel inventoryModel) {
        String type = inventoryModel.getType();
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

        InventoryEntity inventoryEntity = new InventoryEntity();
        BeanUtils.copyProperties(inventoryModel, inventoryEntity);
        inventoryEntity.setBook_id(book_id);
        inventoryRepository.save(inventoryEntity);

        return RequestOperationStatus.SUCCESS.name();
    }

    public String updateInventory(long isbn, InventoryModel inventoryModel) {        String type = inventoryModel.getType();
        if (!bookTypeProperties.containsType(type)) {
            return RequestOperationStatus.TYPE_NOT_CONFIGURED.name();
        }

        Long book_id = bookRepository.findBook_idByIsbn(isbn);
        if (book_id == null) {
            return RequestOperationStatus.ISBN_NOT_IN_DB.name();
        }
        InventoryEntity inventoryEntity = new InventoryEntity();
        BeanUtils.copyProperties(inventoryModel, inventoryEntity);
        inventoryEntity.setBook_id(book_id);
        inventoryRepository.save(inventoryEntity);

        return RequestOperationStatus.SUCCESS.name();
    }

    //@Transactional
    public String deleteInventory(long isbn, InventoryModel inventoryModel) {
        Long book_id = bookRepository.findBook_idByIsbn(isbn);
        if (book_id == null) {
            return RequestOperationStatus.ISBN_NOT_IN_DB.name();
        }
        InventoryEntityPK inventoryEntityPK = new InventoryEntityPK(book_id, inventoryModel.getType());
        inventoryRepository.deleteById(inventoryEntityPK);

        return RequestOperationStatus.SUCCESS.name();
    }
}
