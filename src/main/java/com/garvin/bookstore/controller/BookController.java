package com.garvin.bookstore.controller;

import com.garvin.bookstore.model.BookModel;
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
}