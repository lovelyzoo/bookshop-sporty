package com.garvin.bookstore.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.io.Serial;
import java.io.Serializable;

@Entity(name="customer")
public class CustomerEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 6373464312394303871L;

    @Id
    @GeneratedValue
    private long customer_id;

    @Column(nullable = false)
    private String userId;

    private long loyaltyPoints;

    public long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(long customer_id) {
        this.customer_id = customer_id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(long loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }
}
