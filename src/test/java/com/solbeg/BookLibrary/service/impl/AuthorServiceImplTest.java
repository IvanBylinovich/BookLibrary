package com.solbeg.BookLibrary.service.impl;

import com.solbeg.BookLibrary.dto.AuthorRequestDto;
import com.solbeg.BookLibrary.dto.AuthorResponseDto;
import com.solbeg.BookLibrary.exception.AuthorAlreadyExistByFirstNameAndLastNameException;
import com.solbeg.BookLibrary.exception.AuthorNotFoundByIdException;
import com.solbeg.BookLibrary.model.entity.Author;
import com.solbeg.BookLibrary.model.entity.Book;
import com.solbeg.BookLibrary.repository.AuthorRepository;
import com.solbeg.BookLibrary.repository.BookRepository;
import com.solbeg.BookLibrary.service.AuthorService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static utils.LibraryTestConstants.FIRST_NAME1;
import static utils.LibraryTestConstants.FIRST_NAME2;
import static utils.LibraryTestConstants.ID1;
import static utils.LibraryTestConstants.ID2;
import static utils.LibraryTestConstants.LAST_NAME1;
import static utils.LibraryTestConstants.LAST_NAME2;
import static utils.TestAuthorFactory.createAuthor1;
import static utils.TestAuthorFactory.createAuthor2;
import static utils.TestAuthorFactory.createAuthorList;
import static utils.TestAuthorFactory.createAuthorRequestDto1;
import static utils.TestAuthorFactory.createAuthorRequestDto2;
import static utils.TestBookFactory.createBookList2;

@ActiveProfiles("test")
@SpringBootTest
class AuthorServiceImplTest {

    @Autowired
    private AuthorService authorService;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private BookRepository bookRepository;

    @Test
    void findAllAuthors_correctWork_returnAuthorResponseDtoList() {
        //Date
        List<Author> authorList = createAuthorList();
        //When
        when(authorRepository.findAll()).thenReturn(authorList);
        //Then
        List<AuthorResponseDto> authorsResponseDto = authorService.findAllAuthors();

        assertEquals(2, authorsResponseDto.size());
        assertEquals(ID1, authorsResponseDto.get(0).getId());
        assertEquals(FIRST_NAME1, authorsResponseDto.get(0).getFirstName());
        assertEquals(LAST_NAME1, authorsResponseDto.get(0).getLastName());
        assertEquals(authorsResponseDto.get(1).getId(), ID2);
        assertEquals(FIRST_NAME2, authorsResponseDto.get(1).getFirstName());
        assertEquals(LAST_NAME2, authorsResponseDto.get(1).getLastName());
    }

    @Test
    void findAllAuthors_correctWork_returnEmptyList() {
        //When
        when(authorRepository.findAll()).thenReturn(Lists.emptyList());
        //Then
        List<AuthorResponseDto> authorsResponseDto = authorService.findAllAuthors();

        assertTrue(authorsResponseDto.isEmpty());
    }

    @Test
    void findAuthorById_correctWork_returnAuthorResponseDto() {
        //Date
        Author author1 = createAuthor1();
        //When
        when(authorRepository.findById(ID1)).thenReturn(Optional.of(author1));
        //Then
        AuthorResponseDto authorResponseDto = authorService.findAuthorById(ID1);

        assertEquals(ID1, authorResponseDto.getId());
        assertEquals(FIRST_NAME1, authorResponseDto.getFirstName());
        assertEquals(LAST_NAME1, authorResponseDto.getLastName());
    }

    @Test
    void findAuthorById_authorNotExistById_throwAuthorNotFoundByIdException() {
        //When
        when(authorRepository.findById(ID1)).thenReturn(Optional.empty());
        //Then
        AuthorNotFoundByIdException thrown = assertThrows(AuthorNotFoundByIdException.class, () -> {
            authorService.findAuthorById(ID1);
        });

        assertEquals(thrown.getMessage(), new AuthorNotFoundByIdException(ID1).getMessage());
    }

    @Test
    void createAuthor_correctWorkAuthorExist_returnAuthorResponseDto() {
        //Date
        Author author = createAuthor1();
        AuthorRequestDto authorRequestDto1 = createAuthorRequestDto1();
        //When
        when(authorRepository.findAuthorByFirstNameAndLastName(authorRequestDto1.getFirstName(), authorRequestDto1.getLastName()))
                .thenReturn(Optional.of(author));
        //Then
        AuthorResponseDto authorResponseDto = authorService.createAuthor(authorRequestDto1);

        assertEquals(ID1, authorResponseDto.getId());
        assertEquals(FIRST_NAME1, authorResponseDto.getFirstName());
        assertEquals(LAST_NAME1, authorResponseDto.getLastName());
    }

    @Test
    void createAuthor_correctWorkAuthorNotExist_returnAuthorResponseDto() {
        //Date
        Author author1 = createAuthor1();
        AuthorRequestDto authorRequestDto1 = createAuthorRequestDto1();
        //When
        when(authorRepository.findAuthorByFirstNameAndLastName(authorRequestDto1.getFirstName(), authorRequestDto1.getLastName()))
                .thenReturn(Optional.empty());
        when(authorRepository.save(author1)).thenReturn(author1);
        //Then
        AuthorResponseDto authorResponseDto = authorService.createAuthor(authorRequestDto1);

        assertEquals(FIRST_NAME1, authorResponseDto.getFirstName());
        assertEquals(LAST_NAME1, authorResponseDto.getLastName());
    }

