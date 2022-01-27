package com.solbeg.BookLibrary.dto;

import com.solbeg.BookLibrary.model.Tag;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Getter
@Setter
public class BookResponseDto {
    private String id;
    private String title;
    private String imageUrl;
    private BigDecimal price;
    private Tag tag;
    private AuthorResponseDto author;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
