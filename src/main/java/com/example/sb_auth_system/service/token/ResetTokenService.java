package com.example.sb_auth_system.service.token;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResetTokenService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String PREFIX = "reset:token:";

    public String createToken(Integer userId) {
        String token = UUID.randomUUID().toString();

        redisTemplate.opsForValue().set(
                PREFIX + token,
                userId.toString(),
                Duration.ofMinutes(15)
        );

        return token;
    }

    public Integer validateToken(String token) {
        String userId = redisTemplate.opsForValue().get(PREFIX + token);

        if (userId == null) {
            throw new RuntimeException("Invalid or expired reset token");
        }

        redisTemplate.delete(PREFIX + token);

        return Integer.valueOf(userId);
    }
}