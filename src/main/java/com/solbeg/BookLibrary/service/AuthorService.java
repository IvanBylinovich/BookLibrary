package com.solbeg.BookLibrary.service;

import com.solbeg.BookLibrary.dto.AuthorRequestDto;
import com.solbeg.BookLibrary.dto.AuthorResponseDto;

import java.util.List;

public interface AuthorService {

    List<AuthorResponseDto> findAllAuthors();

    AuthorResponseDto findAuthorById(String id);

    AuthorResponseDto createAuthor(AuthorRequestDto authorRequestDto);

    AuthorResponseDto updateAuthor(String id, AuthorRequestDto authorRequestDto);

    void deleteAuthor(String id);

}
