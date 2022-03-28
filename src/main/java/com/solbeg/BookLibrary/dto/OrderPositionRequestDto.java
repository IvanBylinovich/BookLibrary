package com.solbeg.BookLibrary.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.solbeg.BookLibrary.utils.LibraryConstants.ID_SWAGGER_EXAMPLE;
import static com.solbeg.BookLibrary.utils.LibraryConstants.QUANTITY_SWAGGER_EXAMPLE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderPositionRequestDto {

    @NotBlank(message = "The book's id should not be null or empty")
    @ApiModelProperty(value = "Ordered book's id", example = ID_SWAGGER_EXAMPLE, required = true)
    private String bookId;

    @NotNull(message = "The quantity in order's position should not be null")
    @Min(value = 1, message = "The quantity of books in the order should not be less than one")
    @ApiModelProperty(value = "Ordered book's quantity", example = QUANTITY_SWAGGER_EXAMPLE, required = true)
    private int quantity;
}

