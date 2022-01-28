package com.solbeg.BookLibrary.exception;

public class TagNotFoundByNameException extends RuntimeException {
    public TagNotFoundByNameException(String name) {
        super(String.format("Tag with name [%s] not found", name));
    }
}
