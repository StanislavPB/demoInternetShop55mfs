package org.demointernetshop55mfs.controller;

import lombok.RequiredArgsConstructor;
import org.demointernetshop55mfs.security.dto.AuthRequest;
import org.demointernetshop55mfs.security.dto.AuthResponse;
import org.demointernetshop55mfs.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public ResponseEntity<AuthResponse> authenticate(AuthRequest request) {

        String jwt = authService.generateJwt(request);

        return new ResponseEntity<>(new AuthResponse(jwt), HttpStatus.OK);
    }
}
