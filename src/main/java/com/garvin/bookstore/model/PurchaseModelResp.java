package com.garvin.bookstore.model;

import java.math.BigDecimal;

public class PurchaseModelResp {
    private String userId;
    private BigDecimal totalCost;
    private long loyaltyPointsAdjustment;
    private boolean canComplete;
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

    public long getLoyaltyPointsAdjustment() {
        return loyaltyPointsAdjustment;
    }

    public void setLoyaltyPointsAdjustment(long loyaltyPointsAdjustment) {
        this.loyaltyPointsAdjustment = loyaltyPointsAdjustment;
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
