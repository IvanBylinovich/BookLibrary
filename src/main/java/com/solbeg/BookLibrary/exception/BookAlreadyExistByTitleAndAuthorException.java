package com.solbeg.BookLibrary.exception;

import com.solbeg.BookLibrary.model.entity.Author;

public class BookAlreadyExistByTitleAndAuthorException extends RuntimeException {
    public BookAlreadyExistByTitleAndAuthorException(String title, Author author, String bookId) {
        super(String.format(
                "Book with title [%s] and author [%s %s] already exists. This book has id [%s]",
                title,
                author.getFirstName(),
                author.getLastName(),
                bookId)
        );
    }
}
