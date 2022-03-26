package com.solbeg.BookLibrary.service.impl;

import com.solbeg.BookLibrary.dto.TagRequestDto;
import com.solbeg.BookLibrary.dto.TagResponseDto;
import com.solbeg.BookLibrary.exception.TagAlreadyExistByNameException;
import com.solbeg.BookLibrary.exception.TagNotFoundByIdException;
import com.solbeg.BookLibrary.exception.TagNotFoundByNameException;
import com.solbeg.BookLibrary.model.entity.Book;
import com.solbeg.BookLibrary.model.entity.Tag;
import com.solbeg.BookLibrary.repository.BookRepository;
import com.solbeg.BookLibrary.repository.TagRepository;
import com.solbeg.BookLibrary.service.TagService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static utils.LibraryTestConstants.ID1;
import static utils.LibraryTestConstants.ID2;
import static utils.LibraryTestConstants.ID3;
import static utils.LibraryTestConstants.TAG_NAME1;
import static utils.LibraryTestConstants.TAG_NAME2;
import static utils.LibraryTestConstants.TAG_NAME3;
import static utils.TestBookFactory.createBookList1;
import static utils.TestTagFactory.createTag1;
import static utils.TestTagFactory.createTag2;
import static utils.TestTagFactory.createTag3;
import static utils.TestTagFactory.createTagList;
import static utils.TestTagFactory.createTagRequestDto3;
import static utils.TestTagFactory.createTagsRequestDto;
import static utils.TestTagFactory.createTagsRequestDto2;

@SpringBootTest
class TagServiceImplTest {

    @Autowired
    private TagService tagService;

    @MockBean
    private TagRepository tagRepository;

    @MockBean
    private BookRepository bookRepository;

    @Test
    void findAllAuthors_correctWorkAuthorsNotExist_returnEmptyList() {
        //When
        when(tagRepository.findAll()).thenReturn(Lists.emptyList());
        //Then
        Set<TagResponseDto> tagsResponseDto = tagService.findAllTags();

        assertTrue(tagsResponseDto.isEmpty());
    }

    @Test
    void findAllTags_correctWorkAuthorsExist_returnTagsList() {
        //Date
        List<Tag> tagsList = createTagList();
        //When
        when(tagRepository.findAll()).thenReturn(tagsList);
        //Then
        List<TagResponseDto> tagsResponseDto = new ArrayList<>(tagService.findAllTags());
        Set<String> tagsName = Set.of(tagsResponseDto.get(0).getName(), tagsResponseDto.get(1).getName());
        Set<String> tagsId = Set.of(tagsResponseDto.get(0).getId(), tagsResponseDto.get(1).getId());

        assertEquals(tagsResponseDto.size(), 2);
        assertTrue(tagsName.contains(TAG_NAME1));
        assertTrue(tagsName.contains(TAG_NAME2));
        assertTrue(tagsId.contains(ID1));
        assertTrue(tagsId.contains(ID2));
    }

    @Test
    void findTagById_correctWorkValidId_returnTagResponseDto() {
        //Date
        Tag tag1 = createTag1();
        //When
        when(tagRepository.findById(ID1)).thenReturn(Optional.of(tag1));
        //Then
        TagResponseDto tagResponseDto = tagService.findTagById(ID1);

        assertEquals(ID1, tagResponseDto.getId());
        assertEquals(TAG_NAME1, tagResponseDto.getName());
    }

    @Test
    void findTagById_invalidId_throwTagNotFoundByIdException() {
        //When
        when(tagRepository.findById(ID1)).thenReturn(Optional.empty());
        //Then
        TagNotFoundByIdException thrown = assertThrows(TagNotFoundByIdException.class, () -> {
            tagService.findTagById(ID1);
        });

        assertEquals(thrown.getMessage(), new TagNotFoundByIdException(ID1).getMessage());
    }

    @Test
    void createTag_correctWorkTagNotExist_returnTagResponseDto() {
        //Date
        TagRequestDto tagRequestDto3 = createTagRequestDto3();
        //When
        when(tagRepository.findTagByName(TAG_NAME3)).thenReturn(Optional.empty());
        //Then
        TagResponseDto tagResponseDto = tagService.createTag(tagRequestDto3);

        assertEquals(TAG_NAME3, tagResponseDto.getName());
        verify(tagRepository, times(1)).findTagByName(TAG_NAME3);
        verify(tagRepository, times(1)).save(any());
    }

    @Test
    void createTag_correctWorkTagExist_returnTagResponseDto() {
        //Date
        Tag tag3 = createTag3();
        TagRequestDto tagRequestDto3 = createTagRequestDto3();
        //When
        when(tagRepository.findTagByName(TAG_NAME3)).thenReturn(Optional.of(tag3));
        //Then
        TagResponseDto tagResponseDto = tagService.createTag(tagRequestDto3);

        assertEquals(ID3, tagResponseDto.getId());
        assertEquals(TAG_NAME3, tagResponseDto.getName());
        verify(tagRepository, times(1)).findTagByName(TAG_NAME3);
    }

    @Test
    void updateTag_correctWorkValidData_returnTagResponseDto() {
        //Date
        TagRequestDto tagRequestDto3 = createTagRequestDto3();
        Tag tag = createTag1();
        //When
        when(tagRepository.findById(ID1)).thenReturn(Optional.of(tag));
        when(tagRepository.findTagByName(TAG_NAME3)).thenReturn(Optional.empty());
        when(tagRepository.save(any(Tag.class))).thenReturn(tag);
        //Then
        TagResponseDto tagResponseDto = tagService.updateTag(ID1, tagRequestDto3);

        assertEquals(ID1, tagResponseDto.getId());
        assertEquals(TAG_NAME3, tagResponseDto.getName());
        verify(tagRepository, times(1)).findById(ID1);
        verify(tagRepository, times(1)).findTagByName(TAG_NAME3);
        verify(tagRepository, times(1)).save(any(Tag.class));
    }

