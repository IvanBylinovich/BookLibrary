package com.solbeg.BookLibrary.mapper;

import com.solbeg.BookLibrary.dto.BookRequestDto;
import com.solbeg.BookLibrary.dto.BookResponseDto;
import com.solbeg.BookLibrary.dto.TagResponseDto;
import com.solbeg.BookLibrary.model.entity.Book;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static utils.LibraryTestConstants.TAG_NAME1;
import static utils.LibraryTestConstants.TAG_NAME2;
import static utils.TestBookFactory.createBook1;
import static utils.TestBookFactory.createBookRequestDto1;

class BookMapperTest {

    private final BookMapper bookMapper = new BookMapper(new ModelMapper());

    @Test
    void convertBookRequestDtoToBook_correctWork() {
        Book book = createBook1();
        BookResponseDto bookResponseDto = bookMapper.convertBookToBookResponseDto(book);
        List<String> tagsNames = bookResponseDto.getTags().stream().map(TagResponseDto::getName).toList();
        assertEquals(book.getId(), bookResponseDto.getId());
        assertEquals(book.getTitle(), bookResponseDto.getTitle());
        assertEquals(book.getImageUrl(), bookResponseDto.getImageUrl());
        assertEquals(book.getPrice(), bookResponseDto.getPrice());
        assertEquals(ZonedDateTime.class, bookResponseDto.getCreatedAt().getClass());
        assertEquals(ZonedDateTime.class, bookResponseDto.getUpdatedAt().getClass());
        assertEquals(2, bookResponseDto.getTags().size());
        assertTrue(tagsNames.contains(TAG_NAME1));
        assertTrue(tagsNames.contains(TAG_NAME2));
    }

    @Test
    void convertBookToBookResponseDto_correctWork() {
        BookRequestDto bookRequestDto = createBookRequestDto1();
        Book book = bookMapper.convertBookRequestDtoToBook(bookRequestDto);
        assertNull(book.getId());
        assertEquals(bookRequestDto.getTitle(), book.getTitle());
        assertEquals(bookRequestDto.getImageUrl(), book.getImageUrl());
        assertEquals(bookRequestDto.getPrice(), book.getPrice());
        assertEquals(bookRequestDto.getTags().size(), book.getTags().size());
    }
}