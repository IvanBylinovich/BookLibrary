package com.solbeg.BookLibrary.controller;

import com.solbeg.BookLibrary.config.SwaggerConfig;
import com.solbeg.BookLibrary.dto.UserRequestDto;
import com.solbeg.BookLibrary.dto.UserResponseDto;
import com.solbeg.BookLibrary.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Api(tags = {SwaggerConfig.USER_SERVICE_TAG})
public class UserController {

    private final UserService userService;

    @GetMapping("/allUsers")
    @ApiOperation(value = "Get all users", notes = "Provide method to get all users", response = ResponseEntity.class)
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok().body(userService.findAllUsers());
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get user by id", notes = "Provide method to get user", response = ResponseEntity.class)
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable String id) {
        return ResponseEntity.ok().body(userService.findUserById(id));
    }

    @GetMapping("/refreshToken")
    @ApiOperation(value = "Refresh token", notes = "Provide method to refresh token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        userService.refreshToken(request, response);
    }

    @PostMapping("/registration")
    @ApiOperation(value = "User registration", notes = "Provide method to register user")
    public void createUser(@RequestBody @Valid UserRequestDto userRequestDto) {
        userService.createUser(userRequestDto);
    }

    @PostMapping("/admin")
    @ApiOperation(value = "Create user with role ADMIN", notes = "Provide method to create user with role ADMIN")
    public void createUserAdmin(@RequestBody @Valid UserRequestDto userRequestDto) {
        userService.createUserAdmin(userRequestDto);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update user", notes = "Provide method to update user", response = ResponseEntity.class)
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable String id, @RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok().body(userService.updateUser(id, userRequestDto));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete user", notes = "Provide method to delete user")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }
}
