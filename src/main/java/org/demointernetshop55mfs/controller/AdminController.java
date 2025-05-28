package org.demointernetshop55mfs.controller;

import lombok.RequiredArgsConstructor;
import org.demointernetshop55mfs.controller.api.AdminApi;
import org.demointernetshop55mfs.dto.UserResponseDto;
import org.demointernetshop55mfs.entity.User;
import org.demointernetshop55mfs.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminController implements AdminApi {

    private final UserService service;


    @Override
    public ResponseEntity<List<User>> findAllFullDetails() {
        return ResponseEntity.ok(service.findFullDetailsUsers());
    }

    @Override
    public ResponseEntity<List<UserResponseDto>> findAll() {
        return ResponseEntity.ok(service.findAllUsers());
    }

    @Override
    public boolean deleteUser(@PathVariable Integer id) {
        return service.deleteUser(id);
    }
}
