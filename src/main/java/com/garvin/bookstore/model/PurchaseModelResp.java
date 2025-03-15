package com.garvin.bookstore.model;

import java.math.BigDecimal;
import java.util.List;

public class PurchaseModelResp {
    private String userId;
    private BigDecimal totalCost;
    private long loyaltyPointsBalance;
    private List<PurchaseItemModel> purchaseItems;
    private List<PurchaseItemModel> freeItems;

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

    public List<PurchaseItemModel> getPurchaseItems() {
        return purchaseItems;
    }

    public void setPurchaseItems(List<PurchaseItemModel> purchaseItems) {
        this.purchaseItems = purchaseItems;
    }

    public List<PurchaseItemModel> getFreeItems() {
        return freeItems;
    }

    public void setFreeItems(List<PurchaseItemModel> freeItems) {
        this.freeItems = freeItems;
    }
}
