package com.solbeg.BookLibrary.service.implementation;

import com.solbeg.BookLibrary.dto.authorDto.AuthorSavingDTO;
import com.solbeg.BookLibrary.dto.authorDto.AuthorUpdateDTO;
import com.solbeg.BookLibrary.entity.Author;
import com.solbeg.BookLibrary.repository.AuthorRepository;
import com.solbeg.BookLibrary.service.AuthorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {

    final
    AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author createAuthor(AuthorSavingDTO dto) {
        if (!authorRepository.existsByFirstNameAndLastName(dto.getFirstName(), dto.getLastName())) {
            return authorRepository.save(new Author(dto.getFirstName(), dto.getLastName()));
        } else {
            throw new RuntimeException("Such author already exist");
        }
    }

    @Override
    public void deleteAuthor(long id) {
        Optional<Author> authorOptional = authorRepository.findAuthorById(id);
        authorOptional.orElseThrow(() -> new NoSuchElementException("No such author with id " + id));
        authorRepository.delete(authorOptional.get());
    }

    @Override
    public List<Author> getAllAuthor() {
        return authorRepository.findAll();
    }

    @Override
    public void updateAuthor(AuthorUpdateDTO dto) {
        Optional<Author> optionalAuthor = authorRepository.findAuthorById(dto.getId());
        optionalAuthor.orElseThrow(() -> new NoSuchElementException("No such author with id " + dto.getId()));
        Author author = optionalAuthor.get();
        author.setFirstName(dto.getFirstName());
        author.setLastName(dto.getLastName());
        authorRepository.save(author);
    }

    @Override
    public Author findAuthorById(long id) {
        Optional<Author> authorOptional = authorRepository.findAuthorById(id);
        authorOptional.orElseThrow(() -> new NoSuchElementException("There is no such author"));
        return authorOptional.get();
    }


}
