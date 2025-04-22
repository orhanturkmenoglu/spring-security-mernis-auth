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
        redisTemplate.opsForValue().set(token, username, EXPIRATION, TimeUnit.MILLISECONDS);
    }

    public String getToken(String token) {
        return redisTemplate.opsForValue().get(token);
    }

    public boolean isTokenValid(String token) {
        Long expire = redisTemplate.getExpire(token, TimeUnit.SECONDS);
        return expire != null && expire > 0;
    }

    public void deleteToken(String username) {
        redisTemplate.delete(username);
    }
}
