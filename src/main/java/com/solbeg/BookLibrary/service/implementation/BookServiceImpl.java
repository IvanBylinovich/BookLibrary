package com.solbeg.BookLibrary.service.implementation;

import com.solbeg.BookLibrary.dto.bookDto.BookSavingDTO;
import com.solbeg.BookLibrary.dto.bookDto.BookUpdateDTO;
import com.solbeg.BookLibrary.entity.Author;
import com.solbeg.BookLibrary.entity.Book;
import com.solbeg.BookLibrary.repository.BookRepository;
import com.solbeg.BookLibrary.service.AuthorService;
import com.solbeg.BookLibrary.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    final BookRepository bookRepository;
    final AuthorService authorService;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, AuthorServiceImpl authorService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
    }

    @Override
    public Book createBook(BookSavingDTO dto) {
        Book book = new Book(dto.getTitle(), dto.getImageUrl(), dto.getPrice(), dto.getTag());
        book.setAuthor(authorService.findAuthorById(dto.getAuthorId()));
        if (bookRepository.existsBookByTitleAndAuthor(book.getTitle(), book.getAuthor())) {
            throw new RuntimeException("Such book already exists");
        }

        book.setCreatedAt(ZonedDateTime.now());
        book.setUpdatedAt(ZonedDateTime.now());
        bookRepository.save(book);
        return book;
    }

    @Override
    public void deleteBook(long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
        } else {
            throw new NoSuchElementException("There is no such author with id " + id);
        }
    }

    @Override
    public Book getBookById(long id) {
        return returnBookByIdOreIfNonExistThrowNoSuchElementException(id);
    }

    @Override
    public List<Book> getAllBook() {
        return bookRepository.findAll();
    }

    @Override
    public void updateBook(BookUpdateDTO dto) {
        Author author = authorService.findAuthorById(dto.getAuthorId());
        Book book = returnBookByIdOreIfNonExistThrowNoSuchElementException(dto.getBookId());

        book.setTitle(dto.getTitle());
        book.setImageUrl(dto.getImageUrl());
        book.setPrice(dto.getPrice());
        book.setTag(dto.getTag());
        book.setAuthor(author);
        book.setUpdatedAt(ZonedDateTime.now());
        bookRepository.save(book);
    }

    private Book returnBookByIdOreIfNonExistThrowNoSuchElementException(long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        bookOptional.orElseThrow(() -> new NoSuchElementException("No such book with id " + id));
        return bookOptional.get();
    }
}
