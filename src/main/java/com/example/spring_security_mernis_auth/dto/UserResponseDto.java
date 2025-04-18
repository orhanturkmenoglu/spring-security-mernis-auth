package com.example.spring_security_mernis_auth.dto;

import com.example.spring_security_mernis_auth.enums.Role;
import com.example.spring_security_mernis_auth.model.Authority;
import jakarta.validation.constraints.Min;

import java.util.List;
import java.util.Set;

public class UserResponseDto {

    private Long identityNumber;

    private String firstName;

    private String lastName;

    @Min(value = 1900, message = "Birth Year must be after 1900.")
    private int birthYear;

    private Set<Authority> authorities;

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


    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "UserResponseDto{" +
                "identityNumber=" + identityNumber +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthYear=" + birthYear +
                ", authorities=" + authorities +
                '}';
    }
}
