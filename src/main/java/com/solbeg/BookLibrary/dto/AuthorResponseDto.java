package com.solbeg.BookLibrary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorResponseDto {
    private String id;
    private String firstName;
    private String lastName;


}
