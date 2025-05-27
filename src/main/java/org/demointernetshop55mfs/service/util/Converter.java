package org.demointernetshop55mfs.service.util;

import lombok.RequiredArgsConstructor;
import org.demointernetshop55mfs.dto.UserRequestDto;
import org.demointernetshop55mfs.dto.UserResponseDto;
import org.demointernetshop55mfs.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class Converter {

    public User fromDto(UserRequestDto request){
        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .hashPassword(request.getHashPassword())
                .build();
    }

    public UserResponseDto toDto(User user){
        return UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole().toString())
                .build();
    }

    public List<UserResponseDto> fromUser(List<User> users){
        return users.stream()
                .map(user -> toDto(user))
                .toList();
    }
}
