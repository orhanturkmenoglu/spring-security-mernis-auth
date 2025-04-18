package com.example.spring_security_mernis_auth.mapper;

import com.example.spring_security_mernis_auth.dto.UserRequestDto;
import com.example.spring_security_mernis_auth.dto.UserResponseDto;
import com.example.spring_security_mernis_auth.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {


    public static User mapToUser(UserRequestDto userRequestDto){
        User user = new User();
        user.setUsername(userRequestDto.getUsername());
        user.setPassword(userRequestDto.getPassword());
        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setBirthYear(userRequestDto.getBirthYear());
        user.setIdentityNumber(userRequestDto.getIdentityNumber());

        return user;
    }

    public static UserResponseDto mapToUserResponseDto(User user){
        UserResponseDto userResponseDto=new UserResponseDto();

        userResponseDto.setIdentityNumber(user.getIdentityNumber());
        userResponseDto.setFirstName(user.getFirstName());
        userResponseDto.setLastName(user.getLastName());
        userResponseDto.setAuthorities(user.getAuthorities());
        userResponseDto.setBirthYear(user.getBirthYear());

        return userResponseDto;
    }

}
