package com.solbeg.BookLibrary.service.impl;

import com.solbeg.BookLibrary.dto.BookRequestDto;
import com.solbeg.BookLibrary.dto.BookResponseDto;
import com.solbeg.BookLibrary.exception.BookAlreadyExistByTitleAndAuthorException;
import com.solbeg.BookLibrary.exception.BookNotFoundByIdException;
import com.solbeg.BookLibrary.mapper.BookMapper;
import com.solbeg.BookLibrary.model.entity.Author;
import com.solbeg.BookLibrary.model.entity.Book;
import com.solbeg.BookLibrary.repository.BookRepository;
import com.solbeg.BookLibrary.service.AuthorService;
import com.solbeg.BookLibrary.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final AuthorService authorService;

    @Override
    public List<BookResponseDto> findAllBooks() {
        return bookRepository.findAll().stream()
                .map(bookMapper::convertBookToBookResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookResponseDto findBookById(String id) {
        return bookMapper.convertBookToBookResponseDto(findBookOrThrowException(id));
    }

    @Override
    public BookResponseDto createBook(BookRequestDto bookRequestDto) {
        Author author = authorService.getExistingAuthorOrCreateAuthor(bookRequestDto.getAuthor());
        Book book = bookRepository.findBookByTitleAndAuthor(bookRequestDto.getTitle(), author).orElse(null);
        if (book == null) {
            book = bookMapper.convertBookRequestDtoToBook(bookRequestDto);
            book.setAuthor(author);
            book.setCreatedAt(ZonedDateTime.now());
            book.setUpdatedAt(ZonedDateTime.now());
            bookRepository.save(book);
        }
        return bookMapper.convertBookToBookResponseDto(book);
    }

    @Override
    public BookResponseDto updateBook(String id, BookRequestDto bookRequestDto) {
        Book existingBook = findBookOrThrowException(id);
        Author author = authorService.getExistingAuthorOrCreateAuthor(bookRequestDto.getAuthor());
        Book book = bookRepository.findBookByTitleAndAuthor(bookRequestDto.getTitle(), author).orElse(null);
        if (book != null && !id.equals(book.getId())) {
            throw new BookAlreadyExistByTitleAndAuthorException(book.getTitle(), author, book.getId());
        }
        book = bookMapper.convertBookRequestDtoToBook(bookRequestDto);
        book.setId(existingBook.getId());
        book.setAuthor(author);
        book.setUpdatedAt(ZonedDateTime.now());
        book.setCreatedAt(existingBook.getCreatedAt());
        bookRepository.save(book);
        return bookMapper.convertBookToBookResponseDto(book);
    }

    @Override
    public void deleteBook(String id) {
        Book book = findBookOrThrowException(id);
        bookRepository.delete(book);
    }

    private Book findBookOrThrowException(String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundByIdException(id));
    }
}
