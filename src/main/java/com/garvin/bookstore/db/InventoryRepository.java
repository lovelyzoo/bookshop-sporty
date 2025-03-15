package com.garvin.bookstore.db;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, InventoryEntityPK> {
    @Transactional
    default InventoryEntity updateOrInsert(InventoryEntity inventoryEntity) {
        return save(inventoryEntity);
    }
}