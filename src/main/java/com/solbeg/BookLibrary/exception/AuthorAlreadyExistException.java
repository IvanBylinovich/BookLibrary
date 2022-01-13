package com.solbeg.BookLibrary.exception;

public class AuthorAlreadyExistException extends RuntimeException {
    public AuthorAlreadyExistException(String lastName, String firstName) {
        super(String.format("Author with full name [%s %s] already exist ", lastName, firstName));
    }
}
