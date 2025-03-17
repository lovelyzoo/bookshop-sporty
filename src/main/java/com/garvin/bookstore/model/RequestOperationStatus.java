package com.garvin.bookstore.model;

public enum RequestOperationStatus {
    ISBN_NOT_IN_DB,
    ISBN_ALREADY_IN_DB,
    ISBN_NOT_IN_REQ,
    TYPE_ALREADY_IN_DB,
    TYPE_NOT_IN_REQ,
    TYPE_NOT_CONFIGURED,
    BOOK_HAS_INVENTORY_ENTRIES,
    DB_ERROR,
    SUCCESS
}
