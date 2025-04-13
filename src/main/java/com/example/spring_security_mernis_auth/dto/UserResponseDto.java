package com.example.spring_security_mernis_auth.dto;

import com.example.spring_security_mernis_auth.enums.Role;
import jakarta.validation.constraints.Min;

public class UserResponseDto {

    private Long identityNumber;

    private String firstName;

    private String lastName;

    @Min(value = 1900, message = "Birth Year must be after 1900.")
    private int birthYear;

    private Role role;



    public Long getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(Long identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserResponseDto() {
    }

    public UserResponseDto(Long identityNumber, String firstName, String lastName, int birthYear, Role role) {
        this.identityNumber = identityNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthYear = birthYear;
        this.role = role;
    }
}
