package com.solbeg.BookLibrary.service.impl;

import com.auth0.jwt.algorithms.Algorithm;
import com.solbeg.BookLibrary.dto.UserRequestDto;
import com.solbeg.BookLibrary.dto.UserResponseDto;
import com.solbeg.BookLibrary.exception.UserNotFoundByIdException;
import com.solbeg.BookLibrary.exception.UserNotFoundByUsernameException;
import com.solbeg.BookLibrary.exception.UsernameAlreadyExistException;
import com.solbeg.BookLibrary.mapper.UserMapper;
import com.solbeg.BookLibrary.model.Role;
import com.solbeg.BookLibrary.model.entity.Order;
import com.solbeg.BookLibrary.model.entity.OrderPosition;
import com.solbeg.BookLibrary.model.entity.OrderedBook;
import com.solbeg.BookLibrary.model.entity.User;
import com.solbeg.BookLibrary.repository.OrderPositionRepository;
import com.solbeg.BookLibrary.repository.OrderRepository;
import com.solbeg.BookLibrary.repository.OrderedBookRepository;
import com.solbeg.BookLibrary.repository.UserRepository;
import com.solbeg.BookLibrary.security.JWTResponseService;
import com.solbeg.BookLibrary.security.JWTService;
import com.solbeg.BookLibrary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.solbeg.BookLibrary.utils.LibraryConstants.JWT_PREFIX;
import static com.solbeg.BookLibrary.utils.LibraryConstants.JWT_REFRESH_TOKEN_MISSING_ERROR_MESSAGE;
import static com.solbeg.BookLibrary.utils.LibraryConstants.JWT_SECRET;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderedBookRepository orderedBookRepository;
    private final OrderPositionRepository orderPositionRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundByUsernameException(username));
        List<SimpleGrantedAuthority> authorities = user.getRole().getAuthorities().stream()
                .map(String::valueOf)
                .map(SimpleGrantedAuthority::new)
                .toList();
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserResponseDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::convertUserToUserResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponseDto findUserById(String id) {
        return userMapper.convertUserToUserResponseDto(findUserByIdOrThrowException(id));
    }

    @Override
    public UserResponseDto registerUser(UserRequestDto userRequestDto) {
        return userMapper.convertUserToUserResponseDto(createUser(userRequestDto, Role.USER));
    }

    @Override
    public UserResponseDto registerAdmin(UserRequestDto userRequestDto) {
        return userMapper.convertUserToUserResponseDto(createUser(userRequestDto, Role.ADMIN));
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(JWT_PREFIX)) {
            try {
                String refresh_token = authorizationHeader.substring(JWT_PREFIX.length());
                Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET.getBytes());
                User user = (User) jwtService.getRequestUser(algorithm, refresh_token, this);
                String access_token = jwtService.createAccessToken(user, request, algorithm);
                Map<String, String> tokens = jwtService.mapTokens(access_token, refresh_token);
                JWTResponseService.configurationJSONResponse(response, tokens);
            } catch (Exception exception) {
                Map<String, String> error = JWTResponseService.responseErrorConfiguration(response, exception);
                JWTResponseService.configurationJSONResponse(response, error);
            }
        } else {
            throw new RuntimeException(JWT_REFRESH_TOKEN_MISSING_ERROR_MESSAGE);
        }
    }

    @Override
    public UserResponseDto updateUser(String id, UserRequestDto userRequestDto) {
        User existingUser = findUserByIdOrThrowException(id);
        validationUsername(userRequestDto.getUsername());
        User user = userMapper.convertUserRequestDtoToUser(userRequestDto);
        user.setId(existingUser.getId());
        user.setCreatedAt(existingUser.getCreatedAt());
        user.setUpdatedAt(ZonedDateTime.now());
        user.setRole(existingUser.getRole());
        userRepository.save(user);
        return userMapper.convertUserToUserResponseDto(user);
    }

    @Transactional
    @Override
    public void deleteUser(String id) {
        User user = findUserByIdOrThrowException(id);
        deleteOrders(user);
        userRepository.delete(user);
    }

    @Override
    public User findUserByIdOrThrowException(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundByIdException(id));
    }

    @Override
    public User findUserByUsernameOrThrowException(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundByUsernameException(username));
    }

    @Override
    public void validationUsername(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyExistException(username);
        }
    }

    private User createUser(UserRequestDto userRequestDto, Role role) {
        validationUsername(userRequestDto.getUsername());
        User user = userMapper.convertUserRequestDtoToUser(userRequestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(role);
        user.setCreatedAt(ZonedDateTime.now());
        user.setUpdatedAt(ZonedDateTime.now());
        userRepository.save(user);
        return user;
    }

    private void deleteOrders(User user) {
        List<Order> orders = orderRepository.findAllByUser(user).orElse(null);
        if (orders != null) {
            List<OrderPosition> positions = orders.stream()
                    .map(Order::getOrderPositions)
                    .flatMap(Collection::stream)
                    .toList();
            List<OrderedBook> orderedBooks = positions.stream()
                    .map(OrderPosition::getOrderedBook)
                    .toList();
            orderRepository.deleteAll(orders);
            orderPositionRepository.deleteAll(positions);
            orderedBookRepository.deleteAll(orderedBooks);
        }
    }
}
