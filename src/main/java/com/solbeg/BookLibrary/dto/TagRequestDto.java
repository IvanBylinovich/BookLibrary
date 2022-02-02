package com.solbeg.BookLibrary.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class TagRequestDto {

    @NotBlank(message = "Tag's name should not be null or empty")
    @ApiModelProperty(value = "Tag's name", example = "Action", required = true)
    private String name;
}