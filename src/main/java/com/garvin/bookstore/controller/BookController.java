package com.garvin.bookstore.controller;

import com.garvin.bookstore.model.BookModel;
import com.garvin.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("books")
public class BookController {
    @Autowired
    BookService bookService;

    @GetMapping("/{isbn}")
    public BookModel getBook(@PathVariable long isbn) {
        return bookService.getBook(isbn);
    }

    @GetMapping()
    public List<BookModel> getBooks() {
        return bookService.getBooks();
    }
}