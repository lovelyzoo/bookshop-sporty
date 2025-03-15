package com.garvin.bookstore.service;

import com.garvin.bookstore.db.*;
import com.garvin.bookstore.model.PurchaseItemModel;
import com.garvin.bookstore.model.PurchaseModel;
import com.garvin.bookstore.model.PurchaseModelResp;
import com.garvin.bookstore.properties.BookTypeProperties;
import com.garvin.bookstore.utils.PurchaseStockTracker;
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
        long loyaltyPointsBalance = customerEntity.getLoyaltyPoints();
        calculateOutcomeReturnValue.setLoyaltyPointsBalance(loyaltyPointsBalance);

        calculateOutcomeReturnValue.setCanCompleteTransaction(false);

        //test
        PurchaseStockTracker purchaseStockTracker = new PurchaseStockTracker(bookRepository);

        // Determine the bundle size
        long bundleSize = purchaseModel.getPurchaseItems().stream()
                .map(PurchaseItemModel::getQuantity)
                .mapToLong(Long::longValue).sum();

        HashMap<String, BigDecimal> modifier = priceModifier;
        if (bundleSize >= 3) {
            modifier = bundleModifier;
        }
        //bundleModifier.get(item.getType());

        BigDecimal totalCost = BigDecimal.ZERO;

        // Handle the items that are being paid for
        HashMap<String, Long> purchaseItems = new HashMap<String, Long>();
        for (PurchaseItemModel item: purchaseModel.getPurchaseItems()) {
            long isbn = item.getIsbn();
            String type = item.getType();
            long quantity = item.getQuantity();

            BookEntity bookEntity = bookRepository.findByIsbn(isbn);
            if (!purchaseStockTracker.incrementItem(isbn, type, quantity)) {
                calculateOutcomeReturnValue.setStatus(String.format("Insufficient stock of %s %s", isbn, type));
                return calculateOutcomeReturnValue;
            }

            totalCost = totalCost.add(bookEntity.getBasePrice()
                    .multiply(modifier.get(type))
                    .multiply(BigDecimal.valueOf(quantity))
            );
            loyaltyPointsBalance = loyaltyPointsBalance + quantity;
        }

        // Handle the free items
        HashMap<String, Long> freeItems = new HashMap<String, Long>();
        for (PurchaseItemModel item: purchaseModel.getFreeItems()) {
            long isbn = item.getIsbn();
            String type = item.getType();
            long quantity = item.getQuantity();

            if (type.equals("N")) {
                calculateOutcomeReturnValue.setStatus(String.format("%s %s is not eligible for a loyalty point claim", isbn, type));
                return calculateOutcomeReturnValue;
            }

            BookEntity bookEntity = bookRepository.findByIsbn(isbn);
            if (!purchaseStockTracker.incrementItem(isbn, type, quantity)) {
                calculateOutcomeReturnValue.setStatus(String.format("Insufficient stock of %s %s", isbn, type));
                return calculateOutcomeReturnValue;
            }

            loyaltyPointsBalance = loyaltyPointsBalance - 10;
            if (loyaltyPointsBalance < 0) {
                calculateOutcomeReturnValue.setStatus(String.format("Insufficient loyalty points"));
                return calculateOutcomeReturnValue;
            }
        }

        // Setup return for success
        calculateOutcomeReturnValue.setTotalCost(totalCost);
        calculateOutcomeReturnValue.setLoyaltyPointsBalance(loyaltyPointsBalance);
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
