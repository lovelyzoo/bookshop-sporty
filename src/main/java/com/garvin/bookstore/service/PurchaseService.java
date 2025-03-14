package com.garvin.bookstore.service;

import com.garvin.bookstore.controller.PurchaseController;
import com.garvin.bookstore.db.*;
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
import java.util.Iterator;
import java.util.Set;

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

    private static boolean isPurchaseInStock(PurchaseItemModel purchaseItemModel, Set<InventoryEntity> inventoryEntities) {
        Iterator<InventoryEntity> inventoryEntityIterator = inventoryEntities.iterator();
        while(inventoryEntityIterator.hasNext()){
            InventoryEntity inventoryEntity = inventoryEntityIterator.next();
            if (inventoryEntity.getType().equals(purchaseItemModel.getType())) {
                if (inventoryEntity.getStock() >= purchaseItemModel.getQuantity()) {
                    return true;
                } else {
                    return false;
                }

            }
        }
        return false;
    }

    class CalculateOutcomeReturnValue {
        BigDecimal totalCost;
        long loyaltyPointsBalance;

        public BigDecimal getTotalCost() {
            return totalCost;
        }

        public void setTotalCost(BigDecimal totalCost) {
            this.totalCost = totalCost;
        }

        public long getLoyaltyPointsBalance() {
            return loyaltyPointsBalance;
        }

        public void setLoyaltyPointsBalance(long loyaltyPointsBalance) {
            this.loyaltyPointsBalance = loyaltyPointsBalance;
        }
    }

    private CalculateOutcomeReturnValue calculateOutcome(PurchaseModel purchaseModel) {
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

        BigDecimal totalCost = BigDecimal.ZERO;
        long freeBooksTotal = 0;
        long loyaltyPointsAwarded = 0;
        for (PurchaseItemModel item: purchaseModel.getPurchaseItems()) {
            BookEntity bookEntity = bookRepository.findByIsbn(item.getIsbn());

            if (!isPurchaseInStock(item, bookEntity.getInventory())) {
                logger.info("There is insufficient stock to meet this order");
            }

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

                totalCost = totalCost.add(bookEntity.getBasePrice()
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

        logger.info("totalCost: {} lpcredit: {} lpdebit: {}", String.valueOf(totalCost), String.valueOf(loyaltyPointsCredit), String.valueOf(loyaltyPointsDebit));

        CalculateOutcomeReturnValue calculateOutcomeReturnValue = new CalculateOutcomeReturnValue();
        calculateOutcomeReturnValue.setTotalCost(totalCost);
        calculateOutcomeReturnValue.setLoyaltyPointsBalance(loyaltyPointsCredit - loyaltyPointsDebit);
        return calculateOutcomeReturnValue;
    }

    public String getPrice(PurchaseModel purchaseModel) {
        CalculateOutcomeReturnValue calculateOutcomeReturnValue = calculateOutcome(purchaseModel);
        return String.format("Total cost: %s, Loyalty Point balance: %s",
                String.valueOf(calculateOutcomeReturnValue.getTotalCost()),
                String.valueOf(calculateOutcomeReturnValue.getLoyaltyPointsBalance()));
    }
}
