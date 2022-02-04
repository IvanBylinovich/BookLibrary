package com.solbeg.BookLibrary.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class OrderRequestDto {

    @NotNull(message = "The order's positions should not be null")
    @Valid
    @ApiModelProperty(value = "Ordered positions", required = true)
    private List<OrderPositionRequestDto> orderPositions;
}
