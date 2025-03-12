package com.garvin.bookstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.io.Serial;
import java.io.Serializable;

@Entity(name = "tBooks")
public class BookEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 933999866115777188L;

    @Id
    @GeneratedValue
    private long book_id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    // TODO: guarantee uniqueness
    @Column(nullable = false)
    private long isbn;

    public long getBook_id() {
        return book_id;
    }

    public void setBook_id(long book_id) {
        this.book_id = book_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }
}
