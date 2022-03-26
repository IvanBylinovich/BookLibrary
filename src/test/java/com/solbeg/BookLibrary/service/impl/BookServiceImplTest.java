package com.solbeg.BookLibrary.service.impl;

import com.solbeg.BookLibrary.dto.AuthorRequestDto;
import com.solbeg.BookLibrary.dto.BookRequestDto;
import com.solbeg.BookLibrary.dto.BookResponseDto;
import com.solbeg.BookLibrary.dto.TagRequestDto;
import com.solbeg.BookLibrary.dto.TagResponseDto;
import com.solbeg.BookLibrary.exception.BookAlreadyExistByTitleAndAuthorException;
import com.solbeg.BookLibrary.exception.BookNotFoundByIdException;
import com.solbeg.BookLibrary.model.entity.Author;
import com.solbeg.BookLibrary.model.entity.Book;
import com.solbeg.BookLibrary.model.entity.Tag;
import com.solbeg.BookLibrary.repository.BookRepository;
import com.solbeg.BookLibrary.service.AuthorService;
import com.solbeg.BookLibrary.service.BookService;
import com.solbeg.BookLibrary.service.TagService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static utils.LibraryTestConstants.DATE_ZONED_DATA_TIME1;
import static utils.LibraryTestConstants.ID1;
import static utils.LibraryTestConstants.ID2;
import static utils.LibraryTestConstants.ID3;
import static utils.LibraryTestConstants.PRICE_BIG_DECIMAL1;
import static utils.LibraryTestConstants.TAG_NAME1;
import static utils.LibraryTestConstants.TAG_NAME2;
import static utils.LibraryTestConstants.TITLE1;
import static utils.LibraryTestConstants.TITLE2;
import static utils.LibraryTestConstants.URL1;
import static utils.LibraryTestConstants.URL2;
import static utils.TestAuthorFactory.createAuthor1;
import static utils.TestAuthorFactory.createAuthor2;
import static utils.TestAuthorFactory.createAuthorRequestDto1;
import static utils.TestBookFactory.createBook1;
import static utils.TestBookFactory.createBook2;
import static utils.TestBookFactory.createBookList1;
import static utils.TestBookFactory.createBookRequestDto1;
import static utils.TestBookFactory.createBookRequestDto2;
import static utils.TestTagFactory.createTagSet1;
import static utils.TestTagFactory.createTagSet2;
import static utils.TestTagFactory.createTagsRequestDto;

@SpringBootTest
class BookServiceImplTest {

    @Autowired
    BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private TagService tagService;

    @Test
    void findAllBooks_correctWork_returnEmptyList() {
        //When
        when(bookRepository.findAll()).thenReturn(Lists.emptyList());
        //Then
        List<BookResponseDto> tagsResponseDto = bookService.findAllBooks();
        assertTrue(tagsResponseDto.isEmpty());
    }

    @Test
    void findAllBooks_correctWork_returnBookResponseDtoList() {
        //Date
        List<Book> bookList1 = createBookList1();
        //When
        when(bookRepository.findAll()).thenReturn(bookList1);
        //Then
        List<BookResponseDto> booksResponseDto = bookService.findAllBooks();
        List<String> ids = booksResponseDto.stream().map(BookResponseDto::getId).toList();

        assertEquals(2, booksResponseDto.size());
        assertEquals(BookResponseDto.class, booksResponseDto.get(0).getClass());
        assertTrue(ids.contains(ID1));
        assertTrue(ids.contains(ID3));
    }

    @Test
    void findBookById_correctWork_returnBookResponseDto() {
        //Date
        Book book = createBook1();
        //When
        when(bookRepository.findById(ID1)).thenReturn(Optional.of(book));
        //Then
        BookResponseDto bookResponseDto = bookService.findBookById(ID1);

        List<String> tagsNames = bookResponseDto.getTags().stream().map(TagResponseDto::getName).toList();
        assertEquals(ID1, bookResponseDto.getId());
        assertEquals(TITLE1, bookResponseDto.getTitle());
        assertEquals(URL1, bookResponseDto.getImageUrl());
        assertEquals(PRICE_BIG_DECIMAL1, bookResponseDto.getPrice());
        assertEquals(ZonedDateTime.class, bookResponseDto.getCreatedAt().getClass());
        assertEquals(ZonedDateTime.class, bookResponseDto.getUpdatedAt().getClass());
        assertEquals(2, bookResponseDto.getTags().size());
        assertTrue(tagsNames.contains(TAG_NAME1));
        assertTrue(tagsNames.contains(TAG_NAME2));
    }

    @Test
    void findBookById_notExistBookById_throwBookNotFoundByIdException() {
        //When
        when(bookRepository.findById(ID1)).thenReturn(Optional.empty());
        //Then
        BookNotFoundByIdException thrown = assertThrows(BookNotFoundByIdException.class, () -> {
            bookService.findBookById(ID1);
        });

        assertEquals(new BookNotFoundByIdException(ID1).getMessage(), thrown.getMessage());
    }

