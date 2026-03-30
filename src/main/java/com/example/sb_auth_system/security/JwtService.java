package com.example.sb_auth_system.security;

import com.example.sb_auth_system.entity.Users;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;

public class JwtService {
    private String SECRET_KEY = "5tLvdGu1dvmGOPpnyC8egHYWBvXo6excpSShNo7VO6O";

    private Key getSignInKey(){
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
                .signWith(getSignInKey())
                .compact();

        return token;
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSignInKey()) // Required to verify the signature
                .build()
                .parseSignedClaims(token)   // Handles signed tokens (JWS)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token , CustomUserDetails userDetails){
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername()));
    }

    private Date extractExpiration(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }


}
