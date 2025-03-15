package com.garvin.bookstore.utils;

import com.garvin.bookstore.db.BookEntity;
import com.garvin.bookstore.db.BookRepository;
import com.garvin.bookstore.db.InventoryEntity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class PurchaseStockTracker {

    BookRepository bookRepository;
    private HashMap<String, BookStock> bookStocks;

    public PurchaseStockTracker(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        this.bookStocks = new HashMap<String, BookStock>();
    }

    public boolean incrementItem(long isbn, String type, long quantity) {
        String key = String.format("%s %s", isbn, type);
        if (!bookStocks.containsKey(key)) {
            BookEntity bookEntity = bookRepository.findByIsbn(isbn);
            long stock = getStock(type, bookEntity.getInventory());
            bookStocks.put(key, new BookStock(bookEntity.getBook_id(), type, stock, 0L));
        }

        BookStock bookStock = bookStocks.get(key);
        bookStock.setQuantity(bookStock.getQuantity() + quantity);

        return bookStock.hasEnough();
    }

    private static long getStock(String type, Set<InventoryEntity> inventoryEntities) {
        Iterator<InventoryEntity> inventoryEntityIterator = inventoryEntities.iterator();
        while(inventoryEntityIterator.hasNext()){
            InventoryEntity inventoryEntity = inventoryEntityIterator.next();
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
