package com.garvin.bookstore.controller;

import com.garvin.bookstore.model.BookDetailsModel;
import com.garvin.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("book")
public class BookController {

    @Autowired
    BookService bookService;

    @GetMapping("/{isbn}")
    public BookDetailsModel getBook(@PathVariable long isbn) {
        return bookService.getBook(isbn);
    }

    @GetMapping()
    public List<BookDetailsModel> getBooks() {
        return bookService.getBooks();
    }

    @PostMapping
    public BookDetailsModel createBook(@RequestBody BookDetailsModel bookDetails) {
        return bookService.createBook(bookDetails);
    }
}