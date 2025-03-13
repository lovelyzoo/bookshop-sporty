package com.garvin.bookstore.model;

import java.math.BigDecimal;
import java.util.Set;

public class BookModel {
    private String title;
    private String author;
    private long isbn;
    private BigDecimal basePrice;
    private Set<InventoryModel> inventory;

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

    public Set<InventoryModel> getInventory() {
        return inventory;
    }

    public void setInventory(Set<InventoryModel> inventory) {
        this.inventory = inventory;
    }
}
