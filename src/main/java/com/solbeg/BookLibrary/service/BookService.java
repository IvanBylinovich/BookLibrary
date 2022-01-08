package com.solbeg.BookLibrary.service;

import com.solbeg.BookLibrary.dto.bookDto.BookSavingDTO;
import com.solbeg.BookLibrary.dto.bookDto.BookUpdateDTO;
import com.solbeg.BookLibrary.entity.Book;

import java.util.List;

public interface BookService {
    Book createBook(BookSavingDTO dtoBookSaving);

    void deleteBook(long id);

    Book getBookById(long id);

    List<Book> getAllBook();

    void updateBook(BookUpdateDTO dtoBookUpdate);

}
