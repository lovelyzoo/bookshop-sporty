package com.garvin.bookstore.db;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

@Entity(name = "inventory")
@IdClass(InventoryEntityPK.class)
public class InventoryEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -6055555627798684828L;

    @Id
    private long book_id;
    @Id
    private String type;
    private long stock;

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
}
