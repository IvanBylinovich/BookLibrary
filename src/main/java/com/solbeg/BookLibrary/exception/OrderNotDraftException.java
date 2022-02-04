package com.solbeg.BookLibrary.exception;

public class OrderNotDraftException extends RuntimeException {
    public OrderNotDraftException(String id) {
        super(String.format("Order with id [%s] can not be deleted or changed because it is not DRAFT.", id));
    }
}
