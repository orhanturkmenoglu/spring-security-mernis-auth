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
        redisTemplate.opsForValue().set("access:" + username, token, ACCESS_EXPIRATION, TimeUnit.MILLISECONDS);
    }

    public void storeRefreshToken(String refreshToken, String username) {
        redisTemplate.opsForValue().set("refresh:" + refreshToken, username, REFRESH_EXPIRATION, TimeUnit.MILLISECONDS);
    }

    public String getAccessToken(String username) {
        return redisTemplate.opsForValue().get("access:" + username);
    }

    public String getRefreshToken(String refreshToken) {
        return redisTemplate.opsForValue().get("refresh:" + refreshToken);
    }


    public void addToBlacklist(String token) {
        redisTemplate.opsForSet().add("BLACKLISTED", token);
    }

    public boolean isTokenValid(String token, String username) {
        String cachedToken = getAccessToken(username);

        if (cachedToken == null || !cachedToken.equals(token)) {
            return false;
        }

        Long expire = redisTemplate.getExpire("access:" + username, TimeUnit.SECONDS);

        return expire != null && expire > 0 && !isTokenBlackListed(token);
    }

    public boolean isRefreshTokenValid(String refreshToken, String username) {
        String cachedUsername = getRefreshToken(refreshToken);

        if (cachedUsername == null || !cachedUsername.equals(username)) {
            System.out.println("Token geçersiz veya eşleşmiyor.");
            return false;
        }

        Long expire = redisTemplate.getExpire("refresh:" + refreshToken, TimeUnit.SECONDS);

        return expire != null && expire > 0;
    }


    public boolean isTokenBlackListed(String token) {
        return redisTemplate.opsForSet().isMember("BLACKLISTED", token);
    }

    public void deleteAccessToken(String username) {
        redisTemplate.delete("access:" + username);
    }

    public void deleteRefreshToken(String refreshToken) {
        redisTemplate.delete("refresh:" + refreshToken);
    }


    public void invalidateOldTokenAndStoreNew(String oldToken, String newAccessToken, String newRefreshToken, String username) {
        addToBlacklist(oldToken);

        deleteAccessToken(oldToken);

        deleteRefreshToken(oldToken);

        storeAccessToken(newAccessToken, username);

        storeRefreshToken(newRefreshToken, username);
    }


}
