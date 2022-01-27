package com.solbeg.BookLibrary.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AuthorRequestDto {

    @NotBlank(message = "The author's first name should not be null or empty")
    private String firstName;

    @NotBlank(message = "The author's last name should not be null or empty")
    private String lastName;
}
