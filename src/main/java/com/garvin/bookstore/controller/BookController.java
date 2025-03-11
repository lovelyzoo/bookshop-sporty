package com.garvin.bookstore.controller;

import com.garvin.bookstore.model.BookDetailsModel;
import com.garvin.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("book")
public class BookController {

    @Autowired
    BookService bookService;

    @GetMapping
    public String getBook() {
        return "getBook was called\n";
    }

    @PostMapping
    public BookDetailsModel createBook(@RequestBody BookDetailsModel bookDetails) {

        return bookService.createBook(bookDetails);
    }
}
