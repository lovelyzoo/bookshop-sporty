package com.garvin.bookstore.service;

import com.garvin.bookstore.model.BookDetailsModel;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    public BookDetailsModel createBook(BookDetailsModel bookDetails) {
        BookDetailsModel blah = new BookDetailsModel();
        blah.setTitle("Whatevs");
        blah.setAuthor("Me");
        blah.setIsbn(1234L);
        return blah;
    }
}
