package org.demointernetshop55mfs.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.lifetime}")
    private String jwtLifetime;


    public String createToken(String username){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtLifetime);

        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token){
        try{
            Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

            Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
        } catch (JwtException e) {
            throw new InvalidJwtException("Invalid JWT token: " + e.getMessage());
        }

        return true;
    }

    public String getUsernameFromJwt(String token){
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // вытаскиваем из claims (из части payload нашего JWT)
        // из низ берем содержимое поля subject

        return claims.getSubject();
    }

}
