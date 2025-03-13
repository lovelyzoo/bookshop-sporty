package com.garvin.bookstore.model;

import java.util.List;

public class PurchaseModel {
    private String userId;
    private List<PurchaseItemModel> items;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<PurchaseItemModel> getItems() {
        return items;
    }

    public void setItems(List<PurchaseItemModel> items) {
        this.items = items;
    }
}
