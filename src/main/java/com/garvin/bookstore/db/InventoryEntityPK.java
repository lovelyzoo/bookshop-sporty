package com.garvin.bookstore.db;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class InventoryEntityPK implements Serializable {

    @Serial
    private static final long serialVersionUID = 5708308822017118874L;

    private long book_id;
    private String type;

    public InventoryEntityPK() {}

    public InventoryEntityPK(long book_id, String type) {
        this.book_id = book_id;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InventoryEntityPK that = (InventoryEntityPK) o;
        return book_id == that.book_id && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(book_id, type);
    }

}
