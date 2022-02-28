package com.solbeg.BookLibrary.exception;

public class OrderNotFoundByIdException extends RuntimeException {
    public OrderNotFoundByIdException(String id) {
        super(String.format("Order with id [%s] not found", id));
    }
}
