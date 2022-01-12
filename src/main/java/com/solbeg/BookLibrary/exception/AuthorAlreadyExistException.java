package com.solbeg.BookLibrary.exception;

public class AuthorAlreadyExistException extends RuntimeException {
    public AuthorAlreadyExistException(String firstName, String lastName) {
        super(String.format("Author with full name [%s %s] already exists", firstName, lastName));
    }
}
