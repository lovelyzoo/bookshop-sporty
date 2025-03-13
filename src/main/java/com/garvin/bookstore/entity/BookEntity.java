package com.garvin.bookstore.entity;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

@Entity(name = "books")
public class BookEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 933999866115777188L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long book_id;

    private String title;
    private String author;

    // TODO: guarantee uniqueness
    @Column(nullable = false)
    private long isbn;

    @Column(nullable = false)
    private BigDecimal basePrice;

    @OneToMany (fetch = FetchType.LAZY)
    @JoinColumn (name = "book_id", referencedColumnName = "book_id")
    private Set<InventoryEntity> inventory;

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

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public Set<InventoryEntity> getInventory() {
        return inventory;
    }
}
