package com.garvin.bookstore.utils;

import com.garvin.bookstore.db.BookEntity;
import com.garvin.bookstore.db.BookRepository;
import com.garvin.bookstore.db.InventoryEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Set;

public class PurchaseTracker {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseTracker.class);

    private final HashMap<String, BookStock> bookStocks;

    public PurchaseTracker() {
        this.bookStocks = new HashMap<String, BookStock>();
    }


    public boolean hasEnoughStock(BookEntity bookEntity, String type, long quantity) {
        if (bookEntity == null) {
            logger.warn("bookEntity is null");
            return false;
        }

        long isbn = bookEntity.getIsbn();
        String key = String.format("%s %s", isbn, type);
        if (!bookStocks.containsKey(key)) {
            long stock = getStock(type, bookEntity.getInventory());
            bookStocks.put(key, new BookStock(bookEntity.getBook_id(), type, stock, 0L));
        }

        BookStock bookStock = bookStocks.get(key);
        bookStock.setQuantity(bookStock.getQuantity() + quantity);

        return bookStock.hasEnough();
    }

    private static long getStock(String type, Set<InventoryEntity> inventoryEntities) {
        for (InventoryEntity inventoryEntity : inventoryEntities) {
            if (inventoryEntity.getType().equals(type)) {
                return inventoryEntity.getStock();
            }
        }
        // If it's not in the inventory, then there is no stock
        return 0L;
    }

    public HashMap<String, BookStock> getBookStocks() {
        return bookStocks;
    }
}
