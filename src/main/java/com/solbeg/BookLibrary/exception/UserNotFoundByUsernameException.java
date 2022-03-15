package com.solbeg.BookLibrary.exception;

public class UserNotFoundByUsernameException extends RuntimeException {
    public UserNotFoundByUsernameException(String username) {
        super(String.format("User with username [%s] not found", username));
    }
}
