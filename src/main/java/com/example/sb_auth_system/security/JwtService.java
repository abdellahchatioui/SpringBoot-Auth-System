package com.example.sb_auth_system.security;

import com.example.sb_auth_system.entity.Users;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;

public class JwtService {
    private String SECRET_KEY = "5tLvdGu1dvmGOPpnyC8egHYWBvXo6excpSShNo7VO6O";

    private Key getKey(){
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        return key;
    }

    public  String generateToken(Users user){

        String token = Jwts.builder()
                .subject(user.getEmail())
                .claims(user.getRole())
                .issuedAt(new Date(System.currentTimeMillis() ))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getKey())
                .compact();

        return token;
    }


}
