package com.garvin.bookstore.db;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, InventoryEntityPK> {

    @Transactional
    @Modifying
    @Query(value = "update inventory i set i.stock = i.stock - ? where i.book_id = ? and i.type = ?",
            nativeQuery = true)
    int updateInventoryDecrementStock(Long quantity, Long book_id, String type);
}