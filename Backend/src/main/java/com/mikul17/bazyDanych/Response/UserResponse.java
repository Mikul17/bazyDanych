package com.mikul17.bazyDanych.Response;

import com.mikul17.bazyDanych.Models.Address;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String accountNumber;
    private Double balance;
    private LocalDate birthDate;
    private Address address;
}
