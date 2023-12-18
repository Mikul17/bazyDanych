package com.mikul17.bazyDanych.Request;

import com.mikul17.bazyDanych.Models.Address;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressChangeRequest {
    private Address address;
    private Long userId;
}
