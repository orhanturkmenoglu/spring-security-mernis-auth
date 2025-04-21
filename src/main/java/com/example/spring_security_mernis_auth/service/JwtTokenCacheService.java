package com.example.spring_security_mernis_auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class JwtTokenCacheService {

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${spring.security.expiration}")
    private long EXPIRATION;

    public JwtTokenCacheService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void storeToken(String token, String username) {
        redisTemplate.opsForValue().set(username, token, EXPIRATION, TimeUnit.MILLISECONDS);
    }

    public String getToken(String username) {
        return redisTemplate.opsForValue().get(username);
    }

    public void deleteToken(String username) {
        redisTemplate.delete(username);
    }
}
