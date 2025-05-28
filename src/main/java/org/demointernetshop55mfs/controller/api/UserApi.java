package org.demointernetshop55mfs.controller.api;

import org.demointernetshop55mfs.dto.UserRequestDto;
import org.demointernetshop55mfs.dto.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/users")
public interface UserApi {

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findUserById(@PathVariable Integer id);


    @GetMapping()
    public ResponseEntity<UserResponseDto> findUserByEmail(@RequestParam String email);
}
