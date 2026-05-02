package com.example.sb_auth_system.security;

import com.example.sb_auth_system.entity.RefreshToken;
import com.example.sb_auth_system.entity.Role;
import com.example.sb_auth_system.entity.Users;
import com.example.sb_auth_system.repository.RefreshTokenRepository;
import com.example.sb_auth_system.repository.UserRepository;
import com.example.sb_auth_system.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public OAuth2SuccessHandler(PasswordEncoder passwordEncoder, RefreshTokenService refreshTokenService, UserRepository userRepository, JwtService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

        Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        if (name == null || name.isBlank()) {
            name = email.substring(0, email.indexOf("@"));
        }

        String finalName = name;

        Users user = userRepository.findByEmail(email)
                .map(existingUser -> {

                    // Update username only if missing
                    if (existingUser.getUsername() == null || existingUser.getUsername().isBlank()) {
                        existingUser.setUsername(finalName);
                        userRepository.save(existingUser);
                    }

                    return existingUser;
                })
                .orElseGet(() -> {

                    Users newUser = new Users();
                    newUser.setEmail(email);
                    newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                    newUser.setRole(Role.USER);
                    newUser.setUsername(finalName);

                    return userRepository.save(newUser);
                });

        //String jwt = jwtService.generateToken(user);
        String accessToken = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        response.setContentType("application/json");
        response.getWriter().write("{\"accessToken\": \"" + accessToken + "\",");
        response.getWriter().write("\"refreshToken\": \"" + refreshToken.getToken() + "\"}");
        response.getWriter().flush();
    }
}
