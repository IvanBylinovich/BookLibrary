package com.solbeg.BookLibrary.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import static com.solbeg.BookLibrary.utils.LibraryConstants.TAG_SWAGGER_EXAMPLE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TagRequestDto {

    @NotBlank(message = "Tag's name should not be null or empty")
    @ApiModelProperty(value = "Tag's name", example = TAG_SWAGGER_EXAMPLE, required = true)
    private String name;
}