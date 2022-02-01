package com.solbeg.BookLibrary.dto;

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
    private String title;

    @URL(message = "The book's URL of image should match the url format")
    @NotBlank(message = "The book's URL of image should not be null or empty")
    private String imageUrl;

    @NotNull(message = "The book's price should not be null")
    @DecimalMin(value = "0.00", message = "The book's price should be positive")
    private BigDecimal price;

    @NotNull(message = "The book's tags should not be null")
    @Valid
    private Set<TagRequestDto> tags;

    @NotNull(message = "The book's author should not be null")
    @Valid
    private AuthorRequestDto author;
}
