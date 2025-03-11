package com.garvin.bookstore.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("book")
public class BookController {

    @GetMapping
    public String getBook() {
        return "getBook was called\n";
    }

    @PostMapping
    public String createBook() {
        return "createBook was called\n";
    }
}