    @Test
    void createBook_correctWorkAndBookExist_returnBookResponseDto() {
        //Date
        Author author1 = createAuthor1();
        Book book1 = createBook1();
        BookRequestDto bookRequestDto1 = createBookRequestDto1();
        AuthorRequestDto authorRequestDto1 = bookRequestDto1.getAuthor();
        //When
        when(authorService.getExistingAuthorOrCreateAuthor(authorRequestDto1)).thenReturn(author1);
        when(bookRepository.findBookByTitleAndAuthor(TITLE1, author1)).thenReturn(Optional.of(book1));
        //Then
        BookResponseDto bookResponseDto = bookService.createBook(bookRequestDto1);
        List<String> tagsNames = bookResponseDto.getTags().stream().map(TagResponseDto::getName).toList();

        assertEquals(ID1, bookResponseDto.getId());
        assertEquals(TITLE1, bookResponseDto.getTitle());
        assertEquals(URL1, bookResponseDto.getImageUrl());
        assertEquals(PRICE_BIG_DECIMAL1, bookResponseDto.getPrice());
        assertEquals(ZonedDateTime.class, bookResponseDto.getCreatedAt().getClass());
        assertEquals(ZonedDateTime.class, bookResponseDto.getUpdatedAt().getClass());
        assertEquals(2, bookResponseDto.getTags().size());
        assertTrue(tagsNames.contains(TAG_NAME1));
        assertTrue(tagsNames.contains(TAG_NAME2));
    }

    @Test
    void createBook_correctWorkAndBookNotExist_returnBookResponseDto() {
        //Date
        AuthorRequestDto authorRequestDto1 = createAuthorRequestDto1();
        Author author1 = createAuthor1();
        Set<TagRequestDto> tagsRequestDto1 = createTagsRequestDto();
        Set<Tag> tags = createTagSet1();
        BookRequestDto bookRequestDto1 = createBookRequestDto1();
        bookRequestDto1.setTags(tagsRequestDto1);
        //When
        when(authorService.getExistingAuthorOrCreateAuthor(authorRequestDto1)).thenReturn(author1);
        when(bookRepository.findBookByTitleAndAuthor(TITLE1, author1)).thenReturn(Optional.empty());
        when(tagService.getExistingTagsOrThrowException(tagsRequestDto1)).thenReturn(tags);
        //Then
        BookResponseDto bookResponseDto = bookService.createBook(bookRequestDto1);
        List<String> tagsNames = bookResponseDto.getTags().stream().map(TagResponseDto::getName).toList();

        verify(bookRepository, times(1)).save(any(Book.class));
        assertEquals(TITLE1, bookResponseDto.getTitle());
        assertEquals(URL1, bookResponseDto.getImageUrl());
        assertEquals(PRICE_BIG_DECIMAL1, bookResponseDto.getPrice());
        assertEquals(ZonedDateTime.class, bookResponseDto.getCreatedAt().getClass());
        assertEquals(ZonedDateTime.class, bookResponseDto.getUpdatedAt().getClass());
        assertEquals(2, bookResponseDto.getTags().size());
        assertTrue(tagsNames.contains(TAG_NAME1));
        assertTrue(tagsNames.contains(TAG_NAME2));
    }

    @Test
    void updateBook_correctWork_returnUpdatedBookResponseDto() {
        //Date
        Book book1 = createBook1();
        Set<Tag> tags2 = createTagSet2();
        BookRequestDto bookRequestDto2 = createBookRequestDto2();
        Author author2 = createAuthor2();
        AuthorRequestDto authorRequestDto2 = bookRequestDto2.getAuthor();
        //When
        when(bookRepository.findById(ID1)).thenReturn(Optional.of(book1));
        when(authorService.getExistingAuthorOrCreateAuthor(authorRequestDto2)).thenReturn(author2);
        when(bookRepository.findBookByTitleAndAuthor(TITLE1, author2)).thenReturn(Optional.empty());
        when(tagService.getExistingTagsOrThrowException(bookRequestDto2.getTags())).thenReturn(tags2);
        //Then
        BookResponseDto bookResponseDto = bookService.updateBook(ID1, bookRequestDto2);
        List<String> tagNames = bookResponseDto.getTags().stream().map(TagResponseDto::getName).toList();
        List<String> tagIds = bookResponseDto.getTags().stream().map(TagResponseDto::getId).toList();

        verify(bookRepository, times(1)).save(any(Book.class));
        assertEquals(ID1, bookResponseDto.getId());
        assertEquals(TITLE2, bookResponseDto.getTitle());
        assertEquals(URL2, bookResponseDto.getImageUrl());
        assertEquals(author2.getId(), bookResponseDto.getAuthor().getId());
        assertEquals(author2.getFirstName(), bookResponseDto.getAuthor().getFirstName());
        assertEquals(author2.getLastName(), bookResponseDto.getAuthor().getLastName());
        assertEquals(1, bookResponseDto.getTags().size());
        assertTrue(tagNames.contains(TAG_NAME1));
        assertTrue(tagIds.contains(ID1));
        assertFalse(tagNames.contains(TAG_NAME2));
        assertFalse(tagIds.contains(ID2));
        assertEquals(ZonedDateTime.class, bookResponseDto.getUpdatedAt().getClass());
        assertEquals(ZonedDateTime.class, bookResponseDto.getCreatedAt().getClass());
        assertEquals(DATE_ZONED_DATA_TIME1, bookResponseDto.getCreatedAt());
        assertNotEquals(DATE_ZONED_DATA_TIME1, bookResponseDto.getUpdatedAt());
    }

