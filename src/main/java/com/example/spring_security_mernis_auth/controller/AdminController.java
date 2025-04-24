package com.example.spring_security_mernis_auth.controller;

import com.example.spring_security_mernis_auth.dto.UserResponseDto;
import com.example.spring_security_mernis_auth.service.AuthService;
import com.example.spring_security_mernis_auth.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AuthService authService;

    private final UserService userService;

    public AdminController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    /**
     * Bütün JWT tokenlarını Redis'den siler.
     * Sadece username 'admin' olan ya da ROLE_ADMIN yetkisine sahip kullanıcılar erişebilir.
     */
    @DeleteMapping("/delete-all-keys")
    @PreAuthorize("authentication.principal.username == 'admin' || hasRole('ADMIN')")
    public ResponseEntity<String> deleteAllKeys() {
        try {
            authService.deleteAllKeys();
            return ResponseEntity.ok("Bütün tokenlar silindi!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Tokenlar silinirken hata oluştu: " + e.getMessage());
        }
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}

