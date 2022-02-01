package com.solbeg.BookLibrary.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;

@Getter
@Setter
public class BookResponseDto {

    @ApiModelProperty(value = "Book's id", example = "b234a9fb-0985-4c2e-b106-50cd0eb24ae8")
    private String id;

    @ApiModelProperty(value = "Book's title", example = "The Dark Half")
    private String title;

    @ApiModelProperty(value = "Image's URL of book",
            example = "https://en.wikipedia.org/wiki/The_Dark_Half#/media/File:Darkhalf.jpg")
    private String imageUrl;

    @ApiModelProperty(value = "Book's price", example = "9.99")
    private BigDecimal price;

    @ApiModelProperty(value = "Book's genres")
    private Set<TagResponseDto> tags;

    @ApiModelProperty(value = "Book's author")
    private AuthorResponseDto author;

    @ApiModelProperty(value = "Book's creation time", example = "2022-02-01T13:15:10.4609297+03:00")
    private ZonedDateTime createdAt;

    @ApiModelProperty(value = "Time of the last modification of the book", example = "2022-02-01T13:11:10.4609297+03:00")
    private ZonedDateTime updatedAt;
}
