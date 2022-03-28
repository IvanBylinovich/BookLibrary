package com.solbeg.BookLibrary.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import static com.solbeg.BookLibrary.utils.LibraryConstants.AUTHOR_FIRST_NAME_SWAGGER_EXAMPLE;
import static com.solbeg.BookLibrary.utils.LibraryConstants.AUTHOR_LAST_NAME_SWAGGER_EXAMPLE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorRequestDto {

    @NotBlank(message = "The author's first name should not be null or empty")
    @ApiModelProperty(value = "Author's first name", example = AUTHOR_FIRST_NAME_SWAGGER_EXAMPLE, required = true)
    private String firstName;

    @NotBlank(message = "The author's last name should not be null or empty")
    @ApiModelProperty(value = "Author's last name", example = AUTHOR_LAST_NAME_SWAGGER_EXAMPLE, required = true)
    private String lastName;
}
