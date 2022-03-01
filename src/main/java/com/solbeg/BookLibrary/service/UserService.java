package com.solbeg.BookLibrary.service;

import com.solbeg.BookLibrary.dto.UserRequestDto;
import com.solbeg.BookLibrary.dto.UserResponseDto;
import com.solbeg.BookLibrary.model.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface UserService {

    List<UserResponseDto> findAllUsers();

    UserResponseDto findUserById(String id);

    User findUserByUsernameOrThrowException(String username);

    UserResponseDto createUser(UserRequestDto userRequestDto);

    UserResponseDto createUserAdmin(UserRequestDto userRequestDto);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    UserResponseDto updateUser(String id, UserRequestDto userRequestDto);

    User findUserByIdOrThrowException(String username);

    void validationUsername(String username);

    void deleteUser(String id);
}
