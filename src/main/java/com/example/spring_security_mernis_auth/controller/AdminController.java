package com.example.spring_security_mernis_auth.controller;

import com.example.spring_security_mernis_auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AuthService authService;

    public AdminController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Bütün JWT tokenlarını Redis'den siler.
     * Sadece username 'admin' olan ya da ROLE_ADMIN yetkisine sahip kullanıcılar erişebilir.
     */
    @PreAuthorize("authentication.principal.username == 'admin' || hasRole('ADMIN')")
    @DeleteMapping("/delete-all-keys")
    public ResponseEntity<String> deleteAllKeys() {
        try {
            authService.deleteAllKeys();
            return ResponseEntity.ok("Bütün tokenlar silindi!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Tokenlar silinirken hata oluştu: " + e.getMessage());
        }
    }
}

