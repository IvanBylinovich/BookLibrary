package com.solbeg.BookLibrary.service.impl;

import com.solbeg.BookLibrary.dto.OrderPositionRequestDto;
import com.solbeg.BookLibrary.dto.OrderRequestDto;
import com.solbeg.BookLibrary.dto.OrderResponseDto;
import com.solbeg.BookLibrary.dto.OrderStatusRequestDto;
import com.solbeg.BookLibrary.exception.DuplicateOrderPositionsException;
import com.solbeg.BookLibrary.exception.OrderNotDraftException;
import com.solbeg.BookLibrary.exception.OrderNotFoundByIdException;
import com.solbeg.BookLibrary.exception.OrderStatusNotFoundException;
import com.solbeg.BookLibrary.mapper.OrderMapper;
import com.solbeg.BookLibrary.model.OrderStatus;
import com.solbeg.BookLibrary.model.entity.Book;
import com.solbeg.BookLibrary.model.entity.Order;
import com.solbeg.BookLibrary.model.entity.OrderPosition;
import com.solbeg.BookLibrary.model.entity.OrderedBook;
import com.solbeg.BookLibrary.model.entity.Tag;
import com.solbeg.BookLibrary.model.entity.User;
import com.solbeg.BookLibrary.repository.OrderPositionRepository;
import com.solbeg.BookLibrary.repository.OrderRepository;
import com.solbeg.BookLibrary.repository.OrderedBookRepository;
import com.solbeg.BookLibrary.service.BookService;
import com.solbeg.BookLibrary.service.OrderService;
import com.solbeg.BookLibrary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.solbeg.BookLibrary.utils.LibraryConstants.COMMA_DELIMITER;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderedBookRepository orderedBookRepository;
    private final OrderPositionRepository orderPositionRepository;
    private final BookService bookService;
    private final OrderMapper orderMapper;
    private final UserService userService;

    @Transactional(readOnly = true)
    @Override
    public List<OrderResponseDto> findAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::convertOrderToOrderResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public OrderResponseDto findOrderById(String id) {
        return orderMapper.convertOrderToOrderResponseDto(findOrderOrThrowException(id));
    }

    @Transactional
    @Override
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        User user = userService.findUserByIdOrThrowException(orderRequestDto.getUserId());
        List<OrderPosition> orderPositions = createOrderPositions(orderRequestDto);
        Order order = new Order();
        order.setUser(user);
        order.setOrderPositions(orderPositions);
        BigDecimal totalAmount = countOrderTotalAmount(orderPositions);
        order.setTotalAmount(totalAmount);
        order.setCreatedAt(ZonedDateTime.now());
        order.setUpdatedAt(ZonedDateTime.now());
        order.setOrderStatus(OrderStatus.DRAFT);
        orderRepository.save(order);
        return orderMapper.convertOrderToOrderResponseDto(order);
    }

    @Transactional
    @Override
    public OrderResponseDto updateOrder(String id, OrderRequestDto orderRequestDto) {
        Order order = findOrderOrThrowException(id);
        User user = userService.findUserByIdOrThrowException(orderRequestDto.getUserId());
        if (order.getOrderStatus() != OrderStatus.DRAFT) {
            throw new OrderNotDraftException(id);
        }
        deleteOrderPositions(order);
        List<OrderPosition> orderPositions = createOrderPositions(orderRequestDto);
        order.setUser(user);
        order.setOrderPositions(orderPositions);
        BigDecimal totalAmount = countOrderTotalAmount(orderPositions);
        order.setTotalAmount(totalAmount);
        order.setUpdatedAt(ZonedDateTime.now());
        order.setOrderStatus(OrderStatus.DRAFT);
        orderRepository.save(order);
        return orderMapper.convertOrderToOrderResponseDto(order);
    }

    @Transactional
    @Override
    public OrderResponseDto updateOrderStatus(String id, OrderStatusRequestDto orderStatusRequestDto) {
        if (OrderStatus.validationStatusName(orderStatusRequestDto.getStatus())) {
            throw new OrderStatusNotFoundException(orderStatusRequestDto.getStatus());
        }
        Order order = findOrderOrThrowException(id);
        order.setUpdatedAt(ZonedDateTime.now());
        order.setOrderStatus(OrderStatus.valueOf(orderStatusRequestDto.getStatus()));
        orderRepository.save(order);
        return orderMapper.convertOrderToOrderResponseDto(order);
    }

    @Transactional
    @Override
    public void deleteOrder(String id) {
        Order order = findOrderOrThrowException(id);
        if (order.getOrderStatus() != OrderStatus.DRAFT) {
            throw new OrderNotDraftException(id);
        }
        deleteOrderPositions(order);
        orderRepository.delete(order);
    }

    private Order findOrderOrThrowException(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundByIdException(id));
    }

    public BigDecimal countOrderTotalAmount(List<OrderPosition> orderPosition) {
        return orderPosition.stream()
                .map(position -> position.getOrderedBook().getPrice().multiply(BigDecimal.valueOf(position.getQuantity())))
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    private List<OrderPosition> createOrderPositions(OrderRequestDto orderRequestDto) {
        List<OrderPositionRequestDto> orderPositionsRequestDto = orderRequestDto.getOrderPositions();
        checkOrderPositionsForDuplicates(orderPositionsRequestDto);
        List<OrderPosition> orderPositions = new ArrayList<>();
        for (OrderPositionRequestDto orderPositionRequestDto : orderPositionsRequestDto) {
            OrderPosition orderPosition = new OrderPosition();
            Book book = bookService.findBookOrThrowException(orderPositionRequestDto.getBookId());
            orderPosition.setOrderedBook(createOrderedBook(book));
            orderPosition.setQuantity(orderPositionRequestDto.getQuantity());
            orderPositionRepository.save(orderPosition);
            orderPositions.add(orderPosition);
        }
        return orderPositions;
    }

    private OrderedBook createOrderedBook(Book book) {
        OrderedBook orderedBook = new OrderedBook();
        orderedBook.setTitle(book.getTitle());
        orderedBook.setImageUrl(book.getImageUrl());
        orderedBook.setPrice(book.getPrice());
        orderedBook.setAuthorFirstName(book.getAuthor().getFirstName());
        orderedBook.setAuthorLastName(book.getAuthor().getLastName());
        String tags = String.join(COMMA_DELIMITER, book.getTags().stream().map(Tag::getName).toList());
        orderedBook.setTags(tags);
        orderedBook.setCreatedAt(book.getCreatedAt());
        orderedBook.setUpdatedAt(book.getUpdatedAt());
        orderedBookRepository.save(orderedBook);
        return orderedBook;
    }

    private void deleteOrderPositions(Order order) {
        List<OrderPosition> orderPositions = order.getOrderPositions();
        orderPositionRepository.deleteAll(orderPositions);
        orderPositions.forEach(position -> orderedBookRepository.deleteById(position.getOrderedBook().getId()));
    }

    private void checkOrderPositionsForDuplicates(List<OrderPositionRequestDto> orderPosition) {
        Set<String> validationSet = new HashSet<>();
        Set<String> duplicatePositions = orderPosition.stream()
                .map(OrderPositionRequestDto::getBookId)
                .filter(bookId -> !(validationSet.add(bookId)))
                .collect(Collectors.toSet());
        if (!duplicatePositions.isEmpty()) {
            throw new DuplicateOrderPositionsException(duplicatePositions);
        }
    }
}
