package com.solbeg.BookLibrary.mapper;

import com.solbeg.BookLibrary.dto.UserRequestDto;
import com.solbeg.BookLibrary.dto.UserResponseDto;
import com.solbeg.BookLibrary.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;

    public User convertUserRequestDtoToUser(UserRequestDto userRequestDto) {
        return modelMapper.map(userRequestDto, User.class);
    }

    public UserResponseDto convertUserToUserResponseDto(User user) {
        return modelMapper.map(user, UserResponseDto.class);
    }
}