    @Test
    void updateAuthor_correctWork_returnAuthorResponseDto() {
        //Date
        Author author1 = createAuthor1();
        AuthorRequestDto authorRequestDto2 = createAuthorRequestDto2();
        Author author2 = createAuthor2();
        //When
        when(authorRepository.findById(ID1)).thenReturn(Optional.of(author1));
        when(authorRepository.findAuthorByFirstNameAndLastName(authorRequestDto2.getFirstName(),
                authorRequestDto2.getLastName())).thenReturn(Optional.empty());
        when(authorRepository.save(author2)).thenReturn(author2);
        //Then
        AuthorResponseDto authorResponseDto = authorService.updateAuthor(ID1, authorRequestDto2);

        assertEquals(ID1, authorResponseDto.getId());
        assertEquals(FIRST_NAME2, authorResponseDto.getFirstName());
        assertEquals(LAST_NAME2, authorResponseDto.getLastName());
    }

    @Test
    void updateAuthor_authorNotExistById_throwAuthorNotFoundByIdException() {
        //Date
        AuthorRequestDto authorRequestDto1 = createAuthorRequestDto1();
        //When
        when(authorRepository.findById(ID1)).thenReturn(Optional.empty());
        //Then
        AuthorNotFoundByIdException thrown = assertThrows(AuthorNotFoundByIdException.class, () -> {
            authorService.updateAuthor(ID1, authorRequestDto1);
        });

        assertEquals(new AuthorNotFoundByIdException(ID1).getMessage(), thrown.getMessage());
    }

    @Test
    void updateAuthor_authorExistByFirstNameAndLastName_throwAuthorAlreadyExistByFirstNameAndLastNameException() {
        //Date
        Author author1 = createAuthor1();
        AuthorRequestDto authorRequestDto1 = createAuthorRequestDto1();
        //When
        when(authorRepository.findById(ID1)).thenReturn(Optional.of(author1));
        when(authorRepository.findAuthorByFirstNameAndLastName(FIRST_NAME1, LAST_NAME1)).thenReturn(Optional.of(author1));
        //Then
        AuthorAlreadyExistByFirstNameAndLastNameException thrown = assertThrows(AuthorAlreadyExistByFirstNameAndLastNameException.class, () -> {
            authorService.updateAuthor(ID1, authorRequestDto1);
        });

        assertEquals(new AuthorAlreadyExistByFirstNameAndLastNameException(FIRST_NAME1, LAST_NAME1, ID1).getMessage(), thrown.getMessage());
    }

    @Test
    void deleteAuthor_correctWorkAndNoRelatedBooks_applyMethodFindAllByAuthorFromBookRepositoryAndNotApplyMethodDeleteAll() {
        //Date
        Author author1 = createAuthor1();
        //When
        when(authorRepository.findById(ID1)).thenReturn(Optional.of(author1));
        when(bookRepository.findAllByAuthor(author1)).thenReturn(Optional.of(Lists.emptyList()));
        //Then
        authorService.deleteAuthor(ID1);
        verify(bookRepository, times(1)).findAllByAuthor(author1);
        verify(bookRepository, times(0)).deleteAll();
    }

    @Test
    void deleteAuthor_correctWorkAndNoRelatedBooks_applyMethodFindAllByAuthorFromBookRepositoryAndMethodDeleteAll() {
        //Date
        Author author1 = createAuthor1();
        List<Book> bookList2 = createBookList2();
        //When
        when(authorRepository.findById(ID1)).thenReturn(Optional.of(author1));
        when(bookRepository.findAllByAuthor(author1)).thenReturn(Optional.of(bookList2));
        //Then
        authorService.deleteAuthor(ID1);

        verify(bookRepository, times(1)).findAllByAuthor(author1);
        verify(bookRepository, times(1)).deleteAll(bookList2);
    }

    @Test
    void deleteAuthor_authorNotExistById_throwAuthorNotFoundByIdException() {
        //When
        when(authorRepository.findById(ID1)).thenReturn(Optional.empty());
        //Then
        AuthorNotFoundByIdException thrown = assertThrows(AuthorNotFoundByIdException.class, () -> {
            authorService.deleteAuthor(ID1);
        });

        assertEquals(new AuthorNotFoundByIdException(ID1).getMessage(), thrown.getMessage());
        verify(bookRepository, times(0)).findAllByAuthor(any());
        verify(bookRepository, times(0)).deleteAll(any());
    }

    @Test
    void getExistingAuthorOrCreateAuthor_correctWorkAuthorExist_returnAuthor() {
        //Date
        AuthorRequestDto authorRequestDto1 = createAuthorRequestDto1();
        //When
        when(authorRepository.findAuthorByFirstNameAndLastName(FIRST_NAME1, LAST_NAME1)).thenReturn(Optional.empty());
        //Then
        Author resultAuthor = authorService.getExistingAuthorOrCreateAuthor(authorRequestDto1);

        verify(authorRepository, times(1)).save(any(Author.class));
        assertEquals(FIRST_NAME1, resultAuthor.getFirstName());
        assertEquals(LAST_NAME1, resultAuthor.getLastName());
    }

    @Test
    void getExistingAuthorOrCreateAuthor_correctWorkAuthorNotExist_returnAuthor() {
        //Date
        Author author1 = createAuthor1();
        AuthorRequestDto authorRequestDto1 = createAuthorRequestDto1();
        //When
        when(authorRepository.findAuthorByFirstNameAndLastName(FIRST_NAME1, LAST_NAME1)).thenReturn(Optional.of(author1));
        //Then
        Author resultAuthor = authorService.getExistingAuthorOrCreateAuthor(authorRequestDto1);

        assertEquals(FIRST_NAME1, resultAuthor.getFirstName());
        assertEquals(LAST_NAME1, resultAuthor.getLastName());
    }
}