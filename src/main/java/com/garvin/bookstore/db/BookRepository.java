package com.garvin.bookstore.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
    BookEntity findByIsbn(long isbn);

    @Query(value = " select book_id from books b where b.isbn = ?",
            nativeQuery = true)
    Long findBook_idByIsbn(long isbn);

    @Transactional
    @Modifying
    @Query(value = "update books b set b.title = ?, b.author = ?, b.base_price = ? where b.isbn = ?",
            nativeQuery = true)
    int updateBookByIsbn(String title, String author, BigDecimal basePrice, Long isbn);

    @Transactional
    Long deleteByIsbn(long isbn);
}
