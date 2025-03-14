package com.garvin.bookstore.service;

import com.garvin.bookstore.db.BookEntity;
import com.garvin.bookstore.db.BookRepository;
import com.garvin.bookstore.db.InventoryEntity;
import com.garvin.bookstore.model.BookModel;
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


    public BookModel getBook(long isbn) {

        return createModel(bookRepository.findByIsbn(isbn));
    }

    public List<BookModel> getBooks() {
        List<BookModel> returnValue = new ArrayList<>();

        List<BookEntity> bookEntities = bookRepository.findAll();
        for (BookEntity bookEntity: bookEntities) {
            BookModel bookModel = createModel(bookEntity);
            returnValue.add(bookModel);
        }

        return returnValue;
    }

    private BookModel createModel(BookEntity bookEntity) {
        BookModel returnValue = new BookModel();
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

}
