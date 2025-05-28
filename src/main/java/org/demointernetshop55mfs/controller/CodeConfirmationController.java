package org.demointernetshop55mfs.controller;

import lombok.RequiredArgsConstructor;
import org.demointernetshop55mfs.dto.UserResponseDto;
import org.demointernetshop55mfs.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public")
public class CodeConfirmationController {

    private final UserService userService;

    @GetMapping("/confirmation")
    public ResponseEntity<UserResponseDto> confirmationEmail(@RequestParam String confirmationCode){
        return new ResponseEntity<>(userService.confirmationEmail(confirmationCode), HttpStatus.OK);
    }
}
