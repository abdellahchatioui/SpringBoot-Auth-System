package com.example.sb_auth_system.service;

import com.example.sb_auth_system.dto.JwtResponse;
import com.example.sb_auth_system.dto.RefreshTokenRequest;
import com.example.sb_auth_system.entity.RefreshToken;
import com.example.sb_auth_system.entity.Role;
import com.example.sb_auth_system.entity.Users;
import com.example.sb_auth_system.repository.UserRepository;
import com.example.sb_auth_system.security.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private RefreshTokenService  refreshTokenService;
    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepos;
    private final JwtService jwtService;

    public AuthService(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, UserRepository userRepos, JwtService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userRepos = userRepos;
        this.jwtService = jwtService;
    }

    @Transactional
    public JwtResponse login(Users user){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        user.getPassword()
                )
        );

        Users findUser = userRepos.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        refreshTokenService.deleteByUser(findUser);

        String accessToken = jwtService.generateToken(findUser);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(findUser);
        return new JwtResponse(accessToken,refreshToken.getToken());
    }

    public Users register(Users user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepos.save(user);
        return user;
    }

    @Transactional
    public JwtResponse refresh(RefreshTokenRequest request) {

        RefreshToken oldRefreshToken = refreshTokenService.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        refreshTokenService.verifyExpiration(oldRefreshToken);

        Users user = oldRefreshToken.getUser();

        refreshTokenService.deleteByUser(user);

        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);

        String newAccessToken = jwtService.generateToken(user);

        return new JwtResponse(newAccessToken, newRefreshToken.getToken());
    }

    public String logout(String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return "Invalid token";
        }

        String token = authHeader.substring(7);

        long expiration = jwtService.getExpirationTime(token);

        tokenBlacklistService.blacklistToken(token, expiration);

        return "Logged out successfully";
    }
}
