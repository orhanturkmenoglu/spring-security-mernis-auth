package com.example.spring_security_mernis_auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class JwtTokenCacheService {

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${spring.security.access.token.expiration}")
    private long ACCESS_EXPIRATION;

    @Value("${spring.security.refresh.token.expiration}")
    private long REFRESH_EXPIRATION;

    public JwtTokenCacheService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void storeAccessToken(String token, String username) {
        redisTemplate.opsForValue().set(token, username, ACCESS_EXPIRATION, TimeUnit.MILLISECONDS);
    }

    public void storeRefreshToken(String refreshToken, String username) {
        redisTemplate.opsForValue().set("refresh:" + username, refreshToken, REFRESH_EXPIRATION, TimeUnit.MILLISECONDS);
    }

    public String getAccessToken(String token) {
        return redisTemplate.opsForValue().get(token);
    }

    public String getRefreshToken(String username) {
        return redisTemplate.opsForValue().get("refresh:" + username);
    }

    public void addToBlacklist(String token) {
        redisTemplate.opsForSet().add("BLACKLISTED", token);
    }

    public boolean isTokenValid(String token) {
        Long expire = redisTemplate.getExpire(token, TimeUnit.SECONDS);
        return expire != null && expire > 0 && !isTokenBlackListed(token);
    }

    public boolean isTokenBlackListed(String token) {
        return redisTemplate.opsForSet().isMember("BLACKLISTED", token);
    }

    public void deleteAccessToken(String token) {
        redisTemplate.delete(token);
    }

    public void deleteRefreshToken(String username) {
        redisTemplate.delete("refresh:" + username);
    }


}
