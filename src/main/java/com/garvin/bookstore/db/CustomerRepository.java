package com.garvin.bookstore.db;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    CustomerEntity findByUserId(String userId);

    @Transactional
    @Modifying
    @Query(value = "update customer c set c.loyalty_points = c.loyalty_points + ? where c.customer_id = ?",
            nativeQuery = true)
    int updateCustomerIncrementLoyaltyPoints(Long loyaltyPoints, Long customer_id);
}