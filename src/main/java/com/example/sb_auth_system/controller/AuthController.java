package com.example.sb_auth_system.controller;

import com.example.sb_auth_system.dto.JwtResponse;
import com.example.sb_auth_system.dto.RefreshTokenRequest;
import com.example.sb_auth_system.entity.Users;
import com.example.sb_auth_system.repository.UserRepository;
import com.example.sb_auth_system.service.AuthService;
import com.example.sb_auth_system.service.RefreshTokenService;
import com.example.sb_auth_system.service.ResetTokenService;
import com.example.sb_auth_system.service.VerificationTokenService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @GetMapping("/test")
    public String test() {
        return "WORKING";
    }

    @Operation(summary = "Login user", description = "Authenticate user and return tokens")
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody Users user,
                                             HttpServletResponse response) {

        return ResponseEntity.ok(authService.login(user, response));
    }

    @PostMapping("/register")
    public ResponseEntity<Users> register(@RequestBody Users user) {

        Users registeredUser = authService.register(user);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(HttpServletRequest request,
                                                    HttpServletResponse response) {

        return ResponseEntity.ok(authService.refresh(request, response));
    }

    @GetMapping("/oauth2/google")
    public void redirectToGoogle(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/google");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request,
                                         HttpServletResponse response) {

        return ResponseEntity.ok(authService.logout(request, response));
    }

    @GetMapping("/verify")
    public String verify(@RequestParam String token) {
        try {
            authService.verify(token);
            return "Your account has been verified successfully! You can now log in.";

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }


}
