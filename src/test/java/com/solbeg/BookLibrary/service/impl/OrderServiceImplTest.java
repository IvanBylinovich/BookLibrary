package com.solbeg.BookLibrary.service.impl;

import com.solbeg.BookLibrary.dto.OrderPositionResponseDto;
import com.solbeg.BookLibrary.dto.OrderRequestDto;
import com.solbeg.BookLibrary.dto.OrderResponseDto;
import com.solbeg.BookLibrary.dto.OrderStatusRequestDto;
import com.solbeg.BookLibrary.dto.OrderedBookResponseDto;
import com.solbeg.BookLibrary.exception.DuplicateOrderPositionsException;
import com.solbeg.BookLibrary.exception.OrderNotDraftException;
import com.solbeg.BookLibrary.exception.OrderNotFoundByIdException;
import com.solbeg.BookLibrary.exception.OrderStatusNotFoundException;
import com.solbeg.BookLibrary.model.OrderStatus;
import com.solbeg.BookLibrary.model.entity.Book;
import com.solbeg.BookLibrary.model.entity.Order;
import com.solbeg.BookLibrary.model.entity.User;
import com.solbeg.BookLibrary.repository.BookRepository;
import com.solbeg.BookLibrary.repository.OrderPositionRepository;
import com.solbeg.BookLibrary.repository.OrderRepository;
import com.solbeg.BookLibrary.repository.OrderedBookRepository;
import com.solbeg.BookLibrary.repository.UserRepository;
import com.solbeg.BookLibrary.service.BookService;
import com.solbeg.BookLibrary.service.OrderService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static utils.LibraryTestConstants.BIG_DECIMAL_TOTAL_AMOUNT1;
import static utils.LibraryTestConstants.DATE_ZONED_DATA_TIME1;
import static utils.LibraryTestConstants.DATE_ZONED_DATA_TIME2;
import static utils.LibraryTestConstants.FIRST_NAME1;
import static utils.LibraryTestConstants.ID1;
import static utils.LibraryTestConstants.ID2;
import static utils.LibraryTestConstants.LAST_NAME1;
import static utils.LibraryTestConstants.NOT_VALID_STATUS_NAME;
import static utils.LibraryTestConstants.PRICE_BIG_DECIMAL1;
import static utils.LibraryTestConstants.TITLE1;
import static utils.LibraryTestConstants.URL1;
import static utils.TestBookFactory.createBook1;
import static utils.TestBookFactory.createBook2;
import static utils.TestOrderFactory.createOrder1;
import static utils.TestOrderFactory.createOrder2;
import static utils.TestOrderFactory.createOrderRequestDtoDto1;
import static utils.TestOrderFactory.createOrderRequestDtoDto2;
import static utils.TestOrderFactory.createOrderRequestDtoWithDuplicate;
import static utils.TestUserFactory.createUser1;

