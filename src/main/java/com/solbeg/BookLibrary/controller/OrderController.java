package com.solbeg.BookLibrary.controller;

import com.solbeg.BookLibrary.dto.OrderRequestDto;
import com.solbeg.BookLibrary.dto.OrderResponseDto;
import com.solbeg.BookLibrary.dto.OrderStatusRequestDto;
import com.solbeg.BookLibrary.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static com.solbeg.BookLibrary.config.SwaggerConfig.ORDER_SERVICE_SWAGGER_TAG;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Api(tags = {ORDER_SERVICE_SWAGGER_TAG})
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/allOrders")
    @ApiOperation(value = "Get all orders", notes = "Provide method to get all orders", response = ResponseEntity.class)
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        return ResponseEntity.ok().body(orderService.findAllOrders());
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get order by id", notes = "Provide method to get order", response = ResponseEntity.class)
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable String id) {
        return ResponseEntity.ok().body(orderService.findOrderById(id));
    }

    @PostMapping
    @ApiOperation(value = "Create order", notes = "Provide method to create order", response = ResponseEntity.class)
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody @Valid OrderRequestDto orderRequestDto) {
        return ResponseEntity.ok().body(orderService.createOrder(orderRequestDto));
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update order", notes = "Provide method to update order", response = ResponseEntity.class)
    public ResponseEntity<OrderResponseDto> updateOrder(@PathVariable String id, @RequestBody @Valid OrderRequestDto orderRequestDto) {
        return ResponseEntity.ok().body(orderService.updateOrder(id, orderRequestDto));
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "Update order's status", notes = "Provide method to update order's status", response = ResponseEntity.class)
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@PathVariable String id, @RequestBody @Valid OrderStatusRequestDto orderStatusRequestDto) {
        return ResponseEntity.ok().body(orderService.updateOrderStatus(id, orderStatusRequestDto));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete order", notes = "Provide method to delete order")
    public void deleteOrder(@PathVariable String id) {
        orderService.deleteOrder(id);
    }
}
