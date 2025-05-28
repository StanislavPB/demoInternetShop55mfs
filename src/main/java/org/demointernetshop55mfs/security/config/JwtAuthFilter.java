package org.demointernetshop55mfs.security.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.demointernetshop55mfs.security.service.CustomUserDetailsService;
import org.demointernetshop55mfs.security.service.InvalidJwtException;
import org.demointernetshop55mfs.security.service.JwtTokenProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            String jwt = getTokenFromHttpRequest(request);

            if (jwt != null && jwtTokenProvider.validateToken(jwt)){
                String username = jwtTokenProvider.getUsernameFromJwt(jwt);
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (InvalidJwtException e){
            return;
        }

        filterChain.doFilter(request,response);
    }

    private String getTokenFromHttpRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        /*
        если в http запросе, который мы получили есть jwt, то тогда в объекте HttpServletRequest
        будет присутствовать строка, которая выглядит так: "Bearer fjahgvkgyi3847tgiufsvgkajfdhsvgkajsasgf"
        то есть нам надо из этой строки взять ВСЕ до конца начиная с первого символа после "Bearer "
         */
        if (bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }

        return null;
    }
}
