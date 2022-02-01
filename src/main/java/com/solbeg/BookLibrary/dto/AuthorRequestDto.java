package com.solbeg.BookLibrary.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AuthorRequestDto {

    @NotBlank(message = "The author's first name should not be null or empty")
    @ApiModelProperty(value = "Author's first name", example = "Stephen", required = true)
    private String firstName;

    @NotBlank(message = "The author's last name should not be null or empty")
    @ApiModelProperty(value = "Author's last name", example = "King", required = true)
    private String lastName;
}
