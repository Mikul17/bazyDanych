package com.mikul17.bazyDanych.Response;

import com.mikul17.bazyDanych.Models.Address;
import lombok.*;

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
    private Address address;
}
