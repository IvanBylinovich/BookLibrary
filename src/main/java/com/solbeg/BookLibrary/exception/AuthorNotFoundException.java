package com.solbeg.BookLibrary.exception;

public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(String id) {
        super("Author is not found for id: " + id);
    }
}
