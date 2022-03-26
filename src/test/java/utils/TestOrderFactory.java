package utils;

import com.solbeg.BookLibrary.dto.OrderPositionRequestDto;
import com.solbeg.BookLibrary.dto.OrderPositionResponseDto;
import com.solbeg.BookLibrary.dto.OrderRequestDto;
import com.solbeg.BookLibrary.dto.OrderResponseDto;
import com.solbeg.BookLibrary.dto.OrderedBookResponseDto;
import com.solbeg.BookLibrary.dto.UserResponseDto;
import com.solbeg.BookLibrary.model.OrderStatus;
import com.solbeg.BookLibrary.model.entity.Order;
import com.solbeg.BookLibrary.model.entity.OrderPosition;
import com.solbeg.BookLibrary.model.entity.OrderedBook;

import java.util.ArrayList;
import java.util.List;

import static utils.LibraryTestConstants.BIG_DECIMAL_TOTAL_AMOUNT1;
import static utils.LibraryTestConstants.BIG_DECIMAL_TOTAL_AMOUNT2;
import static utils.LibraryTestConstants.DATE_ZONED_DATA_TIME1;
import static utils.LibraryTestConstants.DATE_ZONED_DATA_TIME2;
import static utils.LibraryTestConstants.FIRST_NAME1;
import static utils.LibraryTestConstants.FIRST_NAME2;
import static utils.LibraryTestConstants.ID1;
import static utils.LibraryTestConstants.ID2;
import static utils.LibraryTestConstants.LAST_NAME1;
import static utils.LibraryTestConstants.LAST_NAME2;
import static utils.LibraryTestConstants.PRICE_BIG_DECIMAL1;
import static utils.LibraryTestConstants.PRICE_BIG_DECIMAL2;
import static utils.LibraryTestConstants.QUANTITY1;
import static utils.LibraryTestConstants.QUANTITY2;
import static utils.LibraryTestConstants.TAG_NAME1;
import static utils.LibraryTestConstants.TAG_NAME2;
import static utils.LibraryTestConstants.TAG_NAME3;
import static utils.LibraryTestConstants.TITLE1;
import static utils.LibraryTestConstants.TITLE2;
import static utils.LibraryTestConstants.URL1;
import static utils.LibraryTestConstants.URL2;
import static utils.TestUserFactory.createUser1;

public class TestOrderFactory {

    public static Order createOrder1() {
        Order order = new Order();
        order.setId(ID1);
        order.setUser(createUser1());
        order.setOrderPositions(createOrderPositionList1());
        order.setTotalAmount(BIG_DECIMAL_TOTAL_AMOUNT1);
        order.setCreatedAt(DATE_ZONED_DATA_TIME1);
        order.setUpdatedAt(DATE_ZONED_DATA_TIME1);
        order.setOrderStatus(OrderStatus.DRAFT);
        return order;
    }

    public static Order createOrder2() {
        Order order = new Order();
        order.setId(ID2);
        order.setOrderPositions(createOrderPositionList1());
        order.setTotalAmount(BIG_DECIMAL_TOTAL_AMOUNT2);
        order.setCreatedAt(DATE_ZONED_DATA_TIME1);
        order.setUpdatedAt(DATE_ZONED_DATA_TIME1);
        order.setOrderStatus(OrderStatus.DRAFT);
        return order;
    }

    public static List<OrderPosition> createOrderPositionList1() {
        List<OrderPosition> orderPosition = new ArrayList<>();
        orderPosition.add(createOrderPosition1());
        orderPosition.add(createOrderPosition2());
        return orderPosition;
    }

    public static OrderPosition createOrderPosition1() {
        OrderPosition orderPosition = new OrderPosition();
        orderPosition.setId(ID1);
        orderPosition.setOrderedBook(createOrderedBook1());
        orderPosition.setQuantity(QUANTITY1);
        return orderPosition;
    }

    public static OrderPosition createOrderPosition2() {
        OrderPosition orderPosition = new OrderPosition();
        orderPosition.setId(ID2);
        orderPosition.setOrderedBook(createOrderedBook2());
        orderPosition.setQuantity(QUANTITY2);
        return orderPosition;
    }

