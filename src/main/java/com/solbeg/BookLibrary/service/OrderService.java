package com.solbeg.BookLibrary.service;

import com.solbeg.BookLibrary.dto.OrderRequestDto;
import com.solbeg.BookLibrary.dto.OrderResponseDto;
import com.solbeg.BookLibrary.dto.OrderStatusRequestDto;

import java.util.List;

public interface OrderService {

    List<OrderResponseDto> findAllOrders();

    OrderResponseDto findOrderById(String id);

    OrderResponseDto createOrder(OrderRequestDto orderRequestDto);

    OrderResponseDto updateOrder(String id, OrderRequestDto orderRequestDto);

    OrderResponseDto updateOrderStatus(String id, OrderStatusRequestDto orderStatusRequestDto);

    void deleteOrder(String id);
}
