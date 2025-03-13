package com.garvin.bookstore.service;

import com.garvin.bookstore.entity.BookEntity;
import com.garvin.bookstore.entity.BookRepository;
import com.garvin.bookstore.entity.InventoryEntity;
import com.garvin.bookstore.entity.InventoryRepository;
import com.garvin.bookstore.model.BookDetailsModel;
import com.garvin.bookstore.model.InventoryModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    BookRepository bookRepository;

    @Autowired
    InventoryRepository inventoryRepository;

    public BookDetailsModel getBook(long isbn) {
        return createModel(bookRepository.findByIsbn(isbn));
    }

    public List<BookDetailsModel> getBooks() {
        List<BookDetailsModel> returnValue = new ArrayList<>();

        List<BookEntity> bookEntities = bookRepository.findAll();
        for (BookEntity bookEntity: bookEntities) {
            BookDetailsModel bookDetailsModel = createModel(bookEntity);
            returnValue.add(bookDetailsModel);
        }

        return returnValue;
    }

    private BookDetailsModel createModel(BookEntity bookEntity) {
        BookDetailsModel returnValue = new BookDetailsModel();
        BeanUtils.copyProperties(bookEntity, returnValue);

        Set<InventoryModel> inventory = new HashSet<>();
        for (InventoryEntity inventoryEntity: bookEntity.getInventory()) {
            InventoryModel inventoryModel = new InventoryModel();
            BeanUtils.copyProperties(inventoryEntity, inventoryModel);
            inventory.add(inventoryModel);
        }
        returnValue.setInventory(inventory);

        return returnValue;
    }

    // TODO: remove this
    public BookDetailsModel createBook(BookDetailsModel bookDetails) {
        BookEntity bookEntity = new BookEntity();
        BeanUtils.copyProperties(bookDetails, bookEntity);

        BookEntity storedDetails = bookRepository.save(bookEntity);

        BookDetailsModel returnValue = new BookDetailsModel();
        BeanUtils.copyProperties(storedDetails, returnValue);

        return returnValue;
    }
}
