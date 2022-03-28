package com.solbeg.BookLibrary.dto;

import com.solbeg.BookLibrary.model.OrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import static com.solbeg.BookLibrary.utils.LibraryConstants.DATE_TIME_SWAGGER_EXAMPLE;
import static com.solbeg.BookLibrary.utils.LibraryConstants.ID_SWAGGER_EXAMPLE;
import static com.solbeg.BookLibrary.utils.LibraryConstants.TOTAL_AMOUNT_SWAGGER_EXAMPLE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {

    @ApiModelProperty(value = "Order's id", example = ID_SWAGGER_EXAMPLE)
    private String id;

    @ApiModelProperty(value = "User who made the order")
    private UserResponseDto user;

    @ApiModelProperty(value = "Order's positions")
    private List<OrderPositionResponseDto> orderPositions;

    @ApiModelProperty(value = "Order's amount", example = TOTAL_AMOUNT_SWAGGER_EXAMPLE)
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "Order's creation time", example = DATE_TIME_SWAGGER_EXAMPLE)
    private ZonedDateTime createdAt;

    @ApiModelProperty(value = "Time of the last modification of the order", example = DATE_TIME_SWAGGER_EXAMPLE)
    private ZonedDateTime updatedAt;

    @ApiModelProperty(value = "Order's status")
    private OrderStatus orderStatus;
}
