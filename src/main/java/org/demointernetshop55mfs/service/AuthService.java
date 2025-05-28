package org.demointernetshop55mfs.service;

import lombok.RequiredArgsConstructor;
import org.demointernetshop55mfs.security.dto.AuthRequest;
import org.demointernetshop55mfs.security.service.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public String generateJwt(AuthRequest request){

        /*
        не надо делать никакую дополнительную проверку потому что

        Когда мы вызываем authenticationManager.authenticate(...), Spring:

    Идёт в UserDetailsService.loadUserByUsername(username),

    Если пользователя нет в базе — выбрасывается UsernameNotFoundException,

    Если есть, но пароль неправильный — BadCredentialsException,

    Если всё нормально — возвращается Authentication объект с Principal, Authorities и т.д.
         */

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.createToken(request.getUsername());
        return jwt;
    }

}
