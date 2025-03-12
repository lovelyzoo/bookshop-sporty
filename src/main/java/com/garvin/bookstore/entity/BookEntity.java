package com.garvin.bookstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity(name = "tBooks")
public class BookEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 933999866115777188L;

    @Id
    @GeneratedValue
    private long book_id;

    private String title;
    private String author;

    // TODO: guarantee uniqueness
    @Column(nullable = false)
    private long isbn;

    @Column(nullable = false)
    private BigDecimal base_price;

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

    public BigDecimal getBase_price() {
        return base_price;
    }

    public void setBase_price(BigDecimal base_price) {
        this.base_price = base_price;
    }
}
