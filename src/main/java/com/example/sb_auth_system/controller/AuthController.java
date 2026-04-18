package com.example.sb_auth_system.controller;

import com.example.sb_auth_system.dto.JwtResponse;
import com.example.sb_auth_system.dto.RefreshTokenRequest;
import com.example.sb_auth_system.entity.RefreshToken;
import com.example.sb_auth_system.entity.Users;
import com.example.sb_auth_system.service.AuthService;
import com.example.sb_auth_system.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Login user", description = "Authenticate user and return tokens")
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody Users user){
        return  ResponseEntity.ok(authService.login(user));
    }

    @PostMapping("/register")
    public ResponseEntity<Users> register(@RequestBody Users user){
        return  ResponseEntity.ok(authService.register(user));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeder){
        return  ResponseEntity.ok(authService.logout(authHeder));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }

}
