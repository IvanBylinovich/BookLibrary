package com.solbeg.BookLibrary.exception;

public class TagNotFoundByIdException extends RuntimeException {
    public TagNotFoundByIdException(String id) {
        super(String.format("Tag with id [%s] not found", id));
    }
}
