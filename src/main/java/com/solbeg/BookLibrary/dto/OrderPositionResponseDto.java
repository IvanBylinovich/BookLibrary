package com.solbeg.BookLibrary.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.solbeg.BookLibrary.utils.LibraryConstants.ID_SWAGGER_EXAMPLE;
import static com.solbeg.BookLibrary.utils.LibraryConstants.QUANTITY_SWAGGER_EXAMPLE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderPositionResponseDto {

    @ApiModelProperty(value = "Ordered position's id", example = ID_SWAGGER_EXAMPLE)
    private String id;

    @ApiModelProperty(value = "Ordered book")
    private OrderedBookResponseDto orderedBook;

    @ApiModelProperty(value = "Ordered book's quantity", example = QUANTITY_SWAGGER_EXAMPLE)
    private int quantity;
}
