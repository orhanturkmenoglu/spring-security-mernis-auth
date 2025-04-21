package com.example.spring_security_mernis_auth.service;

import com.example.spring_security_mernis_auth.model.Authority;
import com.example.spring_security_mernis_auth.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtTokenUtil {

    @Value("${spring.security.secret.key}")
    private  String SECRET_KEY;

    @Value("${spring.security.expiration}")
    private long EXPIRATION;


    public String generateToken(User user) {
        String roles = user.getAuthorities().stream()
                .map(Authority::getAuthority) // Örn: "ROLE_USER"
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setIssuer("ORHAN TÜRKMENOĞLU")
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)) // 1 hour
                .claim("roles", roles)
                .signWith(SignatureAlgorithm.HS256,getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean isValidateToken(String token, UserDetails userDetails) {
        final String username = getClaims(token).getSubject();
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }

}
