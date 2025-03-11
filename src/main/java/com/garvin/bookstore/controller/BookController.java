package com.garvin.bookstore.controller;

import com.garvin.bookstore.model.BookDetailsModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("book")
public class BookController {

    @GetMapping
    public String getBook() {
        return "getBook was called\n";
    }

    @PostMapping
    public BookDetailsModel createBook(@RequestBody BookDetailsModel bookDetails) {

        return bookDetails;
    }
}
