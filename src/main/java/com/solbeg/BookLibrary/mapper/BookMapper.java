package com.solbeg.BookLibrary.mapper;

import com.solbeg.BookLibrary.dto.BookRequestDto;
import com.solbeg.BookLibrary.dto.BookResponseDto;
import com.solbeg.BookLibrary.model.entity.Book;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookMapper {

    private final ModelMapper modelMapper;

    public Book convertBookRequestDtoToBook(BookRequestDto bookRequestDto) {
        return modelMapper.map(bookRequestDto, Book.class);
    }

    public BookResponseDto convertBookToBookResponseDto(Book book) {
        return modelMapper.map(book, BookResponseDto.class);
    }
}
