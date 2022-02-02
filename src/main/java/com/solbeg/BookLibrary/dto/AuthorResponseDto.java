package com.solbeg.BookLibrary.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorResponseDto {

    @ApiModelProperty(value = "Author's id", example = "b234a9fa-0985-4c2e-b106-50cd0eb24ae8")
    private String id;

    @ApiModelProperty(value = "Author's first name", example = "Stephen")
    private String firstName;

    @ApiModelProperty(value = "Author's last name", example = "King")
    private String lastName;
}
