package org.demointernetshop55mfs.controller;

import lombok.RequiredArgsConstructor;
import org.demointernetshop55mfs.security.dto.AuthRequest;
import org.demointernetshop55mfs.security.dto.AuthResponse;
import org.demointernetshop55mfs.security.service.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request){



        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.createToken(request.getUsername());
        return new ResponseEntity<>(new AuthResponse(jwt), HttpStatus.OK);
    }
}
