package com.garvin.bookstore.service;

import com.garvin.bookstore.db.*;
import com.garvin.bookstore.model.PurchaseItemModel;
import com.garvin.bookstore.model.PurchaseModel;
import com.garvin.bookstore.model.PurchaseModelResp;
import com.garvin.bookstore.properties.BookTypeProperties;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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

    private final HashMap<String, BigDecimal> priceModifier = new HashMap<String, BigDecimal>();
    private final HashMap<String, BigDecimal> bundleModifier = new HashMap<String, BigDecimal>();

    @PostConstruct
    private void postConstruct() {
        // Intialise modifier maps from config
        for (BookTypeProperties.BookType bookType : bookTypeProperties.getBooktypes()) {
            priceModifier.put(bookType.getType(), bookType.getPricemodifier());
            bundleModifier.put(bookType.getType(), bookType.getBundlemodifier());
        }
    }

    private static boolean isInStock(PurchaseItemModel purchaseItemModel, Set<InventoryEntity> inventoryEntities) {
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

    static class CalculateOutcomeReturnValue {
        BigDecimal totalCost;
        long loyaltyPointsBalance;
        boolean canCompleteTransaction;
        String status;

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

        public boolean isCanCompleteTransaction() {
            return canCompleteTransaction;
        }

        public void setCanCompleteTransaction(boolean canCompleteTransaction) {
            this.canCompleteTransaction = canCompleteTransaction;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    private CalculateOutcomeReturnValue calculateOutcome(PurchaseModel purchaseModel) {
        // Initialise return value
        CalculateOutcomeReturnValue calculateOutcomeReturnValue = new CalculateOutcomeReturnValue();
        CustomerEntity customerEntity = customerRepository.findByUserId(purchaseModel.getUserId());
        long currentLoyaltyPoints = customerEntity.getLoyaltyPoints();
        calculateOutcomeReturnValue.setLoyaltyPointsBalance(currentLoyaltyPoints);
        calculateOutcomeReturnValue.setCanCompleteTransaction(false);

        // Confirm uniqueness of purchase items
        HashMap<String, Long> purchaseItems = new HashMap<String, Long>();
        for (PurchaseItemModel item: purchaseModel.getPurchaseItems()) {
            String key = String.format("%s %s", item.getIsbn(), item.getType());
            Long previous = purchaseItems.put(key, item.getQuantity());
            if (previous != null) {
                calculateOutcomeReturnValue.setStatus(String.format("%s occurs more than once in purchaseItems", key));
                return calculateOutcomeReturnValue;
            }
        }

        // Confirm uniqueness of free items
        HashMap<String, Long> freeItems = new HashMap<String, Long>();
        for (PurchaseItemModel item: purchaseModel.getFreeItems()) {
            String key = String.format("%s%s", item.getIsbn(), item.getType());
            Long previous = freeItems.put(key, item.getQuantity());
            if (previous != null) {
                calculateOutcomeReturnValue.setStatus(String.format("%s occurs more than once in freeItems", key));
                return calculateOutcomeReturnValue;
            }
        }

        // Determine the bundle size
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

            if (!isInStock(item, bookEntity.getInventory())) {
                calculateOutcomeReturnValue.setStatus(String.format("Insufficient stock of %s %s to meet purchase", item.getIsbn(), item.getType()));
                return calculateOutcomeReturnValue;
            }

            Long purchaseQuantity = item.getQuantity();
            Long freeQuantity = freeItems.getOrDefault(String.format("%s%s", item.getIsbn(), item.getType()), 0L);
            // TODO: is the item a 'free' one?
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
                calculateOutcomeReturnValue.setStatus(String.format("Insufficient stock of %s %s to meet loyalty points redemption", item.getIsbn(), item.getType()));
                return calculateOutcomeReturnValue;
            }
        }

        // Loyalty Points calculation
        long loyaltyPointsCredit = currentLoyaltyPoints + loyaltyPointsAwarded;
        long loyaltyPointsDebit = freeBooksTotal * 10L;
        if (loyaltyPointsDebit > loyaltyPointsCredit) {
            calculateOutcomeReturnValue.setStatus(String.format("Insufficient loyalty points"));
            return calculateOutcomeReturnValue;
        }

        logger.debug("totalCost: {} lpcredit: {} lpdebit: {}", String.valueOf(totalCost), String.valueOf(loyaltyPointsCredit), String.valueOf(loyaltyPointsDebit));

        // Setup return for success
        calculateOutcomeReturnValue.setTotalCost(totalCost);
        calculateOutcomeReturnValue.setLoyaltyPointsBalance(loyaltyPointsCredit - loyaltyPointsDebit);
        calculateOutcomeReturnValue.setCanCompleteTransaction(true);
        calculateOutcomeReturnValue.setStatus(String.format("OK"));
        return calculateOutcomeReturnValue;
    }

    public PurchaseModelResp getOutcome(PurchaseModel purchaseModel) {
        PurchaseModelResp returnValue = new PurchaseModelResp();
        CalculateOutcomeReturnValue calculateOutcomeReturnValue = calculateOutcome(purchaseModel);

        BeanUtils.copyProperties(purchaseModel, returnValue);
        BeanUtils.copyProperties(calculateOutcomeReturnValue, returnValue);

        return returnValue;
    }
}
