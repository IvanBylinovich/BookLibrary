package com.solbeg.BookLibrary.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
public class BookRequestDto {

    @NotBlank(message = "The book's title should not be null or empty")
    @ApiModelProperty(value = "Book's title", example = "The Dark Half", required = true)
    private String title;

    @URL(message = "The book's URL of image should match the url format")
    @NotBlank(message = "The book's URL of image should not be null or empty")
    @ApiModelProperty(value = "Image's URL of book",
            example = "https://en.wikipedia.org/wiki/The_Dark_Half#/media/File:Darkhalf.jpg",
            required = true)
    private String imageUrl;

    @NotNull(message = "The book's price should not be null")
    @DecimalMin(value = "0.00", message = "The book's price should be positive")
    @ApiModelProperty(value = "Book's price", example = "9.99", required = true)
    private BigDecimal price;

    @Valid
    @ApiModelProperty(value = "Book's genres")
    private Set<TagRequestDto> tags;

    @NotNull(message = "The book's author should not be null")
    @Valid
    @ApiModelProperty(value = "Book's author", required = true)
    private AuthorRequestDto author;
}
