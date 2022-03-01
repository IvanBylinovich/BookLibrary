package com.solbeg.BookLibrary.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.solbeg.BookLibrary.utils.LibraryConstants.PASSWORD_SWAGGER_EXAMPLE;
import static com.solbeg.BookLibrary.utils.LibraryConstants.USERNAME_SWAGGER_EXAMPLE;
import static com.solbeg.BookLibrary.utils.LibraryConstants.USER_FIRST_NAME_SWAGGER_EXAMPLE;
import static com.solbeg.BookLibrary.utils.LibraryConstants.USER_LAST_FIRST_NAME_SWAGGER_EXAMPLE;

@Getter
@Setter
public class UserRequestDto {

    @NotBlank(message = "Username should not be null or empty")
    @Email(message = "Username should be an email")
    @ApiModelProperty(value = "Username", example = USERNAME_SWAGGER_EXAMPLE, required = true)
    private String username;

    @NotBlank(message = "Password should not be null or empty")
    @Size(min = 8)
    @ApiModelProperty(value = "User's password", example = PASSWORD_SWAGGER_EXAMPLE, required = true)
    private String password;

    @NotBlank(message = "First name should not be null or empty")
    @Size(min = 2)
    @ApiModelProperty(value = "User's first name", example = USER_FIRST_NAME_SWAGGER_EXAMPLE, required = true)
    private String firstName;

    @NotBlank(message = "Last name should not be null or empty")
    @Size(min = 2)
    @ApiModelProperty(value = "User's last name", example = USER_LAST_FIRST_NAME_SWAGGER_EXAMPLE, required = true)
    private String lastName;
}
