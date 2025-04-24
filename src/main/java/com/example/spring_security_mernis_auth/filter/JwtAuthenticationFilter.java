package com.example.spring_security_mernis_auth.filter;

import com.example.spring_security_mernis_auth.service.CustomUserDetailsService;
import com.example.spring_security_mernis_auth.service.JwtTokenCacheService;
import com.example.spring_security_mernis_auth.service.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import reactor.util.annotation.NonNull;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;

    private final CustomUserDetailsService customUserDetailsService;

    private final JwtTokenCacheService jwtTokenCacheService;

    private final AntPathMatcher antPathMatcher;


    private static final List<String> EXCLUDE_URLS = List.of(
            "/api/v1/auth/register",
            "/api/v1/auth/login",
            "/api/v1/auth/logout",
            "/api/v1/auth/refresh-token"
    );

    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, CustomUserDetailsService customUserDetailsService, JwtTokenCacheService jwtTokenCacheService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenCacheService = jwtTokenCacheService;
        this.antPathMatcher = new AntPathMatcher();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token;
        String username;
        String cachedToken;

        // Authorization header kontrolü
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        token = authHeader.substring(7);  // Bearer token'ı çıkar
        username = jwtTokenUtil.extractUsername(token);  // Token'dan kullanıcı adı çıkart

        System.out.println("Token :"+token);
        System.out.println("Username :"+username);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Redis üzerinden token kontrolü
            cachedToken  = jwtTokenCacheService.getAccessToken(username);  // Redis'ten token al
            System.out.println("cachedToken  :"+cachedToken );
            // Eğer Redis'te token varsa ve gelen token ile eşleşiyorsa
            if (cachedToken != null && cachedToken.equals(token) && jwtTokenCacheService.isTokenValid(token,username)) {
                // JWT token'ı geçerliyse, userDetails'i yükle
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                if (jwtTokenUtil.isValidateToken(token, userDetails)) {
                    // Kimlik doğrulaması başarılıysa, authentication token'ını oluştur
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        // Diğer filtreleri çalıştır
        filterChain.doFilter(request, response);
    }


    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        return EXCLUDE_URLS.stream().anyMatch(url -> antPathMatcher.match(url, request.getServletPath()));
    }
}
