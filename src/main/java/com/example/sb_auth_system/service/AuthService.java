package com.example.sb_auth_system.service;

import com.example.sb_auth_system.dto.JwtResponse;
import com.example.sb_auth_system.dto.RefreshTokenRequest;
import com.example.sb_auth_system.entity.RefreshToken;
import com.example.sb_auth_system.entity.Role;
import com.example.sb_auth_system.entity.Users;
import com.example.sb_auth_system.repository.UserRepository;
import com.example.sb_auth_system.security.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

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
    public JwtResponse login(Users user , HttpServletResponse response){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        user.getPassword()
                )
        );

        Users findUser = userRepos.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // refreshTokenService.deleteByUser(findUser);

        String accessToken = jwtService.generateToken(findUser);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(findUser);

        Cookie cookie = new Cookie("refreshToken", refreshToken.getToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // true in production
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);

        response.addCookie(cookie);
        return new JwtResponse(accessToken);
    }

    public Users register(Users user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepos.save(user);
        return user;
    }

    @Transactional
    public JwtResponse refresh(HttpServletRequest request,
                               HttpServletResponse response) {

        String refreshToken = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("refreshToken"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        RefreshToken token = refreshTokenService.verifyExpiration(
                refreshTokenService.findByToken(refreshToken)
                        .orElseThrow(() -> new RuntimeException("Invalid refresh token"))
        );

        Users user = token.getUser();

        // 🔁 ROTATION
        refreshTokenService.deleteByUser(user);
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);

        String newAccessToken = jwtService.generateToken(user);

        Cookie cookie = new Cookie("refreshToken", newRefreshToken.getToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);

        response.addCookie(cookie);

        return new JwtResponse(newAccessToken);
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
