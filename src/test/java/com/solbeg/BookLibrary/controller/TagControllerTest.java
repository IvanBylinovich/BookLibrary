package com.solbeg.BookLibrary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solbeg.BookLibrary.dto.TagRequestDto;
import com.solbeg.BookLibrary.dto.TagResponseDto;
import com.solbeg.BookLibrary.model.entity.Tag;
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
import static utils.LibraryTestConstants.TAG_NAME3;
import static utils.TestTagFactory.createTag1;
import static utils.TestTagFactory.createTag2;
import static utils.TestTagFactory.createTagRequestDto1;
import static utils.TestTagFactory.createTagRequestDto3;

@SpringBootTest
@AutoConfigureMockMvc
class TagControllerTest {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private Tag tag1;
    private Tag tag2;

    private final TagRequestDto tagRequestDto1 = createTagRequestDto1();
    private final TagRequestDto tagRequestDto3 = createTagRequestDto3();

    @BeforeEach
    private void insertTestData() {
        tagRepository.deleteAll();
        tag1 = createTag1();
        tag1.setId(null);
        tag2 = createTag2();
        tag2.setId(null);
        tagRepository.save(tag1);
        tagRepository.save(tag2);
    }

    @AfterEach()
    private void clearTestData() {
        tagRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void getAllTags_correctWork_returnTagResponseDtoList() throws Exception {
        //Date
        String url = "/tags/allTags";
        //When
        String response = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //Then
        List<HashMap<String, String>> tags = objectMapper.readValue(response, List.class);
        List ids = tags.stream().map(m -> m.get("id")).toList();
        List tagNames = tags.stream().map(m -> m.get("name")).toList();

        assertEquals(2, tags.size());
        assertTrue(ids.containsAll(List.of(tag1.getId(), tag2.getId())));
        assertTrue(tagNames.containsAll(List.of(tag1.getName(), tag2.getName())));
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void getAllTags_correctWorkTagsNotExist_returnEmptyList() throws Exception {
        //Date
        String url = "/tags/allTags";
        tagRepository.deleteAll();
        //When
        String response = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //Then
        List tags = objectMapper.readValue(response, List.class);
        assertTrue(tags.isEmpty());
    }

    @Test
    @WithMockUser(username = "admin@admin.by", password = "123456789")
    void getAllTags_invalidAuthority_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/tags/allTags";
        //When
        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    void getAllTags_userNotAuthorised_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/tags/allTags";
        //When
        mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void getTagById_correctWork_returnTagResponseDto() throws Exception {
        //Date
        String url = "/tags/{id}";
        //When
        String response = mockMvc.perform(get(url, tag1.getId()).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //Then
        TagResponseDto tag = objectMapper.readValue(response, TagResponseDto.class);

        assertEquals(tag1.getId(), tag.getId());
        assertEquals(tag1.getName(), tag.getName());
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void getTagById_tagNotExistById_returnClientErrorCode() throws Exception {
        //Date
        String url = "/tags/{id}";
        //When
        mockMvc.perform(get(url, ID3).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", password = "123456789")
    void getTagById_invalidAuthority_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/tags/{id}";
        //When
        mockMvc.perform(get(url, tag1.getId()).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    void getTagById_userNotAuthorised_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/tags/{id}";
        //When
        mockMvc.perform(get(url, tag1.getId()).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void createTag_correctWorkTagNotExistByName_returnTagResponseDto() throws Exception {
        //Date
        String url = "/tags";
        tagRepository.deleteAll();
        //When
        String response = mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(tagRequestDto1))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //Then
        TagResponseDto tagResponseDto = objectMapper.readValue(response, TagResponseDto.class);
        assertNotEquals(tag1.getId(), tagResponseDto.getId());
        assertEquals(tag1.getName(), tagResponseDto.getName());
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void createTag_correctWorkTagExistByName_returnTagResponseDto() throws Exception {
        //Date
        String url = "/tags";
        //When
        String response = mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(tagRequestDto1))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //Then
        TagResponseDto tagResponseDto = objectMapper.readValue(response, TagResponseDto.class);
        assertEquals(tag1.getId(), tagResponseDto.getId());
        assertEquals(tag1.getName(), tagResponseDto.getName());
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void createTag_invalidRequestDto_returnClientErrorCode() throws Exception {
        //Date
        String url = "/tags";
        //When
        mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new TagRequestDto()))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", password = "123456789")
    void createTag_invalidAuthority_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/tags";
        //When
        mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(tagRequestDto1))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    void createTag_userNotAuthorised_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/tags";
        //When
        mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(tagRequestDto1))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void updateTag_correctWork_returnTagResponseDto() throws Exception {
        //Date
        String url = "/tags/{id}";
        //When
        String response = mockMvc.perform(put(url, tag1.getId()).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(tagRequestDto3))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //Then
        TagResponseDto tagResponseDto = objectMapper.readValue(response, TagResponseDto.class);
        assertEquals(tag1.getId(), tagResponseDto.getId());
        assertEquals(TAG_NAME3, tagResponseDto.getName());
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void updateTag_tagNotExistById_returnClientErrorCode() throws Exception {
        //Date
        String url = "/tags/{id}";
        //When
        mockMvc.perform(put(url, ID3).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void updateTag_invalidRequestDto_returnClientErrorCode() throws Exception {
        //Date
        String url = "/tags/{id}";
        //When
        mockMvc.perform(put(url, tag1.getId()).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new TagRequestDto()))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", password = "123456789")
    void updateTag_invalidAuthority_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/tags/{id}";
        //When
        mockMvc.perform(put(url, tag1.getId()).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    void updateTag_userNotAuthorised_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/tags/{id}";
        //When
        mockMvc.perform(put(url, tag1.getId()).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void deleteTag_correctWork_returnSuccessfulCode() throws Exception {
        //Date
        String url = "/tags/{id}";
        //When
        mockMvc.perform(delete(url, tag1.getId()).accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void deleteTag_tagNotExistById_returnClientErrorCode() throws Exception {
        //Date
        String url = "/tags/{id}";
        //When
        mockMvc.perform(delete(url, ID3).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", password = "123456789")
    void deleteTag_invalidAuthority_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/tags/{id}";
        //When
        mockMvc.perform(delete(url, tag1.getId()).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    void deleteTag_userNotAuthorised_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/tags/{id}";
        //When
        mockMvc.perform(delete(url, tag1.getId()).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }
}