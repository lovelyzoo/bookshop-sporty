package com.garvin.bookstore.service;

import com.garvin.bookstore.controller.BookController;
import com.garvin.bookstore.db.BookEntity;
import com.garvin.bookstore.db.BookRepository;
import com.garvin.bookstore.db.InventoryEntity;
import com.garvin.bookstore.db.InventoryRepository;
import com.garvin.bookstore.model.BookModel;
import com.garvin.bookstore.model.InventoryModel;
import com.garvin.bookstore.model.RequestOperationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);


    @Autowired
    BookRepository bookRepository;

    @Autowired
    InventoryRepository inventoryRepository;

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

    private String doAddBook(BookModel bookModel) {
        BookEntity bookEntity = new BookEntity();
        BeanUtils.copyProperties(bookModel, bookEntity);
        if (bookEntity.getBasePrice() == null) {
            bookEntity.setBasePrice(BigDecimal.valueOf(0.00));
        }

        try {
            bookRepository.save(bookEntity);
        } catch (Exception e) {
            return RequestOperationStatus.DB_ERROR.name();
        }

        return RequestOperationStatus.SUCCESS.name();
    }

    public String addBook(BookModel bookModel) {
        Long isbn = bookModel.getIsbn();
        if (isbn == null) {
            return RequestOperationStatus.ISBN_NOT_IN_REQ.name();
        }

        Long book_id = bookRepository.findBook_idByIsbn(isbn);
        if (book_id != null) {
            return RequestOperationStatus.ISBN_ALREADY_IN_DB.name();
        }

        return doAddBook(bookModel);
    }

    public String updateBook(BookModel bookModel) {
        Long isbn = bookModel.getIsbn();
        if (isbn == null) {
            return RequestOperationStatus.ISBN_NOT_IN_REQ.name();
        }

        Long book_id = bookRepository.findBook_idByIsbn(isbn);
        if (book_id == null) {
            return doAddBook(bookModel);
        }

        BigDecimal basePrice = bookModel.getBasePrice();
        if (basePrice == null) {
            basePrice = BigDecimal.valueOf(0.00);
        }
        bookRepository.updateBookByIsbn(bookModel.getTitle(), bookModel.getAuthor(), basePrice, isbn);

        return RequestOperationStatus.SUCCESS.name();
    }

    public String deleteBook(long isbn) {
        if (inventoryRepository.countByIsbn(isbn) > 0) {
            return RequestOperationStatus.BOOK_HAS_INVENTORY_ENTRIES.name();
        }

        try {
            bookRepository.deleteByIsbn(isbn);
        } catch (Exception e) {
            return RequestOperationStatus.DB_ERROR.name();
        }

        return RequestOperationStatus.SUCCESS.name();
    }

}
