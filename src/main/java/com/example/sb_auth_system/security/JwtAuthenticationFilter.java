package com.example.sb_auth_system.security;

import com.example.sb_auth_system.entity.Users;
import com.example.sb_auth_system.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserRepository userRepos;
    private final JwtService jwtService;

    public JwtAuthenticationFilter(UserRepository userRepos, JwtService jwtService) {
        this.userRepos = userRepos;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String JWTtoken;
        String userEmail;

        if( authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        JWTtoken = authHeader.substring(7);
        userEmail = jwtService.extractEmail(JWTtoken);

        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            Users user = userRepos.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if(jwtService.validateToken(JWTtoken,user)){
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                        );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request,response);
    }
}
