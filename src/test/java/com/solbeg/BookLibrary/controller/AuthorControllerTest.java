package com.solbeg.BookLibrary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solbeg.BookLibrary.dto.AuthorRequestDto;
import com.solbeg.BookLibrary.dto.AuthorResponseDto;
import com.solbeg.BookLibrary.model.entity.Author;
import com.solbeg.BookLibrary.repository.AuthorRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utils.LibraryTestConstants.FIRST_NAME3;
import static utils.LibraryTestConstants.ID3;
import static utils.LibraryTestConstants.LAST_NAME3;
import static utils.TestAuthorFactory.createAuthor1_notSaved;
import static utils.TestAuthorFactory.createAuthor2_notSaved;
import static utils.TestAuthorFactory.createAuthorRequestDto1;
import static utils.TestAuthorFactory.createAuthorRequestDto3;

@SpringBootTest
@AutoConfigureMockMvc
class AuthorControllerTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Author author1;
    private Author author2;
    private final AuthorRequestDto authorRequestDto1 = createAuthorRequestDto1();
    private final AuthorRequestDto authorRequestDto2 = createAuthorRequestDto1();
    private final AuthorRequestDto authorRequestDto3 = createAuthorRequestDto3();

    @BeforeEach
    private void insertTestData() {
        authorRepository.deleteAll();
        author1 = createAuthor1_notSaved();
        author2 = createAuthor2_notSaved();
        authorRepository.save(author1);
        authorRepository.save(author2);
    }

    @AfterEach()
    private void clearTestData() {
        authorRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void getAllAuthors_correctWork_returnAuthorResponseDtoList() throws Exception {
        //Date
        String url = "/authors/allAuthors";
        //When
        String response = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //Then
        List<HashMap<String, String>> authors = objectMapper.readValue(response, List.class);
        HashMap<String, String> authorData1 = authors.get(0);
        HashMap<String, String> authorData2 = authors.get(1);

        assertEquals(2, authors.size());
        assertEquals(author1.getId(), authorData1.get("id"));
        assertEquals(author1.getFirstName(), authorData1.get("firstName"));
        assertEquals(author1.getLastName(), authorData1.get("lastName"));
        assertEquals(author2.getId(), authorData2.get("id"));
        assertEquals(author2.getFirstName(), authorData2.get("firstName"));
        assertEquals(author2.getLastName(), authorData2.get("lastName"));
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void getAllAuthors_correctWorkAuthorsNotExist_returnEmptyList() throws Exception {
        //Date
        String url = "/authors/allAuthors";
        authorRepository.deleteAll();

        //When
        String response = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //Then
        List<HashMap<String, String>> authors = objectMapper.readValue(response, List.class);

        assertTrue(authors.isEmpty());
    }

    @Test
    @WithMockUser(username = "admin@admin.by", password = "123456789")
    void getAllAuthors_invalidAuthority_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/authors/allAuthors";
        //When
        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    void getAllAuthors_userNotAuthorised_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/authors/allAuthors";
        //When
        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", password = "123456789")
    void getAuthorById_correctWork_returnAuthorResponseDto() throws Exception {
        //Date
        String url = "/authors/{Id}";
        //When
        String response = mockMvc.perform(get(url, author1.getId()).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //Then
        AuthorResponseDto authorResponseDto = objectMapper.readValue(response, AuthorResponseDto.class);
        assertEquals(author1.getId(), authorResponseDto.getId());
        assertEquals(author1.getFirstName(), authorResponseDto.getFirstName());
        assertEquals(author1.getLastName(), authorResponseDto.getLastName());
    }

    @Test
    @WithMockUser(username = "admin@admin.by", password = "123456789")
    void getAuthorById_authorNotExistById_returnClientErrorCode() throws Exception {
        //Date
        String url = "/authors/{Id}";
        //When
        mockMvc.perform(get(url, ID3).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    void getAuthorById_userNotAuthenticated_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/authors/{Id}";
        //When
        mockMvc.perform(get(url, author1.getId()).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void createAuthor_correctWorkAuthorNotExistByFullName_returnAuthorResponseDto() throws Exception {
        //Date
        String url = "/authors";
        //When
        String response = mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(authorRequestDto3))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //Then
        AuthorResponseDto authorResponseDto = objectMapper.readValue(response, AuthorResponseDto.class);
        assertEquals(FIRST_NAME3, authorResponseDto.getFirstName());
        assertEquals(LAST_NAME3, authorResponseDto.getLastName());
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void createAuthor_correctWorkAuthorExistByFullName_returnAuthorResponseDto() throws Exception {
        //Date
        String url = "/authors";
        //When
        String response = mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(authorRequestDto1))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //Then
        AuthorResponseDto authorResponseDto = objectMapper.readValue(response, AuthorResponseDto.class);
        assertEquals(author1.getId(), authorResponseDto.getId());
        assertEquals(author1.getFirstName(), authorResponseDto.getFirstName());
        assertEquals(author1.getLastName(), authorResponseDto.getLastName());
    }

    @Test
    @WithMockUser(username = "admin@admin.by", password = "123456789")
    void createAuthor_invalidRequestDTO_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/authors";
        //When
        mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new AuthorRequestDto()))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", password = "123456789")
    void createAuthor_invalidAuthority_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/authors";
        //When
        mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(authorRequestDto1))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    void createAuthor_userNotAuthenticated_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/authors";
        //When
        mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(authorRequestDto1))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void updateAuthor_invalidRequestDTO_returnClientErrorCode() throws Exception {
        //Date
        String url = "/authors/{id}";
        //When
        mockMvc.perform(put(url, author1.getId()).accept(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(new AuthorRequestDto()))
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void updateAuthor_correctWork_returnAuthorResponseDto() throws Exception {
        //Date
        String url = "/authors/{id}";
        //When
        String response = mockMvc.perform(put(url, author1.getId()).accept(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(authorRequestDto3))
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //then
        AuthorResponseDto authorResponseDto = objectMapper.readValue(response, AuthorResponseDto.class);
        assertEquals(author1.getId(), authorResponseDto.getId());
        assertEquals(FIRST_NAME3, authorResponseDto.getFirstName());
        assertEquals(LAST_NAME3, authorResponseDto.getLastName());
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void updateAuthor_authorNotExistById_returnClientErrorCode() throws Exception {
        //Date
        String url = "/authors/{id}";
        //When
        mockMvc.perform(put(url, ID3).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(authorRequestDto2))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void updateAuthor_authorExistByFullName_returnClientErrorCode() throws Exception {
        //Date
        String url = "/authors/{id}";
        //When
        mockMvc.perform(put(url, author1.getId()).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(authorRequestDto2))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", password = "123456789")
    void updateAuthor_invalidAuthority_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/authors/{id}";
        //When
        mockMvc.perform(put(url, author1.getId()).accept(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(authorRequestDto2))
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    void updateAuthor_userNotAuthenticated_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/authors/{id}";
        //When
        mockMvc.perform(put(url, author1.getId()).accept(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(authorRequestDto2))
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void deleteAuthor_correctWork_returnSuccessfulCode() throws Exception {
        //Date
        String url = "/authors/{Id}";
        //When
        mockMvc.perform(delete(url, author1.getId()).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void deleteAuthor_authorNotExistById_returnClientErrorCode() throws Exception {
        //Date
        String url = "/authors/{Id}";
        //When
        mockMvc.perform(delete(url, ID3).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", password = "123456789")
    void deleteAuthor_invalidAuthority_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/authors/{Id}";
        //When
        mockMvc.perform(delete(url, author1.getId()).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    void deleteAuthor_userNotAuthenticated_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/authors/{Id}";
        //When
        mockMvc.perform(delete(url, author1.getId()).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }
}