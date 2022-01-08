package com.solbeg.BookLibrary.service;

import com.solbeg.BookLibrary.dto.authorDto.AuthorSavingDTO;
import com.solbeg.BookLibrary.dto.authorDto.AuthorUpdateDTO;
import com.solbeg.BookLibrary.entity.Author;

import java.util.List;

public interface AuthorService {
    Author findAuthorById(long id);

    Author createAuthor(AuthorSavingDTO dto);

    void deleteAuthor(long id);

    List<Author> getAllAuthor();

    void updateAuthor(AuthorUpdateDTO authorUpdateDTO);
}
