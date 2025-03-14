package com.garvin.bookstore.service;

import com.garvin.bookstore.controller.PurchaseController;
import com.garvin.bookstore.db.BookEntity;
import com.garvin.bookstore.db.BookRepository;
import com.garvin.bookstore.model.PurchaseItemModel;
import com.garvin.bookstore.model.PurchaseModel;
import com.garvin.bookstore.properties.BookTypeProperties;
import jakarta.annotation.PostConstruct;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
public class PurchaseService {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookTypeProperties bookTypeProperties;

    private HashMap<String, BigDecimal> priceModifier = new HashMap<String, BigDecimal>();
    private HashMap<String, BigDecimal> bundleModifier = new HashMap<String, BigDecimal>();

    @PostConstruct
    private void postConstruct() {
        for (BookTypeProperties.BookType bookType : bookTypeProperties.getBooktypes()) {
            priceModifier.put(bookType.getType(), bookType.getPricemodifier());
            bundleModifier.put(bookType.getType(), bookType.getBundlemodifier());
        }
    }

    public String getPrice(PurchaseModel purchaseModel) {
        // Test get the info
        logger.info(purchaseModel.getUserId());
        //        for (PurchaseItemModel item: purchaseModel.getPurchaseItems()){
        //            logger.info("{} {}", Long.toString(item.getIsbn()), item.getType());
        //        }

        long totalItems = purchaseModel.getPurchaseItems().stream()
                .map(PurchaseItemModel::getQuantity)
                .mapToLong(Long::longValue).sum();
        logger.info("totalItems:" + totalItems);

        BigDecimal total = BigDecimal.ZERO;
        for (PurchaseItemModel item: purchaseModel.getPurchaseItems()) {
            BookEntity bookEntity = bookRepository.findByIsbn(item.getIsbn());

            BigDecimal modifier = priceModifier.get(item.getType());
            if (totalItems > 3) {
                modifier = bundleModifier.get(item.getType());
            }
            total = total.add(bookEntity.getBasePrice().multiply(modifier));
        }
        logger.info("total: " + String.valueOf(total));
        return "getPrice was called";
    }
}
