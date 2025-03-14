package com.garvin.bookstore.service;

import com.garvin.bookstore.controller.PurchaseController;
import com.garvin.bookstore.db.BookEntity;
import com.garvin.bookstore.db.BookRepository;
import com.garvin.bookstore.model.PurchaseItemModel;
import com.garvin.bookstore.model.PurchaseModel;
import com.garvin.bookstore.properties.GlobalProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PurchaseService {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);

    @Autowired
    BookRepository bookRepository;

    @Autowired
    GlobalProperties globalProperties;

    public String getPrice(PurchaseModel purchaseModel) {
        // Test get the info
        logger.info(purchaseModel.getUserId());
        for (GlobalProperties.BookType bookType : globalProperties.getBooktypes()) {
            logger.info(bookType.getType());
        }

        BigDecimal total = BigDecimal.ZERO;
        for (PurchaseItemModel item: purchaseModel.getPurchaseItems()) {
            BookEntity bookEntity = bookRepository.findByIsbn(item.getIsbn());
            logger.info(Long.toString(item.getIsbn()) + " " + item.getType() + " " + bookEntity.getBasePrice());
            total = total.add(bookEntity.getBasePrice());
        }
        logger.info("total: " + String.valueOf(total));
        return "getPrice was called";
    }
}
