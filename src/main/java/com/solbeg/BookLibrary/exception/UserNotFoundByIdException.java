package com.solbeg.BookLibrary.exception;

public class UserNotFoundByIdException extends RuntimeException {
    public UserNotFoundByIdException(String id) {
        super(String.format("User with id [%s] not found", id));
    }
}
