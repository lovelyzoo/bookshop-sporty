package com.garvin.bookstore.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
    BookEntity findByIsbn(long isbn);

    @Query(value = " select book_id from books b where b.isbn = ?",
            nativeQuery = true)
    Long findBook_idByIsbn(long isbn);
}
