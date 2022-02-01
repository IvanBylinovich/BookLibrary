package com.solbeg.BookLibrary.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class TagRequestDto {

    @NotBlank(message = "Tag's name should not be null or empty")
    private String name;
}