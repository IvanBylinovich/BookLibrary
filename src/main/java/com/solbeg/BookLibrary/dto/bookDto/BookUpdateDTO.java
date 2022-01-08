package com.solbeg.BookLibrary.dto.bookDto;

import com.solbeg.BookLibrary.entity.Tag;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookUpdateDTO {
    private String title;
    private String imageUrl;
    private BigDecimal price;
    private Tag tag;
    private long bookId;
    private long authorId;


}
