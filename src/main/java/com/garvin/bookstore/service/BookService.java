package com.garvin.bookstore.service;

import com.garvin.bookstore.entity.BookEntity;
import com.garvin.bookstore.entity.BookRepository;
import com.garvin.bookstore.model.BookDetailsModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    public BookDetailsModel getBook(long isbn) {
        Iterable<BookEntity> bookEntities = bookRepository.findAll();
        BookEntity bookEntity = bookRepository.findByIsbn(isbn);

        BookDetailsModel returnValue = new BookDetailsModel();
        BeanUtils.copyProperties(bookEntity, returnValue);

        return returnValue;
    }

    public List<BookDetailsModel> getBooks() {
        List<BookDetailsModel> returnValue = new ArrayList<>();

        List<BookEntity> bookEntities = bookRepository.findAll();
        for (BookEntity bookEntity: bookEntities) {
            BookDetailsModel bookDetailsModel = new BookDetailsModel();
            BeanUtils.copyProperties(bookEntity, bookDetailsModel);
            returnValue.add(bookDetailsModel);
        }

        return returnValue;
    }

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