    @Test
    void updateTag_tagAlreadyExistByName_throwTagAlreadyExistByNameException() {
        //Date
        TagRequestDto tagRequestDto3 = createTagRequestDto3();
        //When
        when(tagRepository.findById(ID1)).thenReturn(Optional.empty());
        //Then
        TagNotFoundByIdException thrown = assertThrows(TagNotFoundByIdException.class, () -> {
            tagService.updateTag(ID1, tagRequestDto3);
        });

        assertEquals(thrown.getMessage(), new TagNotFoundByIdException(ID1).getMessage());
        verify(tagRepository, times(1)).findById(ID1);
    }

    @Test
    void updateTag_tagNotExistById_throwTagNotFoundByIdException() {
        //Date
        Tag tag1 = createTag1();
        Tag tag3 = createTag3();
        TagRequestDto tagRequestDto3 = createTagRequestDto3();
        //When
        when(tagRepository.findById(ID1)).thenReturn(Optional.of(tag1));
        when(tagRepository.findTagByName(TAG_NAME3)).thenReturn(Optional.of(tag3));
        //Then
        TagAlreadyExistByNameException thrown = assertThrows(TagAlreadyExistByNameException.class, () -> {
            tagService.updateTag(ID1, tagRequestDto3);
        });

        assertEquals(thrown.getMessage(), new TagAlreadyExistByNameException(TAG_NAME3, ID3).getMessage());
        verify(tagRepository, times(1)).findById(ID1);
        verify(tagRepository, times(1)).findTagByName(TAG_NAME3);
    }

    @Test
    void deleteTag_correctWorkValidDataAndExistBooksWithTag_deleteTagFromBookAndApplyMethods() {
        //Date
        Tag tag1 = createTag1();
        Tag tag2 = createTag2();
        Set<Tag> tags1 = new HashSet<>();
        tags1.add(tag1);
        tags1.add(tag2);
        Set<Tag> tags2 = new HashSet<>();
        tags2.add(tag1);
        List<Book> books = createBookList1();
        books.get(0).setTags(tags1);
        books.get(1).setTags(tags2);
        //When
        when(tagRepository.findById(ID1)).thenReturn(Optional.of(tag1));
        when(bookRepository.findAllByTagsContains(tag1)).thenReturn(Optional.of(books));
        //Then
        tagService.deleteTag(ID1);
        Set<Tag> tags = books.get(0).getTags();

        assertTrue(tags.contains(tag2));
        assertFalse(tags.contains(tag1));
        assertFalse(books.get(1).getTags().contains(tag2));
        verify(tagRepository, times(1)).findById(ID1);
        verify(bookRepository, times(1)).findAllByTagsContains(tag1);
    }

    @Test
    void deleteTag_correctWorkValidDataAndNotExistBooksWithThisTag_applyMethods() {
        //Date
        Tag tag1 = createTag1();
        //When
        when(tagRepository.findById(ID1)).thenReturn(Optional.of(tag1));
        when(bookRepository.findAllByTagsContains(tag1)).thenReturn(Optional.empty());
        //Then
        tagService.deleteTag(ID1);

        verify(tagRepository, times(1)).findById(ID1);
        verify(bookRepository, times(1)).findAllByTagsContains(tag1);
        verify(tagRepository, times(1)).delete(tag1);
    }

    @Test
    void deleteTag_tagNotExistById_throwTagNotFoundByIdException() {
        //When
        when(tagRepository.findById(ID1)).thenReturn(Optional.empty());
        //Then
        TagNotFoundByIdException thrown = assertThrows(TagNotFoundByIdException.class, () -> {
            tagService.deleteTag(ID1);
        });

        assertEquals(thrown.getMessage(), new TagNotFoundByIdException(ID1).getMessage());
    }

    @Test
    void getExistingTagsOrThrowException_correctWork_returnTags() {
        //Date
        Tag tag1 = createTag1();
        Tag tag2 = createTag2();
        Set<TagRequestDto> tagsRequestDto1 = createTagsRequestDto();
        //When
        when(tagRepository.findTagByName(TAG_NAME1)).thenReturn(Optional.of(tag1));
        when(tagRepository.findTagByName(TAG_NAME2)).thenReturn(Optional.of(tag2));
        //Then
        Set<Tag> tags = tagService.getExistingTagsOrThrowException(tagsRequestDto1);

        assertEquals(2, tags.size());
        assertTrue(tags.contains(tag1));
        assertTrue(tags.contains(tag2));
    }

    @Test
    void getExistingTagsOrThrowException_tagsIsNull_returnEmptyHashSet() {
        //Then
        Set<Tag> tags = tagService.getExistingTagsOrThrowException(null);

        assertEquals(HashSet.class, tags.getClass());
        assertTrue(tags.isEmpty());
    }

    @Test
    void getExistingTagsOrThrowException_tagNotExistByName_throwTagNotFoundByNameException() {
        //Date
        Set<TagRequestDto> tagsRequestDto2 = createTagsRequestDto2();
        //When
        when(tagRepository.findTagByName(TAG_NAME1)).thenReturn(Optional.empty());
        //Then
        TagNotFoundByNameException thrown = assertThrows(TagNotFoundByNameException.class, () -> {
            tagService.getExistingTagsOrThrowException(tagsRequestDto2);
        });

        assertEquals(thrown.getMessage(), new TagNotFoundByNameException(TAG_NAME1).getMessage());
    }
}