package com.garvin.bookstore.utils;

class BookStock {
    private long stock;
    private long quantity;

    BookStock(long stock, long quantity) {
        this.stock = stock;
        this.quantity = quantity;
    }

    boolean hasEnough() {
        return quantity <= stock;
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
