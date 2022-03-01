package com.solbeg.BookLibrary.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import static com.solbeg.BookLibrary.utils.LibraryConstants.ID_SWAGGER_EXAMPLE;
import static com.solbeg.BookLibrary.utils.LibraryConstants.USERNAME_SWAGGER_EXAMPLE;
import static com.solbeg.BookLibrary.utils.LibraryConstants.USER_FIRST_NAME_SWAGGER_EXAMPLE;
import static com.solbeg.BookLibrary.utils.LibraryConstants.USER_LAST_FIRST_NAME_SWAGGER_EXAMPLE;

@Getter
@Setter
public class UserResponseDto {

    @ApiModelProperty(value = "User's id", example = ID_SWAGGER_EXAMPLE)
    private String id;

    @ApiModelProperty(value = "Username", example = USERNAME_SWAGGER_EXAMPLE)
    private String username;

    @ApiModelProperty(value = "User's first name", example = USER_FIRST_NAME_SWAGGER_EXAMPLE)
    private String firstName;

    @ApiModelProperty(value = "User's last name", example = USER_LAST_FIRST_NAME_SWAGGER_EXAMPLE)
    private String lastName;
}
