package com.mikul17.bazyDanych.Request;

import com.mikul17.bazyDanych.Models.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "First name is required")
    @Size(max = 30, message = "First name cannot be longer than 30 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 30, message = "Last name cannot be longer than 30 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Size(max = 50, message = "Email cannot be longer than 50 characters")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min=7, max = 50, message = "Password must be between 7 and 50 characters")
    private String password;

    @NotBlank(message = "Phone number is required")
    @Size(min = 7, max = 15, message = "Phone number must be between 7 and 15 characters")
    private String phoneNumber;

    @Size(min = 26, max = 26, message = "Account number must be 26 characters long")
    private String accountNumber;

    @NotBlank(message = "SSN is required")
    @Size(min = 11, max = 11, message = "SSN must be 11 characters long")
    private String SSN;

    // Fields for Address
    @NotBlank(message = "Country is required")
    @Size(max = 2, message = "Country cannot be longer than 2 characters")
    private String country;

    @NotBlank(message = "City is required")
    @Size(max = 50, message = "City cannot be longer than 50 characters")
    private String city;

    @NotBlank(message = "Street is required")
    @Size(max = 50, message = "Street cannot be longer than 50 characters")
    private String street;

    @NotBlank(message = "Street number is required")
    @Size(max = 5, message = "Street number cannot be longer than 5 characters")
    private String streetNumber;

    @NotBlank(message = "House number is required")
    @Size(max = 5, message = "House number cannot be longer than 5 characters")
    private String houseNumber;

    @NotBlank(message = "Zip code is required")
    @Size(max = 6, message = "Zip code cannot be longer than 6 characters")
    private String zipCode;
}
