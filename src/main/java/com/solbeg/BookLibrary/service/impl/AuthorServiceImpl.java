package com.solbeg.BookLibrary.service.impl;

import com.solbeg.BookLibrary.dto.AuthorRequestDto;
import com.solbeg.BookLibrary.dto.AuthorResponseDto;
import com.solbeg.BookLibrary.model.Author;
import com.solbeg.BookLibrary.exception.AuthorAlreadyExistException;
import com.solbeg.BookLibrary.exception.AuthorNotFoundException;
import com.solbeg.BookLibrary.repository.AuthorRepository;
import com.solbeg.BookLibrary.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public List<AuthorResponseDto> findAllAuthors() {
        List<Author> authors = authorRepository.findAll();

        List<AuthorResponseDto> authorsResponseDto = new ArrayList<>();
        for (Author author : authors) {
            authorsResponseDto.add(new AuthorResponseDto(author.getId(), author.getFirstName(), author.getLastName()));
        }
        return authorsResponseDto;
    }

    @Override
    public AuthorResponseDto findAuthorById(String id) {
        Author author = findAuthorOrThrowException(id);
        return new AuthorResponseDto(author.getId(), author.getFirstName(), author.getLastName());
    }

    @Override
    public AuthorResponseDto createAuthor(AuthorRequestDto authorRequestDto) {
        if (authorRepository.existsByFirstNameAndLastName(authorRequestDto.getFirstName(), authorRequestDto.getLastName())) {
            throw new AuthorAlreadyExistException(authorRequestDto.getFirstName(), authorRequestDto.getLastName());
        }
        Author author = new Author(authorRequestDto.getFirstName(), authorRequestDto.getLastName());
        authorRepository.save(author);
        return new AuthorResponseDto(author.getId(), author.getFirstName(), author.getLastName());
    }

    @Override
    public AuthorResponseDto updateAuthor(String id, AuthorRequestDto authorRequestDto) {
        Author author = findAuthorOrThrowException(id);
        author.setFirstName(authorRequestDto.getFirstName());
        author.setLastName(authorRequestDto.getLastName());
        authorRepository.save(author);
        return new AuthorResponseDto(author.getId(), author.getFirstName(), author.getLastName());
    }

    @Override
    public void deleteAuthor(String id) {
        Author author = findAuthorOrThrowException(id);
        authorRepository.delete(author);
    }

    private Author findAuthorOrThrowException(String id) {
        return authorRepository.findAuthorById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }

}
