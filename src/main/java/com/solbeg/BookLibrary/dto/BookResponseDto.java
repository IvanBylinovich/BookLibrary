package com.solbeg.BookLibrary.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

@Getter
@Setter
public class BookResponseDto {
    private String id;
    private String title;
    private String imageUrl;
    private BigDecimal price;
    private Set<TagResponseDto> tags;
    private AuthorResponseDto author;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
