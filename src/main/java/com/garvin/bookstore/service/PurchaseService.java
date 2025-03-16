package com.garvin.bookstore.service;

import com.garvin.bookstore.db.*;
import com.garvin.bookstore.model.PurchaseItemModel;
import com.garvin.bookstore.model.PurchaseModel;
import com.garvin.bookstore.model.PurchaseModelResp;
import com.garvin.bookstore.properties.BookTypeProperties;
import com.garvin.bookstore.utils.BookStock;
import com.garvin.bookstore.utils.PurchaseStockTracker;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

@Service
public class PurchaseService {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseService.class);

    @Autowired
    BookRepository bookRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    InventoryRepository inventoryRepository;

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
        private BigDecimal totalCost;
        private long customer_id;
        private long loyaltyPointsAdjustment;
        private PurchaseStockTracker purchaseStockTracker;
        private boolean canComplete;
        private String status;

        public BigDecimal getTotalCost() {
            return totalCost;
        }

        public void setTotalCost(BigDecimal totalCost) {
            this.totalCost = totalCost;
        }

        public long getCustomer_id() {
            return customer_id;
        }

        public void setCustomer_id(long customer_id) {
            this.customer_id = customer_id;
        }

        public long getLoyaltyPointsAdjustment() {
            return loyaltyPointsAdjustment;
        }

        public void setLoyaltyPointsAdjustment(long loyaltyPointsAdjustment) {
            this.loyaltyPointsAdjustment = loyaltyPointsAdjustment;
        }

        public PurchaseStockTracker getPurchaseStockTracker() {
            return purchaseStockTracker;
        }

        public void setPurchaseStockTracker(PurchaseStockTracker purchaseStockTracker) {
            this.purchaseStockTracker = purchaseStockTracker;
        }

        public boolean isCanComplete() {
            return canComplete;
        }

        public void setCanComplete(boolean canComplete) {
            this.canComplete = canComplete;
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
        calculateOutcomeReturnValue.setCanComplete(false);

        CustomerEntity customerEntity = customerRepository.findByUserId(purchaseModel.getUserId());
        calculateOutcomeReturnValue.setCustomer_id(customerEntity.getCustomer_id());

        long currentLoyaltyPoints = customerEntity.getLoyaltyPoints();
        long loyaltyPointsAdjustment = 0L;

        PurchaseStockTracker purchaseStockTracker = new PurchaseStockTracker(bookRepository);

        // Determine the price modifier
        long bundleSize = purchaseModel.getPurchaseItems().stream()
                .map(PurchaseItemModel::getQuantity)
                .mapToLong(Long::longValue).sum();

        HashMap<String, BigDecimal> modifier = priceModifier;
        if (bundleSize >= 3) {
            modifier = bundleModifier;
        }

        BigDecimal totalCost = BigDecimal.ZERO;

        // Handle the items that are being paid for
        HashMap<String, Long> purchaseItems = new HashMap<String, Long>();
        for (PurchaseItemModel item: purchaseModel.getPurchaseItems()) {
            long isbn = item.getIsbn();
            String type = item.getType();
            long quantity = item.getQuantity();

            BookEntity bookEntity = bookRepository.findByIsbn(isbn);
            if (!purchaseStockTracker.incrementItem(bookEntity, type, quantity)) {
                calculateOutcomeReturnValue.setStatus(String.format("Insufficient stock of %s %s", isbn, type));
                return calculateOutcomeReturnValue;
            }

            totalCost = totalCost.add(bookEntity.getBasePrice()
                    .multiply(modifier.get(type))
                    .multiply(BigDecimal.valueOf(quantity))
            );
            loyaltyPointsAdjustment = loyaltyPointsAdjustment + quantity;
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
            if (!purchaseStockTracker.incrementItem(bookEntity, type, quantity)) {
                calculateOutcomeReturnValue.setStatus(String.format("Insufficient stock of %s %s", isbn, type));
                return calculateOutcomeReturnValue;
            }

            loyaltyPointsAdjustment = loyaltyPointsAdjustment - 10;
            if (currentLoyaltyPoints + loyaltyPointsAdjustment < 0) {
                calculateOutcomeReturnValue.setStatus(String.format("Insufficient loyalty points"));
                return calculateOutcomeReturnValue;
            }
        }

        // Setup return for success
        totalCost = totalCost.setScale(2, RoundingMode.UP);
        calculateOutcomeReturnValue.setTotalCost(totalCost);
        calculateOutcomeReturnValue.setLoyaltyPointsAdjustment(loyaltyPointsAdjustment);
        calculateOutcomeReturnValue.setPurchaseStockTracker(purchaseStockTracker);
        calculateOutcomeReturnValue.setCanComplete(true);
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

    @Transactional
    private void updateDbWithPurchaseChanges(CalculateOutcomeReturnValue calculateOutcomeReturnValue) {
        PurchaseStockTracker purchaseStockTracker = calculateOutcomeReturnValue.getPurchaseStockTracker();
        for (BookStock bookStock : calculateOutcomeReturnValue.getPurchaseStockTracker().getBookStocks().values()) {
            inventoryRepository.updateInventoryDecrementStock(bookStock.getQuantity(), bookStock.getBook_id(), bookStock.getType());
        }
        customerRepository.updateCustomerIncrementLoyaltyPoints(calculateOutcomeReturnValue.getLoyaltyPointsAdjustment(),
                calculateOutcomeReturnValue.getCustomer_id());
    }

    public PurchaseModelResp makePurchase(PurchaseModel purchaseModel) {
        PurchaseModelResp returnValue = new PurchaseModelResp();
        CalculateOutcomeReturnValue calculateOutcomeReturnValue = calculateOutcome(purchaseModel);

        if (calculateOutcomeReturnValue.canComplete) {
            updateDbWithPurchaseChanges(calculateOutcomeReturnValue);
        }

        BeanUtils.copyProperties(purchaseModel, returnValue);
        BeanUtils.copyProperties(calculateOutcomeReturnValue, returnValue);

        return returnValue;
    }
}
