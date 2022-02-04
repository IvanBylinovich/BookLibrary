package com.solbeg.BookLibrary.mapper;

import com.solbeg.BookLibrary.dto.OrderResponseDto;
import com.solbeg.BookLibrary.model.entity.Order;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final ModelMapper modelMapper;

    public OrderResponseDto convertOrderToOrderResponseDto(Order order) {
        return modelMapper.map(order, OrderResponseDto.class);
    }
}
