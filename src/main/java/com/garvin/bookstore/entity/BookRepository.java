package com.garvin.bookstore.entity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<BookEntity, Long> {
    BookEntity findByIsbn(long isbn);
}