    @Test
    void updateBook_bookNotExistById_throwBookNotFoundByIdException() {
        //Date
        BookRequestDto bookRequestDto1 = createBookRequestDto1();
        //When
        when(bookRepository.findById(ID1)).thenReturn(Optional.empty());
        //Then
        BookNotFoundByIdException thrown = assertThrows(BookNotFoundByIdException.class, () -> {
            bookService.updateBook(ID1, bookRequestDto1);
        });

        assertEquals(new BookNotFoundByIdException(ID1).getMessage(), thrown.getMessage());
    }

    @Test
    void updateBook_bookExistByTitleAndAuthor_throwBookAlreadyExistByTitleAndAuthorException() {
        //Date
        Book book1 = createBook1();
        Author author2 = createAuthor2();
        Book book2 = createBook2();
        BookRequestDto bookRequestDto2 = createBookRequestDto2();
        AuthorRequestDto authorRequestDto2 = bookRequestDto2.getAuthor();
        //When
        when(bookRepository.findById(ID1)).thenReturn(Optional.of(book1));
        when(authorService.getExistingAuthorOrCreateAuthor(authorRequestDto2)).thenReturn(author2);
        when(bookRepository.findBookByTitleAndAuthor(TITLE2, author2)).thenReturn(Optional.of(book2));
        //Then
        BookAlreadyExistByTitleAndAuthorException thrown = assertThrows(BookAlreadyExistByTitleAndAuthorException.class, () -> {
            bookService.updateBook(ID1, bookRequestDto2);
        });

        assertEquals(new BookAlreadyExistByTitleAndAuthorException(TITLE2, author2, ID3).getMessage(), thrown.getMessage());
    }

    @Test
    void deleteBook_correctWork_applyDeleteMethod() {
        //Date
        Book book1 = createBook1();
        //When
        when(bookRepository.findById(ID1)).thenReturn(Optional.of(book1));
        //Then
        bookService.deleteBook(ID1);

        verify(bookRepository, times(1)).delete(book1);
    }

    @Test
    void deleteBook_bookNotExistById_throwBookNotFoundByIdException() {
        //When
        when(bookRepository.findById(ID1)).thenReturn(Optional.empty());
        //Then
        BookNotFoundByIdException thrown = assertThrows(BookNotFoundByIdException.class, () -> {
            bookService.deleteBook(ID1);
        });

        assertEquals(new BookNotFoundByIdException(ID1).getMessage(), thrown.getMessage());
    }

    @Test
    void findBookOrThrowException_correctWork_returnBook() {
        //Date
        Book book1 = createBook1();
        //When
        when(bookRepository.findById(ID1)).thenReturn(Optional.of(book1));
        //Then
        Book book = bookService.findBookOrThrowException(ID1);
        List<String> tagsNames = book.getTags().stream().map(Tag::getName).toList();

        assertEquals(ID1, book.getId());
        assertEquals(TITLE1, book.getTitle());
        assertEquals(URL1, book.getImageUrl());
        assertEquals(PRICE_BIG_DECIMAL1, book.getPrice());
        assertEquals(ZonedDateTime.class, book.getCreatedAt().getClass());
        assertEquals(ZonedDateTime.class, book.getUpdatedAt().getClass());
        assertEquals(2, book.getTags().size());
        assertTrue(tagsNames.contains(TAG_NAME1));
        assertTrue(tagsNames.contains(TAG_NAME2));
    }

    @Test
    void findBookOrThrowException_bookNotExistById_throwBookNotFoundByIdException() {
        //When
        when(bookRepository.findById(ID1)).thenReturn(Optional.empty());
        //Then
        BookNotFoundByIdException thrown = assertThrows(BookNotFoundByIdException.class, () -> {
            bookService.findBookOrThrowException(ID1);
        });

        assertEquals(new BookNotFoundByIdException(ID1).getMessage(), thrown.getMessage());
    }
}