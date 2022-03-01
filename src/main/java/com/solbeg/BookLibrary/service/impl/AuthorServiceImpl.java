package com.solbeg.BookLibrary.service.impl;

import com.solbeg.BookLibrary.dto.AuthorRequestDto;
import com.solbeg.BookLibrary.dto.AuthorResponseDto;
import com.solbeg.BookLibrary.exception.AuthorAlreadyExistByFirstNameAndLastNameException;
import com.solbeg.BookLibrary.exception.AuthorNotFoundByIdException;
import com.solbeg.BookLibrary.mapper.AuthorMapper;
import com.solbeg.BookLibrary.model.entity.Author;
import com.solbeg.BookLibrary.repository.AuthorRepository;
import com.solbeg.BookLibrary.repository.BookRepository;
import com.solbeg.BookLibrary.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public List<AuthorResponseDto> findAllAuthors() {
        return authorRepository.findAll().stream()
                .map(authorMapper::convertAuthorToAuthorResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public AuthorResponseDto findAuthorById(String id) {
        return authorMapper.convertAuthorToAuthorResponseDto(findAuthorOrThrowException(id));
    }

    @Override
    public AuthorResponseDto createAuthor(AuthorRequestDto authorRequestDto) {
        Author author = getExistingAuthorOrCreateAuthor(authorRequestDto);
        return authorMapper.convertAuthorToAuthorResponseDto(author);
    }

    @Override
    public AuthorResponseDto updateAuthor(String id, AuthorRequestDto authorRequestDto) {
        findAuthorOrThrowException(id);
        Author author = authorRepository.findAuthorByFirstNameAndLastName(authorRequestDto.getFirstName(), authorRequestDto.getLastName()).orElse(null);
        if (author != null) {
            throw new AuthorAlreadyExistByFirstNameAndLastNameException(author.getFirstName(), author.getLastName(), author.getId());
        }
        author = authorMapper.convertAuthorRequestDtoToAuthor(authorRequestDto);
        author.setId(id);
        authorRepository.save(author);
        return authorMapper.convertAuthorToAuthorResponseDto(author);
    }

    @Transactional
    @Override
    public void deleteAuthor(String id) {
        Author author = findAuthorOrThrowException(id);
        bookRepository.findAllByAuthor(author).ifPresent(bookRepository::deleteAll);
        authorRepository.delete(author);
    }

    @Override
    public Author getExistingAuthorOrCreateAuthor(AuthorRequestDto authorRequestDto) {
        Author author = authorRepository.findAuthorByFirstNameAndLastName(authorRequestDto.getFirstName(), authorRequestDto.getLastName()).orElse(null);
        if (author == null) {
            author = authorMapper.convertAuthorRequestDtoToAuthor(authorRequestDto);
            authorRepository.save(author);
        }
        return author;
    }

    private Author findAuthorOrThrowException(String id) {
        return authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundByIdException(id));
    }
}
