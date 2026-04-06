package com.example.sb_auth_system.controller;

import com.example.sb_auth_system.dto.JwtResponse;
import com.example.sb_auth_system.dto.RefreshTokenRequest;
import com.example.sb_auth_system.entity.RefreshToken;
import com.example.sb_auth_system.entity.Users;
import com.example.sb_auth_system.service.AuthService;
import com.example.sb_auth_system.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    AuthService authService;

    @GetMapping("/test")
    public String test() {
        return "WORKING";
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody Users user){
        return  ResponseEntity.ok(authService.login(user));
    }

    @PostMapping("/register")
    public ResponseEntity<Users> register(@RequestBody Users user){
        return  ResponseEntity.ok(authService.register(user));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshToken> refreshToken(@RequestBody RefreshTokenRequest refreshToken){
        return ResponseEntity.ok((refreshTokenService.verifyAndGetToken(refreshToken.toString())));
    }
}
