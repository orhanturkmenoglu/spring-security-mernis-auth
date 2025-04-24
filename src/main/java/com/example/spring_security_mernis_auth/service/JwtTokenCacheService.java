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
            System.out.println("Token geçersiz veya eşleşmiyor.");
            return false;
        }

        Long expire = redisTemplate.getExpire("access:" + username, TimeUnit.SECONDS);
        System.out.println("Expire süresi: " + expire);

        return expire != null && expire > 0 && !isTokenBlackListed(token);
    }

    public boolean isRefreshTokenValid(String token, String username) {
        String cachedUsername = getRefreshToken(token);
        System.out.println("cachedRefreshToken :" + cachedUsername
        );
        if (cachedUsername == null || !cachedUsername.equals(username)) {
            System.out.println("Token geçersiz veya eşleşmiyor.");
            return false;
        }

        Long expire = redisTemplate.getExpire("refresh:" + token, TimeUnit.SECONDS);
        System.out.println("Expire Süresi: " + expire);

        return expire != null && expire > 0;
    }


    public boolean isTokenBlackListed(String token) {
        System.out.println("isTokenBlackListed" + token);
        return redisTemplate.opsForSet().isMember("BLACKLISTED", token);
    }

    public void deleteAccessToken(String username) {
        System.out.println("deleteAccessToken" + username);
        redisTemplate.delete("access:" + username);
    }

    public void deleteRefreshToken(String username) {
        System.out.println("deleteRefreshToken" + username);
        redisTemplate.delete("refresh:" + username);
    }


    public void invalidateOldTokenAndStoreNew(String oldToken, String newAccessToken, String newRefreshToken, String username) {
        System.out.println("oldToken" + oldToken);
        addToBlacklist(oldToken);

        System.out.println("deleteAccessToken :" + oldToken);
        deleteAccessToken(oldToken);

        System.out.println("deleteRefreshToken :" + username);
        deleteRefreshToken(username);

        System.out.println("newAccessToken :" + username);
        storeAccessToken(newAccessToken, username);

        System.out.println("newRefreshToken :" + username);
        storeRefreshToken(newRefreshToken, username);
    }


}
