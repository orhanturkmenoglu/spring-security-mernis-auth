package com.example.spring_security_mernis_auth.controller;

import com.example.spring_security_mernis_auth.dto.*;
import com.example.spring_security_mernis_auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
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

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponseDto> refreshAccessToken(@RequestHeader("Authorization") String refreshToken) {
        LoginResponseDto refreshedAccessToken = authService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(refreshedAccessToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return ResponseEntity.ok("Logged out");
    }

    @PutMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordRequestDto updatePasswordRequestDto) {
        String updatePassword = authService.updatePassword(updatePasswordRequestDto);
        return ResponseEntity.ok(updatePassword);
    }
}