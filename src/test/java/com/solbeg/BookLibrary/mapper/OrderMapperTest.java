package com.solbeg.BookLibrary.mapper;

import com.solbeg.BookLibrary.dto.OrderResponseDto;
import com.solbeg.BookLibrary.model.entity.Order;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestOrderFactory.createOrder1;

class OrderMapperTest {

    private final OrderMapper orderMapper = new OrderMapper(new ModelMapper());

    @Test
    void convertOrderToOrderResponseDto_correctWork() {
        Order order = createOrder1();
        OrderResponseDto orderResponseDto = orderMapper.convertOrderToOrderResponseDto(order);
        assertEquals(order.getId(), orderResponseDto.getId());
        assertEquals(order.getOrderPositions().size(), orderResponseDto.getOrderPositions().size());
        assertEquals(order.getTotalAmount(), orderResponseDto.getTotalAmount());
        assertEquals(order.getCreatedAt(), orderResponseDto.getCreatedAt());
        assertEquals(order.getUpdatedAt(), orderResponseDto.getUpdatedAt());
        assertEquals(order.getOrderStatus(), orderResponseDto.getOrderStatus());
    }
}