package com.garvin.bookstore.model;

public enum RequestOperationStatus {
    ISBN_NOT_IN_DB,
    TYPE_NOT_IN_DB,
    TYPE_ALREADY_IN_DB,
    TYPE_NOT_IN_REQ,
    TYPE_NOT_CONFIGURED,
    SUCCESS
}
