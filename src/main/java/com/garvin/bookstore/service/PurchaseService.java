package com.garvin.bookstore.service;

import com.garvin.bookstore.controller.PurchaseController;
import com.garvin.bookstore.db.BookEntity;
import com.garvin.bookstore.db.BookRepository;
import com.garvin.bookstore.db.CustomerEntity;
import com.garvin.bookstore.db.CustomerRepository;
import com.garvin.bookstore.model.PurchaseItemModel;
import com.garvin.bookstore.model.PurchaseModel;
import com.garvin.bookstore.properties.BookTypeProperties;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;

@Service
public class PurchaseService {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseService.class);

    @Autowired
    BookRepository bookRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BookTypeProperties bookTypeProperties;

    private HashMap<String, BigDecimal> priceModifier = new HashMap<String, BigDecimal>();
    private HashMap<String, BigDecimal> bundleModifier = new HashMap<String, BigDecimal>();

    @PostConstruct
    private void postConstruct() {
        // Intialise modifier maps from config
        for (BookTypeProperties.BookType bookType : bookTypeProperties.getBooktypes()) {
            priceModifier.put(bookType.getType(), bookType.getPricemodifier());
            bundleModifier.put(bookType.getType(), bookType.getBundlemodifier());
        }
    }

    public String getPrice(PurchaseModel purchaseModel) {
        HashMap<String, Long> freeItems = new HashMap<String, Long>();
        for (PurchaseItemModel item: purchaseModel.getFreeItems()) {
            String key = String.format("%s%s", item.getIsbn(), item.getType());
            Long previous = freeItems.put(key, item.getQuantity());
            if (previous != null) {
                logger.info("{} has been seen before", key);
            }
        }

        CustomerEntity customerEntity = customerRepository.findByUserId(purchaseModel.getUserId());
        long currentLoyaltyPoints = customerEntity.getLoyaltyPoints();

        long purchaseItemsCount = purchaseModel.getPurchaseItems().stream()
                .map(PurchaseItemModel::getQuantity)
                .mapToLong(Long::longValue).sum();
        long freeItemsCount = purchaseModel.getFreeItems().stream()
                        .map(PurchaseItemModel::getQuantity)
                                .mapToLong(Long::longValue).sum();
        long bundleSize = purchaseItemsCount - freeItemsCount;
        logger.info("bundleSize:" + bundleSize);

        BigDecimal total = BigDecimal.ZERO;
        long freeBooksTotal = 0;
        long loyaltyPointsAwarded = 0;
        for (PurchaseItemModel item: purchaseModel.getPurchaseItems()) {
            BookEntity bookEntity = bookRepository.findByIsbn(item.getIsbn());

            Long purchaseQuantity = item.getQuantity();
            Long freeQuantity = freeItems.getOrDefault(String.format("%s%s", item.getIsbn(), item.getType()), 0L);
            if (freeQuantity <= purchaseQuantity) {
                freeBooksTotal = freeBooksTotal + freeQuantity;
                purchaseQuantity = purchaseQuantity - freeQuantity;
                loyaltyPointsAwarded = loyaltyPointsAwarded + purchaseQuantity;

                BigDecimal modifier = priceModifier.get(item.getType());
                if (bundleSize >= 3) {
                    modifier = bundleModifier.get(item.getType());
                }

                total = total.add(bookEntity.getBasePrice()
                                .multiply(modifier)
                                .multiply(BigDecimal.valueOf(purchaseQuantity))
                );
            } else {
                logger.info("more free copies have been requested than what's available");
            }
        }

        long loyaltyPointsCredit = currentLoyaltyPoints + loyaltyPointsAwarded;
        long loyaltyPointsDebit = freeBooksTotal * 10L;

        if (loyaltyPointsDebit > loyaltyPointsCredit) {
            logger.info("not enough loyalty points to cover requested free books");
        }

        logger.info("total: {} lpcredit: {} lpdebit: {}", String.valueOf(total), String.valueOf(loyaltyPointsCredit), String.valueOf(loyaltyPointsDebit));
        return String.valueOf(total);
    }
}
