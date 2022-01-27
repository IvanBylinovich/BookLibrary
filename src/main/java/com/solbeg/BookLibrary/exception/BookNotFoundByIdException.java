package com.solbeg.BookLibrary.exception;

public class BookNotFoundByIdException extends RuntimeException {
    public BookNotFoundByIdException(String id) {
        super(String.format("Book with id [%s] not found", id));
    }
}
