package com.solbeg.BookLibrary.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import static com.solbeg.BookLibrary.utils.LibraryConstants.TAG_SWAGGER_EXAMPLE;

@Getter
@Setter
public class OrderStatusRequestDto {

    @NotBlank(message = "The order's status should not be null or empty")
    @ApiModelProperty(value = "Order's status", example = TAG_SWAGGER_EXAMPLE, required = true)
    private String status;
}