    public static OrderedBook createOrderedBook1() {
        OrderedBook orderedBook = new OrderedBook();
        orderedBook.setId(ID1);
        orderedBook.setTitle(TITLE1);
        orderedBook.setImageUrl(URL1);
        orderedBook.setPrice(PRICE_BIG_DECIMAL1);
        orderedBook.setAuthorFirstName(FIRST_NAME1);
        orderedBook.setAuthorLastName(LAST_NAME1);
        orderedBook.setTags(TAG_NAME1 + ", " + TAG_NAME2);
        orderedBook.setCreatedAt(DATE_ZONED_DATA_TIME1);
        orderedBook.setUpdatedAt(DATE_ZONED_DATA_TIME1);
        return orderedBook;
    }

    public static OrderedBook createOrderedBook2() {
        OrderedBook orderedBook = new OrderedBook();
        orderedBook.setId(ID2);
        orderedBook.setTitle(TITLE2);
        orderedBook.setImageUrl(URL2);
        orderedBook.setPrice(PRICE_BIG_DECIMAL2);
        orderedBook.setAuthorFirstName(FIRST_NAME2);
        orderedBook.setAuthorLastName(LAST_NAME2);
        orderedBook.setTags(TAG_NAME2 + ", " + TAG_NAME3);
        orderedBook.setCreatedAt(DATE_ZONED_DATA_TIME2);
        orderedBook.setUpdatedAt(DATE_ZONED_DATA_TIME2);
        return orderedBook;
    }

    public static OrderRequestDto createOrderRequestDtoDto1() {
        return new OrderRequestDto(ID1, createOrderPositionResponseDtoList1());
    }

    public static OrderRequestDto createOrderRequestDtoWithDuplicate() {
        return new OrderRequestDto(ID1, createOrderPositionResponseDtoListWithDuplicateOrderPositions());
    }

    public static OrderRequestDto createOrderRequestDtoDto2() {
        return new OrderRequestDto(ID1, createOrderPositionResponseDtoList2());
    }

    public static OrderPositionRequestDto createOrderPositionRequestDto1() {
        return new OrderPositionRequestDto(ID1, QUANTITY1);
    }

    public static OrderPositionRequestDto createOrderPositionRequestDto2() {
        return new OrderPositionRequestDto(ID2, QUANTITY2);
    }

    public static List<OrderPositionRequestDto> createOrderPositionResponseDtoList1() {
        List<OrderPositionRequestDto> orderPositionRequestDtoList = new ArrayList<>();
        orderPositionRequestDtoList.add(createOrderPositionRequestDto1());
        orderPositionRequestDtoList.add(createOrderPositionRequestDto2());
        return orderPositionRequestDtoList;
    }

    public static List<OrderPositionRequestDto> createOrderPositionResponseDtoListWithDuplicateOrderPositions() {
        List<OrderPositionRequestDto> orderPositionRequestDtoList = new ArrayList<>();
        OrderPositionRequestDto orderPositionRequestDto = createOrderPositionRequestDto1();
        orderPositionRequestDtoList.add(orderPositionRequestDto);
        orderPositionRequestDtoList.add(orderPositionRequestDto);
        return orderPositionRequestDtoList;
    }

    public static List<OrderPositionRequestDto> createOrderPositionResponseDtoList2() {
        List<OrderPositionRequestDto> orderPositionRequestDtoList = new ArrayList<>();
        orderPositionRequestDtoList.add(createOrderPositionRequestDto1());
        return orderPositionRequestDtoList;
    }

    public static OrderedBookResponseDto createOrderedBookResponseDto1() {
        return new OrderedBookResponseDto(ID1, TITLE1, URL1, PRICE_BIG_DECIMAL1, TAG_NAME1, FIRST_NAME1, LAST_NAME1, DATE_ZONED_DATA_TIME1, DATE_ZONED_DATA_TIME1);
    }

    public static OrderedBookResponseDto createOrderedBookResponseDto2() {
        return new OrderedBookResponseDto(ID2, TITLE2, URL2, PRICE_BIG_DECIMAL2, TAG_NAME2, FIRST_NAME2, LAST_NAME2, DATE_ZONED_DATA_TIME2, DATE_ZONED_DATA_TIME2);
    }
}


