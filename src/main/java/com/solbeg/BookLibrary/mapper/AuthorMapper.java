package com.solbeg.BookLibrary.mapper;

import com.solbeg.BookLibrary.dto.AuthorRequestDto;
import com.solbeg.BookLibrary.dto.AuthorResponseDto;
import com.solbeg.BookLibrary.model.entity.Author;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorMapper {

    private final ModelMapper modelMapper;

    public Author convertAuthorRequestDtoToAuthor(AuthorRequestDto authorRequestDto) {
        return modelMapper.map(authorRequestDto, Author.class);
    }

    public AuthorResponseDto convertAuthorToAuthorResponseDto(Author author) {
        return modelMapper.map(author, AuthorResponseDto.class);
    }
}
