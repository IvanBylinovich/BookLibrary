package com.solbeg.BookLibrary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solbeg.BookLibrary.dto.OrderPositionResponseDto;
import com.solbeg.BookLibrary.dto.OrderRequestDto;
import com.solbeg.BookLibrary.dto.OrderResponseDto;
import com.solbeg.BookLibrary.dto.OrderStatusRequestDto;
import com.solbeg.BookLibrary.dto.OrderedBookResponseDto;
import com.solbeg.BookLibrary.model.OrderStatus;
import com.solbeg.BookLibrary.model.entity.Author;
import com.solbeg.BookLibrary.model.entity.Book;
import com.solbeg.BookLibrary.model.entity.Order;
import com.solbeg.BookLibrary.model.entity.OrderPosition;
import com.solbeg.BookLibrary.model.entity.OrderedBook;
import com.solbeg.BookLibrary.model.entity.Tag;
import com.solbeg.BookLibrary.model.entity.User;
import com.solbeg.BookLibrary.repository.AuthorRepository;
import com.solbeg.BookLibrary.repository.BookRepository;
import com.solbeg.BookLibrary.repository.OrderPositionRepository;
import com.solbeg.BookLibrary.repository.OrderRepository;
import com.solbeg.BookLibrary.repository.OrderedBookRepository;
import com.solbeg.BookLibrary.repository.TagRepository;
import com.solbeg.BookLibrary.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utils.LibraryTestConstants.ID3;
import static utils.LibraryTestConstants.NOT_VALID_STATUS_NAME;
import static utils.TestAuthorFactory.createAuthor1;
import static utils.TestAuthorFactory.createAuthor2;
import static utils.TestBookFactory.createBook1;
import static utils.TestBookFactory.createBook2;
import static utils.TestOrderFactory.createOrder1;
import static utils.TestOrderFactory.createOrder2;
import static utils.TestOrderFactory.createOrderPosition1;
import static utils.TestOrderFactory.createOrderPosition2;
import static utils.TestOrderFactory.createOrderRequestDtoDto1;
import static utils.TestOrderFactory.createOrderRequestDtoDto2;
import static utils.TestOrderFactory.createOrderedBook1;
import static utils.TestOrderFactory.createOrderedBook2;
import static utils.TestTagFactory.createTag1;
import static utils.TestTagFactory.createTag2;
import static utils.TestUserFactory.createUser1;
import static utils.TestUserFactory.createUser2;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private OrderPositionRepository orderPositionRepository;

    @Autowired
    private OrderedBookRepository orderedBookRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    private Order order1;
    private Order order2;
    private Order order3;

    private User user1 = createUser1();
    private User user2 = createUser2();

    private Book book1 = createBook1();
    private Book book2 = createBook2();

    private Tag tag1 = createTag1();
    private Tag tag2 = createTag2();

    private Author author1 = createAuthor1();
    private Author author2 = createAuthor2();

    private OrderedBook orderedBook1 = createOrderedBook1();
    private OrderedBook orderedBook2 = createOrderedBook2();
    private OrderedBook orderedBook3 = createOrderedBook1();
    private OrderedBook orderedBook4 = createOrderedBook2();

    private OrderPosition orderPosition1 = createOrderPosition1();
    private OrderPosition orderPosition2 = createOrderPosition2();
    private OrderPosition orderPosition3 = createOrderPosition1();
    private OrderPosition orderPosition4 = createOrderPosition2();

    private OrderRequestDto orderRequestDto1 = createOrderRequestDtoDto1();
    private OrderRequestDto orderRequestDto2 = createOrderRequestDtoDto2();

    @BeforeEach
    void insertTestData() {

        orderRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();
        authorRepository.deleteAll();
        tagRepository.deleteAll();
        orderPositionRepository.deleteAll();
        orderedBookRepository.deleteAll();

        author1.setId(null);
        author2.setId(null);
        authorRepository.save(author1);
        authorRepository.save(author2);

        tag1.setId(null);
        tag2.setId(null);
        tagRepository.save(tag1);
        tagRepository.save(tag2);

        book1.setAuthor(author1);
        book2.setAuthor(author2);
        book1.setTags(Set.of(tag1, tag2));
        book2.setTags(Set.of(tag1));
        book1.setId(null);
        book2.setId(null);
        bookRepository.save(book1);
        bookRepository.save(book2);

        user1.setId(null);
        user2.setId(null);
        userRepository.save(user1);
        userRepository.save(user2);

        orderedBook1.setId(null);
        orderedBook2.setId(null);
        orderedBook3.setId(null);
        orderedBook4.setId(null);
        orderedBookRepository.save(orderedBook1);
        orderedBookRepository.save(orderedBook2);
        orderedBookRepository.save(orderedBook3);
        orderedBookRepository.save(orderedBook4);

        orderPosition1.setId(null);
        orderPosition2.setId(null);
        orderPosition3.setId(null);
        orderPosition4.setId(null);
        orderPosition1.setOrderedBook(orderedBook1);
        orderPosition2.setOrderedBook(orderedBook2);
        orderPosition3.setOrderedBook(orderedBook3);
        orderPosition4.setOrderedBook(orderedBook4);
        orderPositionRepository.save(orderPosition1);
        orderPositionRepository.save(orderPosition2);
        orderPositionRepository.save(orderPosition3);
        orderPositionRepository.save(orderPosition4);

        order1 = createOrder1();
        order1.setId(null);
        order1.setUser(user1);
        order1.setOrderPositions(List.of(orderPosition1, orderPosition2));
        order2 = createOrder2();
        order2.setId(null);
        order2.setUser(user2);
        order2.setOrderPositions(List.of(orderPosition3));
        order3 = createOrder2();
        order3.setId(null);
        order3.setUser(user1);
        order3.setOrderPositions(List.of(orderPosition4));
        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);

        orderRequestDto1.setUserId(user1.getId());
        orderRequestDto1.getOrderPositions().get(0).setBookId(book1.getId());
        orderRequestDto1.getOrderPositions().get(1).setBookId(book2.getId());
        orderRequestDto2.setUserId(user1.getId());
        orderRequestDto2.getOrderPositions().get(0).setBookId(book1.getId());
    }

    @AfterEach()
    private void clearTestData() {
        orderRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();
        authorRepository.deleteAll();
        tagRepository.deleteAll();
        orderPositionRepository.deleteAll();
        orderedBookRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void getAllOrders_correctWork_returnOrderResponseDto() throws Exception {
        //Date
        String url = "/orders/allOrders";
        //When
        String response = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //Then
        List<HashMap<String, String>> orders = objectMapper.readValue(response, List.class);
        HashMap<String, String> orderData1 = orders.get(0);
        HashMap<String, String> orderData2 = orders.get(1);
        HashMap<String, String> orderData3 = orders.get(2);
        assertEquals(3, orders.size());
        assertEquals(order1.getId(), orderData1.get("id"));
        assertEquals(order2.getId(), orderData2.get("id"));
        assertEquals(order3.getId(), orderData3.get("id"));
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void getAllOrders_correctWork_returnEmptyList() throws Exception {
        //Date
        String url = "/orders/allOrders";
        clearTestData();
        //When
        String response = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //Then
        List<HashMap<String, String>> orders = objectMapper.readValue(response, List.class);
        assertTrue(orders.isEmpty());
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void getOrderById_correctWork_returnOrderResponseDto() throws Exception {
        //Date
        String url = "/orders/{id}";
        //When
        String response = mockMvc.perform(get(url, order1.getId()).accept(MediaType.APPLICATION_JSON_VALUE).accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //Then
        OrderResponseDto orderResponseDto = objectMapper.readValue(response, OrderResponseDto.class);
        OrderPosition orderPosition1 = order1.getOrderPositions().get(0);
        OrderPosition orderPosition2 = order1.getOrderPositions().get(1);
        OrderedBook orderedBook1 = orderPosition1.getOrderedBook();
        OrderedBook orderedBook2 = orderPosition2.getOrderedBook();
        OrderPositionResponseDto orderPositionResponseDto1 = orderResponseDto.getOrderPositions().get(0);
        OrderPositionResponseDto orderPositionResponseDto2 = orderResponseDto.getOrderPositions().get(1);
        OrderedBookResponseDto orderedBookResponseDto1 = orderPositionResponseDto1.getOrderedBook();
        OrderedBookResponseDto orderedBookResponseDto2 = orderPositionResponseDto2.getOrderedBook();

        assertEquals(order1.getId(), orderResponseDto.getId());
        assertEquals(order1.getUser().getId(), orderResponseDto.getUser().getId());
        assertEquals(order1.getTotalAmount(), orderResponseDto.getTotalAmount());
        assertEquals(order1.getOrderStatus(), orderResponseDto.getOrderStatus());
        assertEquals(order1.getOrderPositions().size(), orderResponseDto.getOrderPositions().size());
        assertEquals(orderPosition1.getId(), orderPositionResponseDto1.getId());
        assertEquals(orderPosition2.getId(), orderPositionResponseDto2.getId());
        assertEquals(orderPosition1.getQuantity(), orderPositionResponseDto1.getQuantity());
        assertEquals(orderPosition2.getQuantity(), orderPositionResponseDto2.getQuantity());
        assertEquals(orderedBook1.getId(), orderedBookResponseDto1.getId());
        assertEquals(orderedBook1.getTitle(), orderedBookResponseDto1.getTitle());
        assertEquals(orderedBook1.getPrice(), orderedBookResponseDto1.getPrice());
        assertEquals(orderedBook1.getImageUrl(), orderedBookResponseDto1.getImageUrl());
        assertEquals(orderedBook1.getAuthorFirstName(), orderedBookResponseDto1.getAuthorFirstName());
        assertEquals(orderedBook1.getAuthorLastName(), orderedBookResponseDto1.getAuthorLastName());
        assertEquals(orderedBook2.getId(), orderedBookResponseDto2.getId());
        assertEquals(orderedBook2.getTitle(), orderedBookResponseDto2.getTitle());
        assertEquals(orderedBook2.getPrice(), orderedBookResponseDto2.getPrice());
        assertEquals(orderedBook2.getImageUrl(), orderedBookResponseDto2.getImageUrl());
        assertEquals(orderedBook2.getAuthorFirstName(), orderedBookResponseDto2.getAuthorFirstName());
        assertEquals(orderedBook2.getAuthorLastName(), orderedBookResponseDto2.getAuthorLastName());
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void getOrderById_orderNotExistById_returnClientErrorCode() throws Exception {
        //Date
        String url = "/orders/{id}";
        //When
        mockMvc.perform(get(url, ID3).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    void getOrderById_userNotAuthorised_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/orders/{id}";
        //When
        mockMvc.perform(get(url, order1.getId()).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void createOrder_correctWork_returnOrderResponseDto() throws Exception {
        //Date
        String url = "/orders";
        //When
        String response = mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(orderRequestDto1))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //Then
        OrderResponseDto orderResponseDto = objectMapper.readValue(response, OrderResponseDto.class);
        OrderPosition orderPosition1 = order1.getOrderPositions().get(0);
        OrderPosition orderPosition2 = order1.getOrderPositions().get(1);
        OrderedBook orderedBook1 = orderPosition1.getOrderedBook();
        OrderedBook orderedBook2 = orderPosition2.getOrderedBook();
        OrderPositionResponseDto orderPositionResponseDto1 = orderResponseDto.getOrderPositions().get(0);
        OrderPositionResponseDto orderPositionResponseDto2 = orderResponseDto.getOrderPositions().get(1);
        OrderedBookResponseDto orderedBookResponseDto1 = orderPositionResponseDto1.getOrderedBook();
        OrderedBookResponseDto orderedBookResponseDto2 = orderPositionResponseDto2.getOrderedBook();

        assertNotEquals(order1.getId(), orderResponseDto.getId());
        assertEquals(order1.getUser().getId(), orderResponseDto.getUser().getId());
        assertEquals(order1.getTotalAmount(), orderResponseDto.getTotalAmount());
        assertEquals(order1.getOrderStatus(), orderResponseDto.getOrderStatus());
        assertEquals(order1.getOrderPositions().size(), orderResponseDto.getOrderPositions().size());
        assertNotEquals(orderPosition1.getId(), orderPositionResponseDto1.getId());
        assertNotEquals(orderPosition2.getId(), orderPositionResponseDto2.getId());
        assertEquals(orderPosition1.getQuantity(), orderPositionResponseDto1.getQuantity());
        assertEquals(orderPosition2.getQuantity(), orderPositionResponseDto2.getQuantity());
        assertNotEquals(orderedBook1.getId(), orderedBookResponseDto1.getId());
        assertEquals(orderedBook1.getTitle(), orderedBookResponseDto1.getTitle());
        assertEquals(orderedBook1.getPrice(), orderedBookResponseDto1.getPrice());
        assertEquals(orderedBook1.getImageUrl(), orderedBookResponseDto1.getImageUrl());
        assertEquals(orderedBook1.getAuthorFirstName(), orderedBookResponseDto1.getAuthorFirstName());
        assertEquals(orderedBook1.getAuthorLastName(), orderedBookResponseDto1.getAuthorLastName());
        assertNotEquals(orderedBook2.getId(), orderedBookResponseDto2.getId());
        assertEquals(orderedBook2.getTitle(), orderedBookResponseDto2.getTitle());
        assertEquals(orderedBook2.getPrice(), orderedBookResponseDto2.getPrice());
        assertEquals(orderedBook2.getImageUrl(), orderedBookResponseDto2.getImageUrl());
        assertEquals(orderedBook2.getAuthorFirstName(), orderedBookResponseDto2.getAuthorFirstName());
        assertEquals(orderedBook2.getAuthorLastName(), orderedBookResponseDto2.getAuthorLastName());
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void createOrder_userNotExistById_returnClientErrorCode() throws Exception {
        //Date
        String url = "/orders";
        orderRequestDto1.setUserId(ID3);
        //When
        mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(orderRequestDto1))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void createOrder_bookNoeExistById_returnClientErrorCode() throws Exception {
        //Date
        String url = "/orders";
        orderRequestDto1.getOrderPositions().get(0).setBookId(ID3);
        //When
        mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(orderRequestDto1))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void createOrder_orderPositionsHasDuplicates_returnClientErrorCode() throws Exception {
        //Date
        String url = "/orders";
        orderRequestDto1.getOrderPositions().get(0).setBookId(book2.getId());
        //When
        mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(orderRequestDto1))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    void createOrder_userNotAuthorised_returnForbiddenErrorCode() throws Exception {
        //Date
        String url = "/orders";
        //When
        mockMvc.perform(post(url).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(orderRequestDto1))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void updateOrder_correctWork_returnOrderResponseDto() throws Exception {
        //Date
        String url = "/orders/{id}";
        //When
        String response = mockMvc.perform(put(url, order3.getId()).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(orderRequestDto2))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //Then
        OrderResponseDto orderResponseDto = objectMapper.readValue(response, OrderResponseDto.class);
        OrderPosition orderPosition1 = order1.getOrderPositions().get(0);

        OrderedBook orderedBook1 = orderPosition1.getOrderedBook();
        OrderPositionResponseDto orderPositionResponseDto1 = orderResponseDto.getOrderPositions().get(0);
        OrderedBookResponseDto orderedBookResponseDto1 = orderPositionResponseDto1.getOrderedBook();

        assertNotEquals(order1.getId(), orderResponseDto.getId());
        assertEquals(order1.getUser().getId(), orderResponseDto.getUser().getId());
        assertEquals(order2.getTotalAmount(), orderResponseDto.getTotalAmount());
        assertEquals(order2.getOrderStatus(), orderResponseDto.getOrderStatus());
        assertEquals(order2.getOrderPositions().size(), orderResponseDto.getOrderPositions().size());
        assertNotEquals(orderPosition1.getId(), orderPositionResponseDto1.getId());
        assertEquals(orderPosition1.getQuantity(), orderPositionResponseDto1.getQuantity());
        assertNotEquals(orderedBook1.getId(), orderedBookResponseDto1.getId());
        assertEquals(orderedBook1.getTitle(), orderedBookResponseDto1.getTitle());
        assertEquals(orderedBook1.getPrice(), orderedBookResponseDto1.getPrice());
        assertEquals(orderedBook1.getImageUrl(), orderedBookResponseDto1.getImageUrl());
        assertEquals(orderedBook1.getAuthorFirstName(), orderedBookResponseDto1.getAuthorFirstName());
        assertEquals(orderedBook1.getAuthorLastName(), orderedBookResponseDto1.getAuthorLastName());
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void updateOrder_userNotExistById_returnClientErrorCode() throws Exception {
        //Date
        String url = "/orders/{id}";
        //When
        mockMvc.perform(put(url, ID3).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(orderRequestDto2))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void updateOrder_orderPositionsHasDuplicates_returnClientErrorCode() throws Exception {
        //Date
        String url = "/orders/{id}";
        orderRequestDto1.getOrderPositions().get(0).setBookId(book2.getId());
        //When
        mockMvc.perform(put(url, order3.getId()).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(orderRequestDto1))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void updateOrder_invalidRequestDto_returnClientErrorCode() throws Exception {
        //Date
        String url = "/orders/{id}";
        //When
        mockMvc.perform(put(url, order1).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new OrderRequestDto()))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    void updateOrder_userNotAuthenticated_returnIsForbiddenErrorCode() throws Exception {
        //Date
        String url = "/orders/{id}";
        //When
        mockMvc.perform(put(url, order3.getId()).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(orderRequestDto2))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }


    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void updateOrderStatus_correctWork_returnOrderResponseDto() throws Exception {
        //Date
        String url = "/orders/{id}";
        //When
        String response = mockMvc.perform(patch(url, order2.getId()).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new OrderStatusRequestDto("PAID")))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //Then
        OrderResponseDto orderResponseDto = objectMapper.readValue(response, OrderResponseDto.class);
        OrderPosition orderPosition1 = order2.getOrderPositions().get(0);

        OrderedBook orderedBook1 = orderPosition1.getOrderedBook();
        OrderPositionResponseDto orderPositionResponseDto1 = orderResponseDto.getOrderPositions().get(0);
        OrderedBookResponseDto orderedBookResponseDto1 = orderPositionResponseDto1.getOrderedBook();

        assertEquals(order2.getId(), orderResponseDto.getId());
        assertEquals(order2.getUser().getId(), orderResponseDto.getUser().getId());
        assertEquals(order2.getTotalAmount(), orderResponseDto.getTotalAmount());
        assertEquals(OrderStatus.PAID, orderResponseDto.getOrderStatus());
        assertEquals(order2.getOrderPositions().size(), orderResponseDto.getOrderPositions().size());

        assertEquals(orderPosition1.getId(), orderPositionResponseDto1.getId());
        assertEquals(orderPosition1.getQuantity(), orderPositionResponseDto1.getQuantity());

        assertEquals(orderedBook1.getId(), orderedBookResponseDto1.getId());
        assertEquals(orderedBook1.getTitle(), orderedBookResponseDto1.getTitle());
        assertEquals(orderedBook1.getPrice(), orderedBookResponseDto1.getPrice());
        assertEquals(orderedBook1.getImageUrl(), orderedBookResponseDto1.getImageUrl());
        assertEquals(orderedBook1.getAuthorFirstName(), orderedBookResponseDto1.getAuthorFirstName());
        assertEquals(orderedBook1.getAuthorLastName(), orderedBookResponseDto1.getAuthorLastName());
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void updateOrderStatus_invalidStatusName_returnClientErrorCode() throws Exception {
        //Date
        String url = "/orders/{id}";
        //When
        mockMvc.perform(patch(url, order1.getId()).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new OrderStatusRequestDto(NOT_VALID_STATUS_NAME)))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void updateOrderStatus_orderNotExistById_returnClientErrorCode() throws Exception {
        //Date
        String url = "/orders/{id}";
        //When
        mockMvc.perform(patch(url, ID3).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new OrderStatusRequestDto("PAID")))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void updateOrderStatus_invalidRequestDto_returnClientErrorCode() throws Exception {
        //Date
        String url = "/orders/{id}";
        //When
        mockMvc.perform(patch(url, order1.getId()).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new OrderStatusRequestDto()))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    void updateOrderStatus_notAuthorised_returnOrderResponseDto() throws Exception {
        //Date
        String url = "/orders/{id}";
        //When
        mockMvc.perform(patch(url, order2.getId()).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new OrderStatusRequestDto("PAID")))
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void deleteOrder_correctWork_returnSuccessfulCode() throws Exception {
        //Date
        String url = "/orders/{id}";
        //When
        mockMvc.perform(delete(url, order2.getId()).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void deleteOrder_orderNotExistById_returnClientErrorCode() throws Exception {
        //Date
        String url = "/orders/{id}";
        //When
        mockMvc.perform(delete(url, ID3).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    @WithMockUser(username = "admin@admin.by", authorities = {"READ", "WRITE", "EDIT", "DELETE", "ALL"}, password = "123456789")
    void deleteOrder_statusNotDraftOrder_returnClientErrorCode() throws Exception {
        //Date
        String url = "/orders/{id}";
        order2.setOrderStatus(OrderStatus.PAID);
        orderRepository.save(order2);
        //When
        mockMvc.perform(delete(url, order2.getId()).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError())
                .andReturn().getResponse();
    }

    @Test
    void deleteOrder_userNotAuthorised_returnClientErrorCode() throws Exception {
        //Date
        String url = "/orders/{id}";
        //When
        mockMvc.perform(delete(url, order2.getId()).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();
    }
}