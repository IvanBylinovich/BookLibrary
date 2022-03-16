package com.solbeg.BookLibrary.service.impl;

import com.solbeg.BookLibrary.dto.BookRequestDto;
import com.solbeg.BookLibrary.dto.BookResponseDto;
import com.solbeg.BookLibrary.exception.BookAlreadyExistByTitleAndAuthorException;
import com.solbeg.BookLibrary.exception.BookNotFoundByIdException;
import com.solbeg.BookLibrary.mapper.BookMapper;
import com.solbeg.BookLibrary.model.entity.Author;
import com.solbeg.BookLibrary.model.entity.Book;
import com.solbeg.BookLibrary.model.entity.Tag;
import com.solbeg.BookLibrary.repository.BookRepository;
import com.solbeg.BookLibrary.service.AuthorService;
import com.solbeg.BookLibrary.service.BookService;
import com.solbeg.BookLibrary.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final TagService tagService;
    private final BookMapper bookMapper;

    @Transactional(readOnly = true)
    @Override
    public List<BookResponseDto> findAllBooks() {
        return bookRepository.findAll().stream()
                .map(bookMapper::convertBookToBookResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public BookResponseDto findBookById(String id) {
        return bookMapper.convertBookToBookResponseDto(findBookOrThrowException(id));
    }

    @Transactional
    @Override
    public BookResponseDto createBook(BookRequestDto bookRequestDto) {
        Author author = authorService.getExistingAuthorOrCreateAuthor(bookRequestDto.getAuthor());
        Book book = bookRepository.findBookByTitleAndAuthor(bookRequestDto.getTitle(), author).orElse(null);
        if (book == null) {
            Set<Tag> tags = tagService.getExistingTagsOrThrowException(bookRequestDto.getTags());
            book = bookMapper.convertBookRequestDtoToBook(bookRequestDto);
            book.setAuthor(author);
            book.setTags(tags);
            book.setCreatedAt(ZonedDateTime.now());
            book.setUpdatedAt(ZonedDateTime.now());
            bookRepository.save(book);
        }
        return bookMapper.convertBookToBookResponseDto(book);
    }

    @Transactional
    @Override
    public BookResponseDto updateBook(String id, BookRequestDto bookRequestDto) {
        Book existingBook = findBookOrThrowException(id);
        Author author = authorService.getExistingAuthorOrCreateAuthor(bookRequestDto.getAuthor());
        Book book = bookRepository.findBookByTitleAndAuthor(bookRequestDto.getTitle(), author).orElse(null);
        if (book != null && !id.equals(book.getId())) {
            throw new BookAlreadyExistByTitleAndAuthorException(book.getTitle(), author, book.getId());
        }
        Set<Tag> tags = tagService.getExistingTagsOrThrowException(bookRequestDto.getTags());
        book = bookMapper.convertBookRequestDtoToBook(bookRequestDto);
        book.setId(existingBook.getId());
        book.setAuthor(author);
        book.setTags(tags);
        book.setCreatedAt(existingBook.getCreatedAt());
        book.setUpdatedAt(ZonedDateTime.now());
        bookRepository.save(book);
        return bookMapper.convertBookToBookResponseDto(book);
    }

    @Override
    public void deleteBook(String id) {
        Book book = findBookOrThrowException(id);
        bookRepository.delete(book);
    }

    public Book findBookOrThrowException(String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundByIdException(id));
    }
}
