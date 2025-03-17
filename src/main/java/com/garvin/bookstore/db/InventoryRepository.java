package com.garvin.bookstore.db;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, InventoryEntityPK> {

    @Transactional
    @Modifying
    @Query(value = "update inventory i set i.stock = i.stock - ? where i.book_id = ? and i.type = ?",
            nativeQuery = true)
    int updateInventoryDecrementStock(Long quantity, Long book_id, String type);

    @Query(value = "select count(*) from inventory i, books b where b.isbn = ? and i.book_id = b.book_id",
            nativeQuery = true)
    Long countByIsbn(Long isbn);

    Optional<InventoryEntity> findById(InventoryEntityPK inventoryEntityPK);

}