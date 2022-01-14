package com.solbeg.BookLibrary.service.impl;

import com.solbeg.BookLibrary.dto.AuthorRequestDto;
import com.solbeg.BookLibrary.dto.AuthorResponseDto;
import com.solbeg.BookLibrary.exception.AuthorAlreadyExistException;
import com.solbeg.BookLibrary.exception.AuthorNotFoundException;
import com.solbeg.BookLibrary.mapper.AuthorMapper;
import com.solbeg.BookLibrary.model.Author;
import com.solbeg.BookLibrary.repository.AuthorRepository;
import com.solbeg.BookLibrary.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @Override
    public List<AuthorResponseDto> findAllAuthors() {
        return authorRepository.findAll().stream()
                .map(authorMapper::convertAuthorToAuthorResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public AuthorResponseDto findAuthorById(String id) {
        Author author = findAuthorOrThrowException(id);
        return authorMapper.convertAuthorToAuthorResponseDto(author);
    }

    @Override
    public AuthorResponseDto createAuthor(AuthorRequestDto authorRequestDto) {
        Author author = authorRepository.findByFirstNameAndLastName(authorRequestDto.getFirstName(), authorRequestDto.getLastName()).orElse(null);
        if (author == null) {
            author = authorMapper.convertAuthorRequestDtoToAuthor(authorRequestDto);
            authorRepository.save(author);
        }
        return authorMapper.convertAuthorToAuthorResponseDto(author);
    }

    @Override
    public AuthorResponseDto updateAuthor(String id, AuthorRequestDto authorRequestDto) {
        Author author = authorRepository.findByFirstNameAndLastName(authorRequestDto.getFirstName(), authorRequestDto.getLastName()).orElse(null);
        if (author != null) {
            throw new AuthorAlreadyExistException(authorRequestDto.getFirstName(), authorRequestDto.getLastName());
        }
        findAuthorOrThrowException(id);
        author = authorMapper.convertAuthorRequestDtoToAuthor(authorRequestDto);
        author.setId(id);
        authorRepository.save(author);
        return authorMapper.convertAuthorToAuthorResponseDto(author);
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
