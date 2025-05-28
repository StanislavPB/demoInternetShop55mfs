package org.demointernetshop55mfs.controller.api;

import org.demointernetshop55mfs.dto.UserRequestDto;
import org.demointernetshop55mfs.dto.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/public")
public interface PublicApi {

    // добавление нового пользователя

    @PostMapping("/registration")
    public ResponseEntity<UserResponseDto> addNewUser(@RequestBody UserRequestDto request);
}
