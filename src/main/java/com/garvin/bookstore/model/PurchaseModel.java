package com.garvin.bookstore.model;

import java.util.List;

public class PurchaseModel {
    private String userId;
    private List<PurchaseItemModel> purchaseItems;
    private List<PurchaseItemModel> freeItems;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
