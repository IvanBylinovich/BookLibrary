package com.solbeg.BookLibrary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solbeg.BookLibrary.dto.BookRequestDto;
import com.solbeg.BookLibrary.dto.BookResponseDto;
import com.solbeg.BookLibrary.dto.TagRequestDto;
import com.solbeg.BookLibrary.model.entity.Author;
import com.solbeg.BookLibrary.model.entity.Book;
import com.solbeg.BookLibrary.model.entity.Tag;
import com.solbeg.BookLibrary.repository.AuthorRepository;
import com.solbeg.BookLibrary.repository.BookRepository;
import com.solbeg.BookLibrary.repository.TagRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utils.LibraryTestConstants.ID3;
import static utils.TestAuthorFactory.createAuthor1_notSaved;
import static utils.TestAuthorFactory.createAuthor2_notSaved;
import static utils.TestAuthorFactory.createAuthorRequestDto3;
import static utils.TestBookFactory.createBook1;
import static utils.TestBookFactory.createBook2;
import static utils.TestBookFactory.createBookRequestDto1;
import static utils.TestBookFactory.createBookRequestDto2;
import static utils.TestTagFactory.createTag1;
import static utils.TestTagFactory.createTag2;
import static utils.TestTagFactory.createTagRequestDto2;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private TagRequestDto tagRequestDto = createTagRequestDto2();

    private Tag tag1 = createTag1();
    private Tag tag2 = createTag2();
    private Author author1 = createAuthor1_notSaved();
    private Author author2 = createAuthor2_notSaved();
    private Book book1 = createBook1();
    private Book book2 = createBook2();
    private final BookRequestDto bookRequestDto1 = createBookRequestDto1();
    private final BookRequestDto bookRequestDto2 = createBookRequestDto2();

    @BeforeEach
    private void setTestData() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        tagRepository.deleteAll();
        tag1.setId(null);
        tag2.setId(null);
        tagRepository.saveAll(List.of(tag1, tag2));
        authorRepository.saveAll(List.of(author1, author2));
        book1.setId(null);
        book2.setId(null);
        book1.setAuthor(author1);
        book1.setTags(Set.of(tag1, tag2));
        book2.setAuthor(author2);
        book2.setTags(Set.of(tag1));
        bookRepository.saveAll(List.of(book1, book2));
    }

    @AfterEach()
    private void clearTestData() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        tagRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void getAllBook_correctWork_returnBookResponseDtoList() throws Exception {
        //Date
        String url = "/books/allBooks";
        //When
        String response = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //Then
        List<HashMap<String, String>> books = objectMapper.readValue(response, List.class);
        HashMap<String, String> bookData1 = books.get(0);
        HashMap<String, String> bookData2 = books.get(1);

        assertEquals(2, books.size());
        assertEquals(book1.getId(), bookData1.get("id"));
        assertEquals(book1.getTitle(), bookData1.get("title"));
        assertEquals(book1.getImageUrl(), bookData1.get("imageUrl"));
        assertEquals(book2.getId(), bookData2.get("id"));
        assertEquals(book2.getTitle(), bookData2.get("title"));
        assertEquals(book2.getImageUrl(), bookData2.get("imageUrl"));
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void getAllBook_correctWorkBooksNoExist_returnEmptyList() throws Exception {
        //Date
        String url = "/books/allBooks";
        bookRepository.deleteAll();
        //When
        String response = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //Then
        List<HashMap<String, String>> books = objectMapper.readValue(response, List.class);

        assertTrue(books.isEmpty());
    }

    @Test
    void getAllBook_notAuthorisedUser_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/books/allBooks";
        //When
        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", password = "123456789")
    void getAllBook_invalidAuthority_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/books/allBooks";
        //When
        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", password = "123456789")
    void getBookById_correctWork_returnBookResponseDto() throws Exception {
        //Date
        String url = "/books/{id}";
        //When
        String response = mockMvc.perform(get(url, book1.getId()).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //Then
        BookResponseDto bookResponseDto = objectMapper.readValue(response, BookResponseDto.class);

        assertEquals(book1.getId(), bookResponseDto.getId());
        assertEquals(book1.getTitle(), bookResponseDto.getTitle());
        assertEquals(book1.getImageUrl(), bookResponseDto.getImageUrl());
        assertEquals(book1.getPrice(), bookResponseDto.getPrice());
        assertEquals(book1.getTags().size(), bookResponseDto.getTags().size());
        assertEquals(book1.getAuthor().getId(), bookResponseDto.getAuthor().getId());
    }

    @Test
    @WithMockUser(username = "admin@admin.by", password = "123456789")
    void getBookById_notExistBookById_returnClientErrorCode() throws Exception {
        //Date
        String url = "/books/{id}";
        //When
        mockMvc.perform(get(url, ID3).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    void getBookById_notAuthorisedUser_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/books/{id}";
        //When
        mockMvc.perform(get(url, book1.getId()).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void createBook_correctWorkBookNotExist_returnBookResponseDto() throws Exception {
        //Date
        String url = "/books";
        bookRepository.deleteAll();
        //When
        String response = mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(bookRequestDto1))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //Then
        BookResponseDto bookResponseDto = objectMapper.readValue(response, BookResponseDto.class);

        assertNotEquals(book1.getId(), bookResponseDto.getId());
        assertEquals(book1.getTitle(), bookResponseDto.getTitle());
        assertEquals(book1.getImageUrl(), bookResponseDto.getImageUrl());
        assertEquals(book1.getPrice(), bookResponseDto.getPrice());
        assertEquals(book1.getTags().size(), bookResponseDto.getTags().size());
        assertEquals(book1.getAuthor().getId(), bookResponseDto.getAuthor().getId());
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void createBook_correctWorkBookExist_returnBookResponseDto() throws Exception {
        //Date
        String url = "/books";
        //When
        String response = mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(bookRequestDto1))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //Then
        BookResponseDto book = objectMapper.readValue(response, BookResponseDto.class);

        assertEquals(book1.getTitle(), book.getTitle());
        assertEquals(book1.getImageUrl(), book.getImageUrl());
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void createBook_correctWorkAuthorNotExist_returnBookResponseDto() throws Exception {
        //Date
        String url = "/books";
        bookRequestDto1.setAuthor(createAuthorRequestDto3());
        //When
        String response = mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(bookRequestDto1))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //Then
        BookResponseDto bookResponseDto = objectMapper.readValue(response, BookResponseDto.class);

        assertNotEquals(book1.getId(), bookResponseDto.getId());
        assertEquals(book1.getTitle(), bookResponseDto.getTitle());
        assertEquals(book1.getImageUrl(), bookResponseDto.getImageUrl());
        assertEquals(book1.getPrice(), bookResponseDto.getPrice());
        assertEquals(book1.getTags().size(), bookResponseDto.getTags().size());
        assertNotEquals(book1.getAuthor().getId(), bookResponseDto.getAuthor().getId());
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void createBook_invalidRequestDto_returnClientErrorCode() throws Exception {
        //Date
        String url = "/books";
        //When
        mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new BookRequestDto()))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    void createBook_notAuthorisedUser_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/books";
        //When
        mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(bookRequestDto1))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", password = "123456789")
    void createBook_invalidAuthority_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/books";
        //When
        mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(bookRequestDto1))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void updateBook_correctWork_returnBookResponseDto() throws Exception {
        //Date
        String url = "/books/{id}";
        bookRepository.deleteById(book2.getId());
        //When
        String response = mockMvc.perform(put(url, book1.getId()).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(bookRequestDto2))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //Then
        BookResponseDto bookResponseDto = objectMapper.readValue(response, BookResponseDto.class);

        assertEquals(book1.getId(), bookResponseDto.getId());
        assertEquals(book2.getTitle(), bookResponseDto.getTitle());
        assertEquals(book2.getImageUrl(), bookResponseDto.getImageUrl());
        assertEquals(book2.getPrice(), bookResponseDto.getPrice());
        assertEquals(book2.getTags().size(), bookResponseDto.getTags().size());
        assertEquals(book2.getAuthor().getId(), bookResponseDto.getAuthor().getId());
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void updateBook_notExistBookById_returnClientErrorCode() throws Exception {
        //Date
        String url = "/books/{id}";
        bookRepository.deleteById(book2.getId());
        //When
        mockMvc.perform(put(url, ID3).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(bookRequestDto2))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void updateBook_existBookTitleAndAuthor_returnClientErrorCode() throws Exception {
        //Date
        String url = "/books/{id}";
        //When
        mockMvc.perform(put(url, book1.getId()).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(bookRequestDto2))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void updateBook_tagNotExist_returnClientErrorCode() throws Exception {
        //Date
        bookRequestDto2.setTags(Set.of(new TagRequestDto("TAG")));
        String url = "/books/{id}";
        //When
        mockMvc.perform(put(url, book1.getId()).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(bookRequestDto2))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void updateBook_invalidRequestDto_returnClientErrorCode() throws Exception {
        //Date
        String url = "/books/{id}";
        bookRepository.deleteById(book2.getId());
        //When
        mockMvc.perform(put(url, book1.getId()).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new BookRequestDto()))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    void updateBook_notAuthorisedUser_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/books/{id}";
        bookRepository.deleteById(book2.getId());
        //When
        mockMvc.perform(put(url, book1.getId()).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(bookRequestDto2))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    void updateBook_invalidAuthority_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/books/{id}";
        bookRepository.deleteById(book2.getId());
        //When
        mockMvc.perform(put(url, book1.getId()).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(bookRequestDto2))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void deleteBook_correctWork_returnSuccessfulCode() throws Exception {
        //Date
        String url = "/books/{id}";
        //When
        mockMvc.perform(delete(url, book1.getId()).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void deleteBook_notExistBookById_returnClientErrorCode() throws Exception {
        //Date
        String url = "/books/{id}";
        //When
        mockMvc.perform(delete(url, ID3).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    void deleteBook_notAuthorisedUser_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/books/{id}";
        //When
        mockMvc.perform(delete(url, book1.getId()).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", password = "123456789")
    void deleteBook_invalidAuthority_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/books/{id}";
        //When
        mockMvc.perform(delete(url, book1.getId()).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }
}