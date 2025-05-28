package org.demointernetshop55mfs.controller;

import lombok.RequiredArgsConstructor;
import org.demointernetshop55mfs.controller.api.PublicApi;
import org.demointernetshop55mfs.dto.UserRequestDto;
import org.demointernetshop55mfs.dto.UserResponseDto;
import org.demointernetshop55mfs.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PublicController implements PublicApi {

    private final UserService service;

    @Override
    public ResponseEntity<UserResponseDto> addNewUser(UserRequestDto request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.registration(request));
    }
}
