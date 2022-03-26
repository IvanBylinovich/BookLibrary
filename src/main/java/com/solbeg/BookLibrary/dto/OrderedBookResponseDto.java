package com.solbeg.BookLibrary.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static com.solbeg.BookLibrary.utils.LibraryConstants.AUTHOR_FIRST_NAME_SWAGGER_EXAMPLE;
import static com.solbeg.BookLibrary.utils.LibraryConstants.AUTHOR_LAST_NAME_SWAGGER_EXAMPLE;
import static com.solbeg.BookLibrary.utils.LibraryConstants.DATE_TIME_SWAGGER_EXAMPLE;
import static com.solbeg.BookLibrary.utils.LibraryConstants.ID_SWAGGER_EXAMPLE;
import static com.solbeg.BookLibrary.utils.LibraryConstants.PRICE_SWAGGER_EXAMPLE;
import static com.solbeg.BookLibrary.utils.LibraryConstants.TAGS_SWAGGER_EXAMPLE;
import static com.solbeg.BookLibrary.utils.LibraryConstants.TITLE_SWAGGER_EXAMPLE;
import static com.solbeg.BookLibrary.utils.LibraryConstants.URL_SWAGGER_EXAMPLE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderedBookResponseDto {

    @ApiModelProperty(value = "Ordered book's id", example = ID_SWAGGER_EXAMPLE)
    private String id;

    @ApiModelProperty(value = "Book's title", example = TITLE_SWAGGER_EXAMPLE)
    private String title;

    @ApiModelProperty(value = "Image's URL of book", example = URL_SWAGGER_EXAMPLE)
    private String imageUrl;

    @ApiModelProperty(value = "Book's price", example = PRICE_SWAGGER_EXAMPLE)
    private BigDecimal price;

    @ApiModelProperty(value = "Book's genres", example = TAGS_SWAGGER_EXAMPLE)
    private String tags;

    @ApiModelProperty(value = "Author's first name", example = AUTHOR_FIRST_NAME_SWAGGER_EXAMPLE)
    private String authorFirstName;

    @ApiModelProperty(value = "Author's last name", example = AUTHOR_LAST_NAME_SWAGGER_EXAMPLE)
    private String authorLastName;

    @ApiModelProperty(value = "Book's creation time", example = DATE_TIME_SWAGGER_EXAMPLE)
    private ZonedDateTime createdAt;

    @ApiModelProperty(value = "Time of the last modification of the book", example = DATE_TIME_SWAGGER_EXAMPLE)
    private ZonedDateTime updatedAt;
}
