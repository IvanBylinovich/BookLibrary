package com.solbeg.BookLibrary.service.impl;

import com.solbeg.BookLibrary.dto.UserRequestDto;
import com.solbeg.BookLibrary.dto.UserResponseDto;
import com.solbeg.BookLibrary.exception.UserNotFoundByIdException;
import com.solbeg.BookLibrary.exception.UserNotFoundByUsernameException;
import com.solbeg.BookLibrary.exception.UsernameAlreadyExistException;
import com.solbeg.BookLibrary.model.entity.Order;
import com.solbeg.BookLibrary.model.entity.User;
import com.solbeg.BookLibrary.repository.OrderPositionRepository;
import com.solbeg.BookLibrary.repository.OrderRepository;
import com.solbeg.BookLibrary.repository.OrderedBookRepository;
import com.solbeg.BookLibrary.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static utils.LibraryTestConstants.ID1;
import static utils.LibraryTestConstants.ID2;
import static utils.LibraryTestConstants.USER_USERNAME1;
import static utils.TestOrderFactory.createOrder1;
import static utils.TestUserFactory.createUser1;
import static utils.TestUserFactory.createUser2;
import static utils.TestUserFactory.createUserRequestDto;
import static utils.TestUserFactory.createUser_ADMIN;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    UserServiceImpl userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private OrderedBookRepository orderedBookRepository;

    @MockBean
    private OrderPositionRepository orderPositionRepository;

    @Test
    void loadUserByUsername_correctWork_returnUserDetails() {
        //Date
        String username = USER_USERNAME1;
        User user = createUser1();
        //When
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        //Then
        UserDetails userDetails = userService.loadUserByUsername(username);

        assertEquals(user.getUsername(), userDetails.getUsername());
        assertFalse(user.getAuthorities().retainAll(userDetails.getAuthorities()));
        assertEquals(user.getPassword(), userDetails.getPassword());
    }

    @Test
    void loadUserByUsername_notExistUserByUsername_throwUserNotFoundByUsernameException() {
        //When
        when(userRepository.findByUsername(USER_USERNAME1)).thenReturn(Optional.empty());
        //Then
        UserNotFoundByUsernameException thrown = assertThrows(UserNotFoundByUsernameException.class, () -> {
            userService.loadUserByUsername(USER_USERNAME1);
        });

        assertEquals(new UserNotFoundByUsernameException(USER_USERNAME1).getMessage(), thrown.getMessage());
    }

    @Test
    void findAllUsers_correctWork_returnUserResponseDtoList() {
        //Date
        User user = createUser1();
        User admin = createUser_ADMIN();
        List<User> users = List.of(user, admin);
        //When
        when(userRepository.findAll()).thenReturn(users);
        //Then
        List<UserResponseDto> usersResponseDto = userService.findAllUsers();
        UserResponseDto userResponseDto1 = usersResponseDto.get(0);
        UserResponseDto adminResponseDto2 = usersResponseDto.get(1);

        assertEquals(2, usersResponseDto.size());
        assertEqualsUserAndUserResponseDto(user, userResponseDto1);
        assertEqualsUserAndUserResponseDto(admin, adminResponseDto2);
    }

    @Test
    void findUserById_correctWork_returnUserResponseDto() {
        //Date
        User user = createUser1();
        //When
        when(userRepository.findById(ID1)).thenReturn(Optional.of(user));
        //Then
        UserResponseDto userResponseDto = userService.findUserById(ID1);

        assertEqualsUserAndUserResponseDto(user, userResponseDto);
    }

    @Test
    void findUserById_userNotExistById_throwUserNotFoundByIdException() {
        //When
        when(userRepository.findById(ID1)).thenReturn(Optional.empty());
        //Then
        UserNotFoundByIdException thrown = assertThrows(UserNotFoundByIdException.class, () -> {
            userService.findUserById(ID1);
        });

        assertEquals(new UserNotFoundByIdException(ID1).getMessage(), thrown.getMessage());
    }

    @Test
    void registerUser_correctWork_returnUserResponseDto() {
        //Date
        User user = createUser1();
        user.setId(null);
        UserRequestDto userRequestDto = createUserRequestDto();
        //Then
        UserResponseDto userResponseDto = userService.registerUser(userRequestDto);

        verify(userRepository, times(1)).save(any(User.class));
        assertEqualsUserAndUserResponseDto(user, userResponseDto);
    }

    @Test
    void registerUser_existUserWithSameUsername_throwUsernameAlreadyExistException() {
        //Date
        User user = createUser1();
        UserRequestDto userRequestDto = createUserRequestDto();
        //When
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        //Then
        UsernameAlreadyExistException thrown = assertThrows(UsernameAlreadyExistException.class, () ->
                userService.registerUser(userRequestDto)
        );

        assertEquals(new UsernameAlreadyExistException(user.getUsername()).getMessage(), thrown.getMessage());
    }

    @Test
    void registerAdmin_correctWork_returnUserResponseDto() {
        //Date
        User user = createUser1();
        user.setId(null);
        UserRequestDto userRequestDto = createUserRequestDto();
        //Then
        UserResponseDto userResponseDto = userService.registerAdmin(userRequestDto);

        verify(userRepository, times(1)).save(any(User.class));
        assertEqualsUserAndUserResponseDto(user, userResponseDto);
    }

    @Test
    void registerAdmin_existUserWithSameUsername_throwUsernameAlreadyExistException() {
        //Date
        User user = createUser1();
        UserRequestDto userRequestDto = createUserRequestDto();
        //When
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        //Then
        UsernameAlreadyExistException thrown = assertThrows(UsernameAlreadyExistException.class, () ->
                userService.registerAdmin(userRequestDto)
        );

        assertEquals(new UsernameAlreadyExistException(user.getUsername()).getMessage(), thrown.getMessage());
    }

    @Test
    void updateUser_correctWork_returnUpdatedUserResponseDto() {
        //Date
        User user = createUser2();
        User updatedUser = createUser1();
        updatedUser.setId(ID2);
        UserRequestDto userRequestDto = createUserRequestDto();
        //When
        when(userRepository.findById(ID2)).thenReturn(Optional.of(user));
        //Then
        UserResponseDto userResponseDto = userService.updateUser(ID2, userRequestDto);

        verify(userRepository, times(1)).save(any(User.class));
        assertEqualsUserAndUserResponseDto(updatedUser, userResponseDto);
    }

    @Test
    void updateUser_notExistUserById_throwUserNotFoundByIdException() {
        //When
        when(userRepository.findById(ID1)).thenReturn(Optional.empty());
        //Then
        UserNotFoundByIdException thrown = assertThrows(UserNotFoundByIdException.class, () -> {
            userService.updateUser(ID1, new UserRequestDto());
        });

        assertEquals(new UserNotFoundByIdException(ID1).getMessage(), thrown.getMessage());
    }

    @Test
    void updateUser_existUserWithSameUsername_throwUsernameAlreadyExistException() {
        //Date
        User user1 = createUser1();
        User user2 = createUser2();
        UserRequestDto userRequestDto = createUserRequestDto();
        //When
        when(userRepository.findById(ID1)).thenReturn(Optional.of(user2));
        when(userRepository.findByUsername(userRequestDto.getUsername())).thenReturn(Optional.of(user1));
        //Then
        UsernameAlreadyExistException thrown = assertThrows(UsernameAlreadyExistException.class, () ->
                userService.updateUser(ID1, userRequestDto)
        );

        assertEquals(new UsernameAlreadyExistException(user1.getUsername()).getMessage(), thrown.getMessage());
    }

    @Test
    void deleteUser_correctWorkOrderPositionsExist_applyMethods() {
        //Date
        User user = createUser1();
        Order order = createOrder1();
        List<Order> orders = List.of(order);
        //When
        when(userRepository.findById(ID1)).thenReturn(Optional.of(user));
        when(orderRepository.findAllByUser(user)).thenReturn(Optional.of(orders));
        //Then
        userService.deleteUser(ID1);

        verify(orderRepository, times(1)).deleteAll(orders);
        verify(orderPositionRepository, times(1)).deleteAll(any());
        verify(orderedBookRepository, times(1)).deleteAll(any());
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void deleteUser_correctWorkNotExistOrderPositions_applyMethodFromUserRepository() {
        //Date
        User user = createUser1();
        Order order = createOrder1();
        List<Order> orders = List.of(order);
        //When
        when(userRepository.findById(ID1)).thenReturn(Optional.of(user));
        when(orderRepository.findAllByUser(user)).thenReturn(Optional.empty());
        //Then
        userService.deleteUser(ID1);

        verify(orderRepository, times(0)).deleteAll(orders);
        verify(orderPositionRepository, times(0)).deleteAll(any());
        verify(orderedBookRepository, times(0)).deleteAll(any());
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void deleteUser_userNotExistById_throwUserNotFoundByIdException() {
        //When
        when(userRepository.findById(ID1)).thenReturn(Optional.empty());
        //Then
        UserNotFoundByIdException thrown = assertThrows(UserNotFoundByIdException.class, () -> {
            userService.deleteUser(ID1);
        });

        assertEquals(new UserNotFoundByIdException(ID1).getMessage(), thrown.getMessage());
    }

    private void assertEqualsUserAndUserResponseDto(User user, UserResponseDto userResponseDto) {
        assertEquals(user.getId(), userResponseDto.getId());
        assertEquals(user.getUsername(), userResponseDto.getUsername());
        assertEquals(user.getFirstName(), userResponseDto.getFirstName());
        assertEquals(user.getLastName(), userResponseDto.getLastName());
    }
}