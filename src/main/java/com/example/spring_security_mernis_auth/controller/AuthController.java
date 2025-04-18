package com.example.spring_security_mernis_auth.controller;

import com.example.spring_security_mernis_auth.dto.LoginRequestDto;
import com.example.spring_security_mernis_auth.dto.LoginResponseDto;
import com.example.spring_security_mernis_auth.dto.UserRequestDto;
import com.example.spring_security_mernis_auth.dto.UserResponseDto;
import com.example.spring_security_mernis_auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new LoginResponseDto());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        String result = authService.logout(request, response);
        return ResponseEntity.ok(result);
    }

}