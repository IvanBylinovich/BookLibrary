package com.solbeg.BookLibrary.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.solbeg.BookLibrary.utils.LibraryConstants.ID_SWAGGER_EXAMPLE;

@Getter
@Setter
public class OrderRequestDto {

    @NotBlank(message = "User's id should not be null or empty")
    @ApiModelProperty(value = "User's id", example = ID_SWAGGER_EXAMPLE, required = true)
    private String userId;

    @NotNull(message = "The order's positions should not be null")
    @Valid
    @ApiModelProperty(value = "Ordered positions", required = true)
    private List<OrderPositionRequestDto> orderPositions;
}
