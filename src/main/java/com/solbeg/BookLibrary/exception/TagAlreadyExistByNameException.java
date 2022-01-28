package com.solbeg.BookLibrary.exception;

public class TagAlreadyExistByNameException extends RuntimeException {
    public TagAlreadyExistByNameException(String name, String id) {
        super(String.format("Tag with name [%s] already exist. This tag has id [%s]", name, id));
    }
}
