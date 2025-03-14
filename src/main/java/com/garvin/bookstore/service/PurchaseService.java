package com.garvin.bookstore.service;

import com.garvin.bookstore.controller.PurchaseController;
import com.garvin.bookstore.entity.BookEntity;
import com.garvin.bookstore.entity.BookRepository;
import com.garvin.bookstore.model.PurchaseItemModel;
import com.garvin.bookstore.model.PurchaseModel;
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

    public String getPrice(PurchaseModel purchaseModel) {
        logger.info(purchaseModel.getUserId());
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
