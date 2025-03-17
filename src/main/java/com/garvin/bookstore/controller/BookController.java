package com.garvin.bookstore.controller;

import com.garvin.bookstore.model.BookModel;
import com.garvin.bookstore.model.OperationStatusModel;
import com.garvin.bookstore.model.RequestOperationName;
import com.garvin.bookstore.model.RequestOperationStatus;
import com.garvin.bookstore.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("books")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    BookService bookService;

    @GetMapping("/{isbn}")
    public BookModel getBook(@PathVariable long isbn) {
        logger.info("GET /books/{isbn} endpoint called");
        return bookService.getBook(isbn);
    }

    @GetMapping()
    public List<BookModel> getBooks() {
        logger.info("GET /books endpoint called");
        return bookService.getBooks();
    }

    @PostMapping()
    public OperationStatusModel addBook(@RequestBody BookModel bookModel) {
        logger.info("POST /books endpoint called");

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.ADD_BOOK.name());
        returnValue.setOperationResult(bookService.addBook(bookModel));
        return returnValue;
    }

    @PutMapping()
    public OperationStatusModel updateBook(@RequestBody BookModel bookModel) {
        logger.info("PUT /books endpoint called");

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.UPDATE_BOOK.name());
        returnValue.setOperationResult(bookService.updateBook(bookModel));
        return returnValue;
    }

    @DeleteMapping("/{isbn}")
    public OperationStatusModel deleteBook(@PathVariable long isbn) {
        logger.info("DELETE /books/{isbn} endpoint called");

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE_BOOK.name());
        returnValue.setOperationResult(bookService.deleteBook(isbn));
        return returnValue;
    }
}