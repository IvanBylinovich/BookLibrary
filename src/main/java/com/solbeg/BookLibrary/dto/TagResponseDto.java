package com.solbeg.BookLibrary.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagResponseDto {

    @ApiModelProperty(value = "Tag's id", example = "b234a9fb-0985-4c2e-b106-50cd0eb24ae8")
    private String id;

    @ApiModelProperty(value = "Tag's name", example = "Action")
    private String name;
}
