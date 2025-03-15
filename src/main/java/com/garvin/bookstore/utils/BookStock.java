package com.garvin.bookstore.utils;

public class BookStock {
    private long book_id;
    private String type;
    private long stock;
    private long quantity;

    BookStock(long book_id, String type, long stock, long quantity) {
        this.book_id = book_id;
        this.type = type;
        this.stock = stock;
        this.quantity = quantity;
    }

    public boolean hasEnough() {
        return revisedStock() >= 0;
    }

    public long revisedStock() { return stock - quantity; }

    public long getBook_id() {
        return book_id;
    }

    public void setBook_id(long book_id) {
        this.book_id = book_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
