package com.solbeg.BookLibrary.exception;

public class AuthorNotFoundByIdException extends RuntimeException {
    public AuthorNotFoundByIdException(String id) {
        super(String.format("Author with id [%s] not found", id));
    }
}
