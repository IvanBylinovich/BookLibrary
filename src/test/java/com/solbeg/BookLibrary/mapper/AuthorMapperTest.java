package com.solbeg.BookLibrary.mapper;

import com.solbeg.BookLibrary.dto.AuthorRequestDto;
import com.solbeg.BookLibrary.dto.AuthorResponseDto;
import com.solbeg.BookLibrary.model.entity.Author;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static utils.TestAuthorFactory.createAuthor1;
import static utils.TestAuthorFactory.createAuthorRequestDto1;

class AuthorMapperTest {

    private final AuthorMapper authorMapper = new AuthorMapper(new ModelMapper());

    @Test
    void convertAuthorRequestDtoToAuthor_correctWork() {
        AuthorRequestDto authorRequestDto = createAuthorRequestDto1();
        Author author = authorMapper.convertAuthorRequestDtoToAuthor(authorRequestDto);
        assertNull(author.getId());
        assertEquals(authorRequestDto.getFirstName(), author.getFirstName());
        assertEquals(authorRequestDto.getLastName(), author.getLastName());
    }

    @Test
    void convertAuthorToAuthorResponseDto_correctWork() {
        Author author = createAuthor1();
        AuthorResponseDto authorResponseDto = authorMapper.convertAuthorToAuthorResponseDto(author);
        assertEquals(author.getId(), authorResponseDto.getId());
        assertEquals(author.getFirstName(), authorResponseDto.getFirstName());
        assertEquals(author.getLastName(), authorResponseDto.getLastName());
    }
}