package com.garvin.bookstore.service;

import com.garvin.bookstore.entity.BookEntity;
import com.garvin.bookstore.entity.BookRepository;
import com.garvin.bookstore.model.BookDetailsModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    public BookDetailsModel createBook(BookDetailsModel bookDetails) {
        BookEntity bookEntity = new BookEntity();
        BeanUtils.copyProperties(bookDetails, bookEntity);

        BookEntity storedDetails = bookRepository.save(bookEntity);
        // TODO: log the book_id here

        BookDetailsModel returnValue = new BookDetailsModel();
        BeanUtils.copyProperties(storedDetails, returnValue);

        return returnValue;
    }
}
