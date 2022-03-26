package com.solbeg.BookLibrary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solbeg.BookLibrary.dto.UserRequestDto;
import com.solbeg.BookLibrary.dto.UserResponseDto;
import com.solbeg.BookLibrary.model.entity.User;
import com.solbeg.BookLibrary.repository.UserRepository;
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
import static utils.LibraryTestConstants.FIRST_NAME1;
import static utils.LibraryTestConstants.ID3;
import static utils.LibraryTestConstants.LAST_NAME1;
import static utils.LibraryTestConstants.USER_USERNAME3;
import static utils.TestUserFactory.createUser1;
import static utils.TestUserFactory.createUser2;
import static utils.TestUserFactory.createUserRequestDto3;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private User user1;
    private User user2;
    private UserRequestDto userRequestDto;

    @BeforeEach
    private void insertTestData() {
        userRepository.deleteAll();
        userRepository.deleteAll();
        user1 = createUser1();
        user2 = createUser2();
        user1.setId(null);
        user2.setId(null);
        userRepository.save(user1);
        userRepository.save(user2);
        userRequestDto = createUserRequestDto3();
    }

    @AfterEach()
    private void clearTestData() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void getAllUsers_correctWorkUsersExist_returnUserResponseDtoList() throws Exception {
        //Date
        String url = "/users/allUsers";
        //When
        String response = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //Then
        List<HashMap<String, String>> authors = objectMapper.readValue(response, List.class);
        HashMap<String, String> userData1 = authors.get(0);
        HashMap<String, String> userData2 = authors.get(1);

        assertEquals(2, authors.size());
        assertEquals(user1.getId(), userData1.get("id"));
        assertEquals(user1.getUsername(), userData1.get("username"));
        assertEquals(user1.getFirstName(), userData1.get("firstName"));
        assertEquals(user1.getLastName(), userData1.get("lastName"));
        assertEquals(user2.getId(), userData2.get("id"));
        assertEquals(user2.getUsername(), userData2.get("username"));
        assertEquals(user2.getFirstName(), userData2.get("firstName"));
        assertEquals(user2.getLastName(), userData2.get("lastName"));
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void getAllUsers_correctWorkNotExistUsers_returnEmptyList() throws Exception {
        //Date
        String url = "/users/allUsers";
        userRepository.deleteAll();
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
    void getAllUsers_invalidAuthority_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/users/allUsers";
        //When
        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    void getAllUsers_userNotAuthorised_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/users/allUsers";
        //When
        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void getUserById_correctWork_returnUserResponseDto() throws Exception {
        //Date
        String url = "/users/{id}";
        //When
        String response = mockMvc.perform(get(url, user1.getId()).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //Then
        UserResponseDto userResponseDto = objectMapper.readValue(response, UserResponseDto.class);
        assertEquals(user1.getId(), userResponseDto.getId());
        assertEquals(user1.getUsername(), userResponseDto.getUsername());
        assertEquals(user1.getFirstName(), userResponseDto.getFirstName());
        assertEquals(user1.getLastName(), userResponseDto.getLastName());
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void getUserById_userNotExistById_returnClientErrorCode() throws Exception {
        //Date
        String url = "/users/{id}";
        //When
        mockMvc.perform(get(url, ID3).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    void getUserById_userNotAuthorised_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/users/{id}";
        //When
        mockMvc.perform(get(url, user1.getId()).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void createUser_correctWork_returnOrderResponseDto() throws Exception {
        //Date
        String url = "/users/registration/user";
        //When
        String response = mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userRequestDto))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //Then
        UserResponseDto userResponseDto = objectMapper.readValue(response, UserResponseDto.class);
        assertEquals(USER_USERNAME3, userResponseDto.getUsername());
        assertEquals(FIRST_NAME1, userResponseDto.getFirstName());
        assertEquals(LAST_NAME1, userResponseDto.getLastName());
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void createUser_existUserWithSameUsername_returnClientErrorCode() throws Exception {
        //Date
        String url = "/users/registration/user";
        userRequestDto.setUsername(user1.getUsername());
        //When
        mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userRequestDto))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void createUser_invalidRequestDto_returnClientErrorCode() throws Exception {
        //Date
        String url = "/users/registration/user";
        //When
        mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new UserRequestDto()))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    void createUser_notAuthorisedUser_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/users/registration/user";
        //When
        mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userRequestDto))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void createUserAdmin_correctWork_returnOrderResponseDto() throws Exception {
        //Date
        String url = "/users/registration/admin";
        //When
        String response = mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userRequestDto))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //Then
        UserResponseDto userResponseDto = objectMapper.readValue(response, UserResponseDto.class);
        assertEquals(USER_USERNAME3, userResponseDto.getUsername());
        assertEquals(FIRST_NAME1, userResponseDto.getFirstName());
        assertEquals(LAST_NAME1, userResponseDto.getLastName());
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void createUserAdmin_existUserWithSameUsername_returnClientErrorCode() throws Exception {
        //Date
        String url = "/users/registration/admin";
        userRequestDto.setUsername(user1.getUsername());
        //When
        mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userRequestDto))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void createUserAdmin_invalidRequestDto_returnClientErrorCode() throws Exception {
        //Date
        String url = "/users/registration/admin";
        //When
        mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new UserRequestDto()))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", password = "123456789")
    void createUserAdmin_invalidAuthority_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/users/registration/admin";
        //When
        mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new UserRequestDto()))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }


    @Test
    void createUserAdmin_userNotAuthorised_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/users/registration/admin";
        //When
        mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userRequestDto))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void updateUser_correctWork_returnOrderResponseDto() throws Exception {
        //Date
        String url = "/users/{id}";
        //When
        String response = mockMvc.perform(put(url, user2.getId()).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userRequestDto))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //Then
        UserResponseDto userResponseDto = objectMapper.readValue(response, UserResponseDto.class);
        assertEquals(user2.getId(), userResponseDto.getId());
        assertEquals(USER_USERNAME3, userResponseDto.getUsername());
        assertEquals(FIRST_NAME1, userResponseDto.getFirstName());
        assertEquals(LAST_NAME1, userResponseDto.getLastName());
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void updateUser_userNotExistById_returnClientErrorCode() throws Exception {
        //Date
        String url = "/users/{id}";
        userRequestDto.setUsername(user1.getUsername());
        //When
        mockMvc.perform(put(url, ID3).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userRequestDto))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void updateUser_existUserWithSameUsername_returnClientErrorCode() throws Exception {
        //Date
        String url = "/users/{id}";
        userRequestDto.setUsername(user1.getUsername());
        //When
        mockMvc.perform(put(url, user2.getId()).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userRequestDto))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void updateUser_invalidRequestDto_returnClientErrorCode() throws Exception {
        //Date
        String url = "/users/{id}";
        //When
        mockMvc.perform(put(url, user2.getId()).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new UserRequestDto()))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", password = "123456789")
    void updateUser_invalidAuthority_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/users/{id}";
        //When
        mockMvc.perform(put(url, user2.getId()).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userRequestDto))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    void updateUser_userNotAuthorised_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/users/{id}";
        //When
        mockMvc.perform(put(url, user2.getId()).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userRequestDto))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void deleteUser_correctWork_returnSuccessfulCode() throws Exception {
        //Date
        String url = "/users/{id}";
        //When
        mockMvc.perform(delete(url, user2.getId()).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userRequestDto))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void deleteUser_userNotExistById_returnClientErrorCode() throws Exception {
        //Date
        String url = "/users/{id}";
        //When
        mockMvc.perform(delete(url, ID3).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userRequestDto))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    void deleteUser_userNotAuthorised_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/users/{id}";
        //When
        mockMvc.perform(delete(url, user2.getId()).accept(MediaType.APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }
}