package com.example.spring_security_mernis_auth.controller;

import com.example.spring_security_mernis_auth.dto.LoginRequestDto;
import com.example.spring_security_mernis_auth.dto.LoginResponseDto;
import com.example.spring_security_mernis_auth.dto.UserRequestDto;
import com.example.spring_security_mernis_auth.dto.UserResponseDto;
import com.example.spring_security_mernis_auth.service.AuthService;
import com.example.spring_security_mernis_auth.service.JwtTokenCacheService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    private final JwtTokenCacheService jwtTokenCacheService;

    public AuthController(AuthService authService, JwtTokenCacheService jwtTokenCacheService) {
        this.authService = authService;
        this.jwtTokenCacheService = jwtTokenCacheService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody @Valid UserRequestDto userRequestDto) {
        try {
            UserResponseDto response = authService.register(userRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new UserResponseDto());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        try {
            LoginResponseDto response = authService.login(loginRequestDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new LoginResponseDto());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return ResponseEntity.ok("Logged out");
    }

}