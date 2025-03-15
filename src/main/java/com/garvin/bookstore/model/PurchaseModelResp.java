package com.garvin.bookstore.model;

import java.math.BigDecimal;
import java.util.List;

public class PurchaseModelResp {
    private String userId;
    private BigDecimal totalCost;
    private long loyaltyPointsBalance;
    private boolean canCompleteTransaction;
    private String status;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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
