package com.example.spring_security_mernis_auth.mapper;

import com.example.spring_security_mernis_auth.dto.UserRequestDto;
import com.example.spring_security_mernis_auth.dto.UserResponseDto;
import com.example.spring_security_mernis_auth.model.Authority;
import com.example.spring_security_mernis_auth.model.User;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserMapper {

    public static User mapToUser(UserRequestDto userRequestDto) {
        User user = new User();
        user.setUsername(userRequestDto.getUsername());
        user.setPassword(userRequestDto.getPassword());
        user.setEmail(userRequestDto.getEmail());
        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setBirthYear(userRequestDto.getBirthYear());
        user.setIdentityNumber(userRequestDto.getIdentityNumber());

        Set<Authority> authorities = userRequestDto.getAuthorities();
        user.setAuthorities(authorities);

        return user;
    }

    public static UserResponseDto mapToUserResponseDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();

        userResponseDto.setIdentityNumber(user.getIdentityNumber());
        userResponseDto.setFirstName(user.getFirstName());
        userResponseDto.setLastName(user.getLastName());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setAuthorities(user.getAuthorities());
        userResponseDto.setBirthYear(user.getBirthYear());
        userResponseDto.setCreatedAt(user.getCreatedAt());
        userResponseDto.setUpdatedAt(user.getUpdatedAt());

        return userResponseDto;
    }

}
