package org.demointernetshop55mfs.controller;

import lombok.RequiredArgsConstructor;
import org.demointernetshop55mfs.controller.api.AdminApi;
import org.demointernetshop55mfs.controller.api.UserApi;
import org.demointernetshop55mfs.dto.UserResponseDto;
import org.demointernetshop55mfs.entity.User;
import org.demointernetshop55mfs.service.UserService;
import org.demointernetshop55mfs.service.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService service;

    @Override
    public ResponseEntity<UserResponseDto> findUserById(Integer id) {
            return ResponseEntity.ok(service.findUserById(id));
    }

    @Override
    public ResponseEntity<UserResponseDto> findUserByEmail(String email) {
        return ResponseEntity.ok(service.findUserByEmail(email));
    }

}
