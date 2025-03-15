package com.garvin.bookstore.db;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    CustomerEntity findByUserId(String userId);

    @Transactional
    default CustomerEntity updateOrInsert(CustomerEntity customerEntity) {
        return save(customerEntity);
    }
}