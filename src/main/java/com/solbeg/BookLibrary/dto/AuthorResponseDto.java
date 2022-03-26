package com.solbeg.BookLibrary.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.solbeg.BookLibrary.utils.LibraryConstants.AUTHOR_FIRST_NAME_SWAGGER_EXAMPLE;
import static com.solbeg.BookLibrary.utils.LibraryConstants.AUTHOR_LAST_NAME_SWAGGER_EXAMPLE;
import static com.solbeg.BookLibrary.utils.LibraryConstants.ID_SWAGGER_EXAMPLE;

@Getter
@Setter
public class AuthorResponseDto {

    @ApiModelProperty(value = "Author's id", example = ID_SWAGGER_EXAMPLE)
    private String id;

    @ApiModelProperty(value = "Author's first name", example = AUTHOR_FIRST_NAME_SWAGGER_EXAMPLE)
    private String firstName;

    @ApiModelProperty(value = "Author's last name", example = AUTHOR_LAST_NAME_SWAGGER_EXAMPLE)
    private String lastName;
}
