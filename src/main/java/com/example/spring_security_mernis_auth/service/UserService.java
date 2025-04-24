package com.example.spring_security_mernis_auth.service;

import com.example.spring_security_mernis_auth.dto.UserResponseDto;
import com.example.spring_security_mernis_auth.mapper.UserMapper;
import com.example.spring_security_mernis_auth.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::mapToUserResponseDto)
                .toList();
    }

    public UserResponseDto getCurrentUserInfo(String username) {
        return userRepository.findByUsername(username)
                .map(UserMapper::mapToUserResponseDto)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
