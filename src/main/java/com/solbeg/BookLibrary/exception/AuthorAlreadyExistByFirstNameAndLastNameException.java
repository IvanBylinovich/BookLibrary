package com.solbeg.BookLibrary.exception;

public class AuthorAlreadyExistByFirstNameAndLastNameException extends RuntimeException {
    public AuthorAlreadyExistByFirstNameAndLastNameException(String firstName, String lastName, String id) {
        super(String.format(
                "Author with full name [%s %s] already exists. This author has id [%s]",
                firstName,
                lastName,
                id)
        );
    }
}
