package com.solbeg.BookLibrary.service;

import com.solbeg.BookLibrary.dto.BookRequestDto;
import com.solbeg.BookLibrary.dto.BookResponseDto;

import java.util.List;

public interface BookService {

    List<BookResponseDto> findAllBooks();

    BookResponseDto findBookById(String id);

    BookResponseDto createBook(BookRequestDto bookRequestDto);

    BookResponseDto updateBook(String id, BookRequestDto bookRequestDto);

    void deleteBook(String id);
}
