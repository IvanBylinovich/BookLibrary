package com.solbeg.BookLibrary.exception;

import com.solbeg.BookLibrary.model.OrderStatus;

import java.util.Arrays;

public class OrderStatusNotFoundException extends RuntimeException {
    public OrderStatusNotFoundException(String status) {
        super(String.format("Status [%s] not found. You can use only the following statuses: %s",
                status, Arrays.toString(OrderStatus.values())));
    }
}