@SpringBootTest
class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private OrderedBookRepository orderedBookRepository;

    @MockBean
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private OrderPositionRepository orderPositionRepository;

    @Test
    void findAllOrders_correctWork_returnOrderResponseDtoList() {
        //Date
        List<Order> orders = new ArrayList<>();
        orders.add(createOrder1());
        orders.add(createOrder2());
        //When
        when(orderRepository.findAll()).thenReturn(orders);
        //Then
        List<OrderResponseDto> ordersResponseDto = orderService.findAllOrders();
        OrderResponseDto orderResponseDto1 = ordersResponseDto.get(0);
        OrderResponseDto orderResponseDto2 = ordersResponseDto.get(1);

        assertEquals(ID1, orderResponseDto1.getId());
        assertEquals(2, orderResponseDto1.getOrderPositions().size());
        assertEquals(BIG_DECIMAL_TOTAL_AMOUNT1, orderResponseDto1.getTotalAmount());
        assertEquals(DATE_ZONED_DATA_TIME1, orderResponseDto1.getCreatedAt());
        assertEquals(DATE_ZONED_DATA_TIME1, orderResponseDto1.getCreatedAt());
        assertEquals(OrderStatus.DRAFT, orderResponseDto1.getOrderStatus());

        assertEquals(ID2, orderResponseDto2.getId());
        assertEquals(2, orderResponseDto2.getOrderPositions().size());
        assertEquals(PRICE_BIG_DECIMAL1, orderResponseDto2.getTotalAmount());
        assertEquals(DATE_ZONED_DATA_TIME2, orderResponseDto2.getCreatedAt());
        assertEquals(DATE_ZONED_DATA_TIME2, orderResponseDto2.getCreatedAt());
        assertEquals(OrderStatus.DRAFT, orderResponseDto2.getOrderStatus());
    }

    @Test
    void findAllOrders_correctWork_returnEmptyList() {
        //When
        when(orderRepository.findAll()).thenReturn(Lists.emptyList());
        //Then
        List<OrderResponseDto> ordersResponseDto = orderService.findAllOrders();

        assertTrue(ordersResponseDto.isEmpty());
    }

    @Test
    void findOrderById_correctWork_returnOrderResponseDto() {
        //Date
        Order order = createOrder1();
        //When
        when(orderRepository.findById(ID1)).thenReturn(Optional.of(order));
        //Then
        OrderResponseDto orderResponseDto = orderService.findOrderById(ID1);

        assertEquals(ID1, orderResponseDto.getId());
        assertEquals(2, orderResponseDto.getOrderPositions().size());
        assertEquals(BIG_DECIMAL_TOTAL_AMOUNT1, orderResponseDto.getTotalAmount());
        assertEquals(DATE_ZONED_DATA_TIME1, orderResponseDto.getCreatedAt());
        assertEquals(DATE_ZONED_DATA_TIME1, orderResponseDto.getCreatedAt());
        assertEquals(OrderStatus.DRAFT, orderResponseDto.getOrderStatus());
    }

    @Test
    void findOrderById_orderNotExistById_throwOrderNotFoundByIdException() {
        //When
        when(orderRepository.findById(ID1)).thenReturn(Optional.empty());
        //Then
        OrderNotFoundByIdException thrown = assertThrows(OrderNotFoundByIdException.class, () -> {
            orderService.findOrderById(ID1);
        });

        assertEquals(new OrderNotFoundByIdException(ID1).getMessage(), thrown.getMessage());
    }

    @Test
    void createOrder_correctWork_returnOrderResponseDto() {
        //Date
        Book book1 = createBook1();
        Book book2 = createBook2();
        User user = createUser1();
        OrderRequestDto orderRequestDto = createOrderRequestDtoDto1();
        //When
        when(bookService.findBookOrThrowException(ID1)).thenReturn(book1);
        when(bookService.findBookOrThrowException(ID2)).thenReturn(book2);
        when(userRepository.findById(ID1)).thenReturn(Optional.of(user));
        //Then
        OrderResponseDto orderResponseDto = orderService.createOrder(orderRequestDto);

        assertEquals(2, orderResponseDto.getOrderPositions().size());
        assertEquals(BigDecimal.valueOf(Double.parseDouble("7.97")), orderResponseDto.getTotalAmount());
        assertEquals(OrderStatus.DRAFT, orderResponseDto.getOrderStatus());
    }

    @Test
    void createOrder_orderPositionsHasDuplicates_throwDuplicateOrderPositionsException() {
        //Date
        OrderRequestDto orderRequestDto = createOrderRequestDtoWithDuplicate();
        //When
        when(userRepository.findById(ID1)).thenReturn(Optional.of(createUser1()));
        //Then
        DuplicateOrderPositionsException thrown = assertThrows(DuplicateOrderPositionsException.class, () -> {
            orderService.createOrder(orderRequestDto);
        });

        assertEquals(new DuplicateOrderPositionsException(Set.of(ID1)).getMessage(), thrown.getMessage());
    }

    @Test
    void updateOrder_correctWork_returnUpdatedOrderResponseDto() {
        //Date
        User user = createUser1();
        Order order = createOrder1();
        Book book1 = createBook1();
        OrderRequestDto orderRequestDto = createOrderRequestDtoDto2();
        //When
        when(orderRepository.findById(ID1)).thenReturn(Optional.of(order));
        when(bookService.findBookOrThrowException(ID1)).thenReturn(book1);
        when(userRepository.findById(ID1)).thenReturn(Optional.of(user));
        //Then
        OrderResponseDto orderResponseDto = orderService.updateOrder(ID1, orderRequestDto);
        OrderPositionResponseDto orderPosition = orderResponseDto.getOrderPositions().get(0);
        OrderedBookResponseDto book = orderPosition.getOrderedBook();

        verify(orderPositionRepository, times(1)).deleteAll(any());
        verify(orderPositionRepository, times(1)).save(any());
        verify(orderedBookRepository, times(1)).deleteById(ID1);
        verify(orderedBookRepository, times(1)).deleteById(ID2);
        assertEquals(1, orderResponseDto.getOrderPositions().size());
        assertEquals(TITLE1, book.getTitle());
        assertEquals(FIRST_NAME1, book.getAuthorFirstName());
        assertEquals(LAST_NAME1, book.getAuthorLastName());
        assertEquals(URL1, book.getImageUrl());
        assertEquals(PRICE_BIG_DECIMAL1, book.getPrice());
        assertEquals(DATE_ZONED_DATA_TIME1, book.getUpdatedAt());
        assertEquals(DATE_ZONED_DATA_TIME1, book.getCreatedAt());
        assertEquals(BigDecimal.valueOf(Double.parseDouble("1.99")), orderResponseDto.getTotalAmount());
        assertEquals(DATE_ZONED_DATA_TIME1, orderResponseDto.getCreatedAt());
        assertNotEquals(DATE_ZONED_DATA_TIME1, orderResponseDto.getUpdatedAt());
        assertEquals(OrderStatus.DRAFT, orderResponseDto.getOrderStatus());
    }

    @Test
    void updateOrder_notExistOrderById_throwOrderNotFoundByIdException() {
        //When
        when(bookRepository.findById(ID1)).thenReturn(Optional.empty());
        //Then
        OrderNotFoundByIdException thrown = assertThrows(OrderNotFoundByIdException.class, () -> {
            orderService.updateOrder(ID1, new OrderRequestDto());
        });

        assertEquals(new OrderNotFoundByIdException(ID1).getMessage(), thrown.getMessage());
    }

    @Test
    void updateOrder_orderStatusNotDraft_throwOrderNotDraftException() {
        //Date
        User user = createUser1();
        Order order = createOrder1();
        order.setOrderStatus(OrderStatus.PAID);
        OrderRequestDto orderRequestDto = createOrderRequestDtoDto1();
        //When
        when(orderRepository.findById(ID1)).thenReturn(Optional.of(order));
        when(orderRepository.findById(ID1)).thenReturn(Optional.of(order));
        when(userRepository.findById(ID1)).thenReturn(Optional.of(user));
        //Then
        OrderNotDraftException thrown = assertThrows(OrderNotDraftException.class, () -> {
            orderService.updateOrder(ID1, orderRequestDto);
        });

        assertEquals(new OrderNotDraftException(ID1).getMessage(), thrown.getMessage());
    }

    @Test
    void updateOrderStatus_correctWork_returnOrderResponseDto() {
        //Date
        Order order = createOrder1();
        OrderStatusRequestDto orderStatusRequestDto = new OrderStatusRequestDto(OrderStatus.PAID.name());
        //When
        when(orderRepository.findById(ID1)).thenReturn(Optional.of(order));
        //Then
        OrderResponseDto orderResponseDto = orderService.updateOrderStatus(ID1, orderStatusRequestDto);

        assertEquals(OrderStatus.PAID, orderResponseDto.getOrderStatus());
    }

    @Test
    void updateOrderStatus_notExistOrderById_throwOrderNotFoundByIdException() {
        //Date
        OrderStatusRequestDto orderStatusRequestDto = new OrderStatusRequestDto(OrderStatus.PAID.name());
        //When
        when(orderRepository.findById(ID1)).thenReturn(Optional.empty());
        //Then
        OrderNotFoundByIdException thrown = assertThrows(OrderNotFoundByIdException.class, () -> {
            orderService.updateOrderStatus(ID1, orderStatusRequestDto);
        });

        assertEquals(new OrderNotFoundByIdException(ID1).getMessage(), thrown.getMessage());
    }

    @Test
    void updateOrderStatus_invalidStatusName_throwOrderStatusNotFoundException() {
        //Date
        OrderStatusRequestDto orderStatusRequestDto = new OrderStatusRequestDto(NOT_VALID_STATUS_NAME);
        //Then
        OrderStatusNotFoundException thrown = assertThrows(OrderStatusNotFoundException.class, () -> {
            orderService.updateOrderStatus(ID1, orderStatusRequestDto);
        });

        assertEquals(new OrderStatusNotFoundException(NOT_VALID_STATUS_NAME).getMessage(), thrown.getMessage());
    }

    @Test
    void deleteOrder_correctWork() {
        //Date
        Order order = createOrder1();
        //When
        when(orderRepository.findById(ID1)).thenReturn(Optional.of(order));
        //Then
        orderService.deleteOrder(ID1);

        verify(orderPositionRepository, times(1)).deleteAll(order.getOrderPositions());
        verify(orderedBookRepository, times(1)).deleteById(ID1);
        verify(orderedBookRepository, times(1)).deleteById(ID2);
        verify(orderRepository, times(1)).delete(order);
    }

    @Test
    void deleteOrder_notExistOrderById_throwOrderNotFoundByIdException() {
        //When
        when(bookRepository.findById(ID1)).thenReturn(Optional.empty());
        //Then
        OrderNotFoundByIdException thrown = assertThrows(OrderNotFoundByIdException.class, () -> {
            orderService.deleteOrder(ID1);
        });

        assertEquals(new OrderNotFoundByIdException(ID1).getMessage(), thrown.getMessage());
    }

    @Test
    void deleteOrder_orderStatusNotDraft_throwOrderNotDraftException() {
        //Date
        Order order = createOrder1();
        order.setOrderStatus(OrderStatus.PAID);
        //When
        when(orderRepository.findById(ID1)).thenReturn(Optional.of(order));
        //Then
        OrderNotDraftException thrown = assertThrows(OrderNotDraftException.class, () -> {
            orderService.deleteOrder(ID1);
        });

        assertEquals(new OrderNotDraftException(ID1).getMessage(), thrown.getMessage());
    }
}