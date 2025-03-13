package com.garvin.bookstore.model;

import com.garvin.bookstore.entity.InventoryEntity;

import java.math.BigDecimal;
import java.util.Set;

public class BookDetailsModel {
    private String title;
    private String author;
    private long isbn;
    private BigDecimal base_price;
    private Set<InventoryEntity> inventory;

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

    public Set<InventoryEntity> getInventory() {
        return inventory;
    }

    public void setInventory(Set<InventoryEntity> inventory) {
        this.inventory = inventory;
    }
}
